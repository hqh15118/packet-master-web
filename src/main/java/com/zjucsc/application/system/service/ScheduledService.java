package com.zjucsc.application.system.service;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.FlowError;
import com.zjucsc.application.domain.bean.GraphInfo;
import com.zjucsc.application.domain.bean.StatisticInfoSaveBean;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.system.service.hessian_iservice.IArtHistoryDataService;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static com.zjucsc.application.config.StatisticsData.*;

@Slf4j
@Service
public class ScheduledService {

    @Autowired public PacketAnalyzeService packetAnalyzeService;

    @Autowired private IDeviceService iDeviceService;

    @Autowired private IArtHistoryDataService iArtHistoryDataService;

    private LinkedBlockingQueue<FvDimensionLayer> fvDimensionLayers = new LinkedBlockingQueue<>(5);

    @Autowired
    public ScheduledService(CapturePacketService capturePacketService ){
        capturePacketService.setNewFvDimensionCallback(layer -> {
            fvDimensionLayers.offer(layer);
        });
        SEND_CONSUMER.setListMap(graphInfoInList);
    }

    private final HashMap<String,Integer> attackByDevice = new HashMap<>(10);
    private final HashMap<String,Integer> exceptionByDevice = new HashMap<>(10);
    private final HashMap<String,Integer> numberByDeviceIn = new HashMap<>(10);
    private final HashMap<String,Integer> numberByDeviceOut = new HashMap<>(10);
    private final HashMap<String,GraphInfo> graphInfoInList = new HashMap<>(10);
    private final SenderConsumer SEND_CONSUMER = new SenderConsumer();
    private final BiConsumer<String,GraphInfo> GRAPH_INFO_CONSUMER =
            StatisticsData::addDeviceGraphInfo;

    private int count = 0;

    @Scheduled(fixedRate = 1000)
    public void commonScheduleService(){
        if (Common.SCHEDULE_RUNNING){
            count ++;
            if (count >= 5){
                sendPacketStatisticsMsg();
                sendGraphInfo();
                statisticFlow();
                saveArtDataToDb();
                count = 0;
            }
            sendAllFvDimensionPacket();
        }
    }
    /**
     * 5秒钟发送一次统计信息
     */
    //@Scheduled(fixedRate = 5000)
    private void sendPacketStatisticsMsg(){
        SEND_CONSUMER.setTimeStamp(new Date().toString());
        //将之前的统计信息置0
        CommonCacheUtil.resetSaveBean();

        final HashMap<String,Integer> DELAY_INFO = packetAnalyzeService.getCollectorNumToDelayList();
        ATTACK_BY_DEVICE.forEach(SEND_CONSUMER.setMap(attackByDevice , 1));
        EXCEPTION_BY_DEVICE.forEach(SEND_CONSUMER.setMap(exceptionByDevice , 2));
        NUMBER_BY_DEVICE_IN.forEach(SEND_CONSUMER.setMap(numberByDeviceIn , 3));
        NUMBER_BY_DEVICE_OUT.forEach(SEND_CONSUMER.setMap(numberByDeviceOut , 4));
        DELAY_INFO.forEach(SEND_CONSUMER.setMap(null,5));

        SocketServiceCenter.updateAllClient(SocketIoEvent.STATISTICS_PACKET,
                new StatisticsDataWrapper.Builder()
                .setCollectorDelay(DELAY_INFO)
                .setAttackByDevice(attackByDevice)          //分设备的攻击数
                .setExceptionByDevice(exceptionByDevice)    //分设备的异常数
                .setAttackCount(attackNumber.get())         //攻击总数
                .setExceptionCount(exceptionNumber.get())   //异常总数
                .setNumber(recvPacketNumber.get())          //捕获的总报文数
                .setNumberByDeviceIn(numberByDeviceIn)      //分设备的接收报文数
                .setNumberByDeviceOut(numberByDeviceOut)    //分设备的发送报文数
                .build()
                );

        graphInfoInList.forEach(GRAPH_INFO_CONSUMER);
        SocketServiceCenter.updateAllClient(SocketIoEvent.GRAPH_INFO,StatisticsData.GRAPH_BY_DEVICE);

        //保存每个设备当前时间间隔内的统计信息到数据库服务器
        iDeviceService.saveStatisticInfo(Common.STATISTICS_INFO_BEAN);
    }

    /**
     * 存储工艺参数信息
     */
    private void saveArtDataToDb() {

    }

    //@Scheduled(fixedRate = 1000)
    private void sendAllFvDimensionPacket(){
        for (int i = 0; i < 5; i++) {
            doSend(fvDimensionLayers.poll());
        }
    }
    /**
     * 工艺参数信息
     */
    //@Scheduled(fixedRate = 5000)
    private void sendGraphInfo(){
        StatisticsData.ART_INFO_SEND.clear();
        synchronized (StatisticsData.LINKED_LIST_LOCK){
//            for (String artName : Common.SHOW_GRAPH_SET) {
//                StatisticsData.ART_INFO_SEND.put(artName, StatisticsData.ART_INFO.get(artName));
//            }
            StatisticsData.ART_INFO.forEach((artName, artValueList) -> {
                if (Common.SHOW_GRAPH_SET.contains(artName)){
                    StatisticsData.ART_INFO_SEND.put(artName, artValueList);
                    iArtHistoryDataService.saveArtData(artName,artValueList.getLast(),null);
                }
            });
            StatisticsData.ART_INFO_SEND.put("timestamp",StatisticsData.ART_INFO.get("timestamp"));
            SocketServiceCenter.updateAllClient(SocketIoEvent.ART_INFO, StatisticsData.ART_INFO_SEND);
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
        int flowDiff = Common.FLOW.getAndSet(0);
        if (flowDiff / 5 >= Common.maxFlowInByte){
            SocketServiceCenter.updateAllClient(SocketIoEvent.MAX_FLOW_ATTACK,new FlowError(new Date().toString(),flowDiff));
        }
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
            StatisticInfoSaveBean bean = Common.STATISTICS_INFO_BEAN.get(deviceNumber);
            if (bean == null) {
                bean = new StatisticInfoSaveBean();
                Common.STATISTICS_INFO_BEAN.put(deviceNumber,bean);
                log.info("设备{}未添加StatisticInfoSaveBean，已重新添加，但程序中有错误...",deviceNumber);
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
}
