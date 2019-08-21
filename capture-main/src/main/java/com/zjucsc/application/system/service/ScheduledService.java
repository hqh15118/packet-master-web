package com.zjucsc.application.system.service;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.D2DWrapper;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.system.service.common_impl.CapturePacketServiceImpl;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.system.service.hessian_iservice.IArtHistoryDataService;
import com.zjucsc.application.system.service.hessian_mapper.DeviceMapper;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.SysRunStateUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static com.zjucsc.application.config.StatisticsData.*;

@Slf4j
@Service
public class ScheduledService {

    @Autowired public PacketAnalyzeService packetAnalyzeService;

    @Autowired private IArtHistoryDataService iArtHistoryDataService;

    @Autowired private DeviceMapper deviceMapper;

    private LinkedBlockingQueue<FvDimensionLayer> fvDimensionLayers = new LinkedBlockingQueue<>(5);

    @Autowired
    public ScheduledService(CapturePacketService capturePacketService ){
        capturePacketService.setNewFvDimensionCallback(layer -> {
            fvDimensionLayers.offer(layer);
        });
        SEND_CONSUMER.setListMap(graphInfoInList);
    }

    private final HashMap<String,Integer> attackByDevice = new HashMap<>(10);
    //private final HashMap<String,Integer> exceptionByDevice = new HashMap<>(10);
    private final HashMap<String,Integer> numberByDeviceIn = new HashMap<>(10);
    private final HashMap<String,Integer> numberByDeviceOut = new HashMap<>(10);
    private final HashMap<String,GraphInfo> graphInfoInList = new HashMap<>(10);
    private final SenderConsumer SEND_CONSUMER = new SenderConsumer();
    private final DevicePacketNumConsumer MAX_FLOW_CONSUMER = new DevicePacketNumConsumer();
    private final BiConsumer<String,GraphInfo> GRAPH_INFO_CONSUMER =
            StatisticsData::addDeviceGraphInfo;

    @Scheduled(fixedDelay = 1000)
    @Async(value = "common_schedule")
    public void commonScheduleService(){
        if (CacheUtil.getScheduleServiceRunningState()){
            try {
                sendAllFvDimensionPacket2();    //每秒钟发送一次五元组
            } catch (InterruptedException e) {
                System.err.println("五元组发送异常");
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    @Async(value = "common_service")
    public void commonService(){
        sendGraphInfo();        //工艺参数
        statisticFlow();        //统计总流量，超过限制报错
        CacheUtil.updateAttackLog();  //统计攻击类型，及所占比例
        //detectDecodeMethodDelay();
    }

    private void detectDecodeMethodDelay() {
        Map<String,Long> map = ArtDecodeCommon.getDecodeDelayMapInfo();
        map.forEach((s, aLong) -> {
            System.out.println(s + " time - " + aLong);
        });
    }

    @Scheduled(fixedRate = 5000)
    @Async("device_schedule_service")
    public void deviceStatisticsInfo(){
        if (CacheUtil.getScheduleServiceRunningState()) {
            sendPacketStatisticsMsg();      //报文统计信息，包括...
        }
    }


    //判断系统运行状态
    @Scheduled(fixedRate = 2000)
    public void sysRunState() throws SigarException {
        SysRunStateUtil.StateWrapper wrapper = SysRunStateUtil.getSysRunState();
        wrapper.setDbState(deviceMapper.selectDatabaseStatus());
        SocketServiceCenter.updateAllClient(SocketIoEvent.SYS_RUN_STATE,wrapper);
        int state = CacheUtil.runStateDetect(wrapper);
        String info = null;
        switch (state){
            case 1 : info = "CPU异常";break;
            case 2 : info = "内存异常";break;
            case 3 : info = "CPU和内存异常";break;
        }
        if (info!=null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.SYS_RUN_ERROR,info);
        }
    }

    @Scheduled(fixedRate = 10000)
    @Async("d2d_schedule_service")
    //设备到设备之间的流量信息
    public void device2DevicePacketInfo(){
        if (CacheUtil.getScheduleServiceRunningState()) {
            sendDevice2DevicePackets();
        }
    }


    private final HashMap<String,Map<String, D2DWrapper>>
        D2DPacketInfoMap = new HashMap<>();

    public synchronized void sendDevice2DevicePackets() {
        D2DPacketInfoMap.clear();
        CacheUtil.getDeviceToDevicePackets().forEach((dstDevice, deviceAtomicIntegerConcurrentHashMap) -> {
            if (dstDevice.getDeviceType() != 1){
                Map<String,D2DWrapper> map = new HashMap<>();
                deviceAtomicIntegerConcurrentHashMap.forEach((srcDevice, atomicInteger) -> {
                    map.put(srcDevice.getDeviceNumber(),new D2DWrapper(atomicInteger.get(), CacheUtil.d2DAttackExist(dstDevice.getDeviceNumber(),srcDevice.getDeviceNumber())));
                });
                D2DPacketInfoMap.put(dstDevice.getDeviceNumber(),map);
            }
        });
        SocketServiceCenter.updateAllClient(SocketIoEvent.DEVICE_2_DEVICE_PACKETS, D2DPacketInfoMap);
        //推送后即清除上一次的所有被攻击设备对
        CacheUtil.clearD2DAttackPair();
    }


    /**
     * 5秒钟发送一次统计信息
     */
    //@Scheduled(fixedRate = 5000)
    private int lastReceivePacket = 0;

    private void sendPacketStatisticsMsg(){
        SEND_CONSUMER.setTimeStamp(new Date().toString());
        //将之前的统计信息置0
        CacheUtil.resetSaveBean();
        final HashMap<String,Integer> DELAY_INFO = packetAnalyzeService.getCollectorNumToDelayList();
        int receivePacket;
        int allReceivePacket = recvPacketNumber.get();
        if (lastReceivePacket == 0) {
            receivePacket = lastReceivePacket = allReceivePacket;
        }else{
            receivePacket = allReceivePacket - lastReceivePacket;
            lastReceivePacket = allReceivePacket;
        }
        DELAY_INFO.replaceAll((collectorId, allDelay) -> allDelay / receivePacket);
        NUMBER_BY_DEVICE_IN.forEach(MAX_FLOW_CONSUMER.setType(1));//download
        NUMBER_BY_DEVICE_OUT.forEach(MAX_FLOW_CONSUMER.setType(0));//upload
        ATTACK_BY_DEVICE.forEach(SEND_CONSUMER.setMap(attackByDevice , 1));
        //EXCEPTION_BY_DEVICE.forEach(SEND_CONSUMER.setMap(exceptionByDevice , 2));
        NUMBER_BY_DEVICE_IN.forEach(SEND_CONSUMER.setMap(numberByDeviceIn , 3));
        NUMBER_BY_DEVICE_OUT.forEach(SEND_CONSUMER.setMap(numberByDeviceOut , 4));
        DELAY_INFO.forEach(SEND_CONSUMER.setMap(null,5));

        SocketServiceCenter.updateAllClient(SocketIoEvent.STATISTICS_PACKET,
                new StatisticsDataWrapper.Builder()
                .setCollectorDelay(DELAY_INFO)
                .setAttackByDevice(attackByDevice)          //分设备的攻击数
                //.setExceptionByDevice(exceptionByDevice)    //分设备的异常数
                .setAttackCount(attackNumber.get())         //攻击总数
                //.setExceptionCount(exceptionNumber.get())   //异常总数
                .setCurrentPacketCount(receivePacket)
                .setNumber(allReceivePacket)                //捕获的总报文数
                .setNumberByDeviceIn(numberByDeviceIn)      //分设备的接收报文数
                .setNumberByDeviceOut(numberByDeviceOut)    //分设备的发送报文数
                .setTop5Statistic(CacheUtil.getTop5StatisticsData())
                .setDeviceCount(CacheUtil.getAllDeviceCount())
                .setAttackedDeviceCount(CacheUtil.getAttackedDeviceCount())
                .build());

        graphInfoInList.forEach(GRAPH_INFO_CONSUMER);
        SocketServiceCenter.updateAllClient(SocketIoEvent.GRAPH_INFO,StatisticsData.GRAPH_BY_DEVICE);

        //保存每个设备当前时间间隔内的统计信息到数据库服务器
        //FIXME 巨慢
        //iDeviceService.saveStatisticInfo(CommonCacheUtil.getStatisticsInfoBean());
    }

//    @Scheduled(fixedRate = 60 * 1000)
//    public void reCapturePacket(){
//        deletePcapFileScheduled();
//    }

    //定时重启tshark全部进程
    //@Scheduled(cron = "0 0 0 ? * MON-FRI")
    @Scheduled(cron = "0 0 0 * * ?")
    public void restartTsharkProcess(){
        for (BasePreProcessor basePreProcessor : CapturePacketServiceImpl.basePreProcessors) {
            Thread thread = new Thread(basePreProcessor::restartCapture);
            thread.setName(basePreProcessor.getClass().getName());
            thread.start();
        }
    }

    //定时删除临时的wireshark报文
    //@Scheduled(cron = "0 5 0 ? * MON-FRI")
    @Scheduled(cron = "30 0 0 * * ? ")
    public void deletePcapFileScheduled(){
        File file = new File(Common.WIRESHARK_TEMP_FILE);
        File[] files = file.listFiles();
        if (files!=null) {
            for (File tempFile : files) {
                if (tempFile.getName().startsWith("wireshark")) {//wireshark temp pcap file
                    File file1 = new File(tempFile.getAbsolutePath());
                    if(file1.delete()){
                        System.out.println("成功删除【wireshark】临时文件" + file1.getAbsolutePath());
                    } //如果文件正在被占用，那么会先删除失败，没关系第二天继续尝试就好了
                    else{
                        System.out.println("无法删除【wireshark】临时文件" + file1.getAbsolutePath());
                    }
                }
            }
        }
    }
    //@Scheduled(fixedRate = 1000)
//    private void sendAllFvDimensionPacket() throws InterruptedException {
//        layers.clear();
//        for (int i = 0; i < 2000; i++) {
//            FvDimensionLayer layer = fvDimensionLayers.poll(200, TimeUnit.MILLISECONDS);
//            if (layer!=null){
//                layers.add(layer);
//            }else{
//                break;
//            }
//            if (layers.size() > 0) {
//                doSendBatch(layers);
//            }
//            //doSend(fvDimensionLayers.poll());
//        }
//    }

    private void sendAllFvDimensionPacket2() throws InterruptedException {
        List<FvDimensionLayer> layers = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            FvDimensionLayer layer = fvDimensionLayers.poll(200, TimeUnit.MILLISECONDS);
            if (layer!=null){
                layers.add(layer);
            }else{
                break;
            }
        }
        if (layers.size() > 0) {
            doSendBatch(layers);
        }
    }

    private void doSendBatch(List<FvDimensionLayer> layers) {
        SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,layers);
    }

    /**
     * 工艺参数信息
     */
    //@Scheduled(fixedRate = 5000)
    private void sendGraphInfo(){
        StatisticsData.ART_INFO_SEND_SINGLE.clear();
        synchronized (StatisticsData.LINKED_LIST_LOCK){
//            for (String artName : Common.SHOW_GRAPH_SET) {
//                StatisticsData.ART_INFO_SEND.put(artName, StatisticsData.ART_INFO.get(artName));
//            }
            StatisticsData.ART_INFO.forEach((artName, artValueList) -> {
                if (CacheUtil.isArtShow(artName)){
                    //StatisticsData.ART_INFO_SEND.put(artName, artValueList);
                    if (artValueList.size() > 0)
                        ART_INFO_SEND_SINGLE.put(artName,artValueList.getLast());
                }
                if (artValueList.size() > 0 && !artName.equals("timestamp")){
                    iArtHistoryDataService.saveArtData(artName,artValueList.getLast(),null);
                }
            });
            //StatisticsData.ART_INFO_SEND.put("timestamp",StatisticsData.ART_INFO.get("timestamp"));
            LinkedList<String> timeStamp = StatisticsData.ART_INFO.get("timestamp");
            if (timeStamp.size() > 0){
                ART_INFO_SEND_SINGLE.put("timestamp",timeStamp.getLast());
            }
            SocketServiceCenter.updateAllClient(SocketIoEvent.ART_INFO, StatisticsData.ART_INFO_SEND_SINGLE);
            addArtData("timestamp", AppCommonUtil.getDateFormat().format(new Date()));
        }
    }

    private void doSend(FvDimensionLayer layer){
        if (layer!=null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,layer);
        }
    }

    /**
     * 流量信息
     */
    //@Scheduled(fixedRate = 5000)
    private void statisticFlow(){
        int flowDiff = StatisticsData.FLOW.getAndSet(0);
        if (flowDiff / 5.0 >= Common.maxFlowInByte){
            SocketServiceCenter.updateAllClient(SocketIoEvent.MAX_FLOW_ATTACK,new FlowError(new Date().toString(),flowDiff));
        }
        /*
        Set<String> devices = DEVICE_TAG_TO_NAME.values();
        Iterator<String> iterator = devices.iterator();
        if (iterator.hasNext()) {
            do {
                String deviceTag = iterator.next();
                int flowDiffByDeviceIn = FLOW_BY_DEVICE_IN.get(deviceTag).getAndSet(0);
                int flowDiffByDeviceOut = FLOW_BY_DEVICE_OUT.get(deviceTag).getAndSet(0);
                DeviceMaxFlow max = StatisticsData.getDeviceMaxFlow(deviceTag);
                //KB
                if (max != null) {
                    FlowError flowErrorOfDevice = new FlowError(new Date().toString(), flowDiff);
                    flowErrorOfDevice.setDeviceNumber(deviceTag);
                    if (((flowDiffByDeviceIn / 5) >>> 10) > max.getMaxFlowIn()) {
                        flowErrorOfDevice.setComment("下行流量超过限制");
                        SocketServiceCenter.updateAllClient(SocketIoEvent.MAX_FLOW_ATTACK, flowErrorOfDevice);
                    }
                    if (((flowDiffByDeviceOut / 5) >>> 10) > max.getMaxFlowOut()){
                        flowErrorOfDevice.setComment("上行流量超过限制");
                        SocketServiceCenter.updateAllClient(SocketIoEvent.MAX_FLOW_ATTACK, flowErrorOfDevice);
                    }
                }
            } while (iterator.hasNext());
        }
        */
    }

    private static class SenderConsumer implements BiConsumer<String, Object>{
        private HashMap<String,Integer> map;
        private int index;
        private HashMap<String,GraphInfo> graphMap;
        private String timeStamp;
        @Override
        public void accept(String deviceNumber, Object obj) {
            GraphInfo graphInfo = graphMap.get(deviceNumber);
            if (graphInfo == null) {
                graphInfo = new GraphInfo();
                graphMap.put(deviceNumber, graphInfo);
            }
            int res;
            if (obj instanceof AtomicInteger) {            //除时延之外的信息
                AtomicInteger data = ((AtomicInteger) obj);//非时延信息，需要转换减去旧数据
                res = data.getAndSet(0);
                map.put(deviceNumber , res);
            }else{
                res = ((Integer) obj);
            }
            /**
             * 设置需要保存的统计信息
             */
            StatisticInfoSaveBean bean = CacheUtil.getStatisticsInfoBean().get(deviceNumber);
            if (bean == null) {
                bean = new StatisticInfoSaveBean();
                CacheUtil.getStatisticsInfoBean().put(deviceNumber,bean);
            }
                bean.setTime(timeStamp);
                switch (index){
                    case 1://ATTACK_BY_DEVICE
                        graphInfo.setAttack(res);
                        bean.setAttack(res);
                        break;
                    case 2://EXCEPTION_BY_DEVICE
                        graphInfo.setException(res);
                        bean.setException(res);
                        break;
                    case 3://NUMBER_BY_DEVICE_IN
                        graphInfo.setPacketIn(res);
                        bean.setDownload(res);
                        break;
                    case 4://NUMBER_BY_DEVICE_OUT
                        graphInfo.setPacketOut(res);
                        bean.setUpload(res);
                        break;
                    case 5://DELAY_INFO
                        graphInfo.setDelay(res);
                        break;
                    default:log.info("*************************索引错误*************************");
                        break;
                }
        }
        SenderConsumer setMap(HashMap<String, Integer> map, int index){
            this.map = map;
            this.index = index;
            return this;
        }
        void setListMap(HashMap<String, GraphInfo> graphMap){
            this.graphMap = graphMap;
        }

        public void setTimeStamp(String timeStamp){
            this.timeStamp = timeStamp;
        }
    }

    private static class DevicePacketNumConsumer implements BiConsumer<String,AtomicInteger>{
        private int type;//0 upload 1 download
        @Override
        public void accept(String deviceNumber, AtomicInteger atomicInteger) {
            DeviceMaxFlow deviceMaxFlow = CacheUtil.getDeviceMaxFlow(deviceNumber);
            switch (type){
                case 0 : if (atomicInteger.get() > deviceMaxFlow.getMaxFlowOut())
                {
                    SocketServiceCenter.updateAllClient(SocketIoEvent.DEVICE_FLOW_ERROR,new DeviceMaxFlow(deviceNumber,deviceMaxFlow.getMaxFlowOut(),-1));
                }
                break;
                case 1 : if (atomicInteger.get() > deviceMaxFlow.getMaxFlowIn())
                {
                    SocketServiceCenter.updateAllClient(SocketIoEvent.DEVICE_FLOW_ERROR,new DeviceMaxFlow(deviceNumber,-1,deviceMaxFlow.getMaxFlowIn()));
                }
                break;
            }
        }

        public DevicePacketNumConsumer setType(int type){
            this.type = type;
            return this;
        }
    }
}
