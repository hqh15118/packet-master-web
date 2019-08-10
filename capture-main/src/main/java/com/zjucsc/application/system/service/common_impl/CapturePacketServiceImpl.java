package com.zjucsc.application.system.service.common_impl;

import com.zjucsc.application.config.*;
import com.zjucsc.application.domain.bean.ArtPacketDetail;
import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler;
import com.zjucsc.application.tshark.pre_processor.*;
import com.zjucsc.application.util.*;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.iec101.IEC101DecodeMain;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.common.AttackTypePro;
import com.zjucsc.common.common_util.CommonUtil;
import com.zjucsc.common.common_util.DBUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.kafka.KafkaThread;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.bean.CollectorState;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.handler.DefaultPipeLine;
import com.zjucsc.tshark.packets.*;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

import static com.zjucsc.application.config.Common.COMMON_THREAD_EXCEPTION_HANDLER;
import static com.zjucsc.application.config.PACKET_PROTOCOL.*;
import static com.zjucsc.socket_io.SocketIoEvent.REAL_TIME_PACKET_FILTER;

@Slf4j
@Service
public class CapturePacketServiceImpl implements CapturePacketService<String,String> {

    private final PacketAnalyzeService packetAnalyzeService;

    private SimulateThread simulateThread;

    @Autowired private ConstantConfig constantConfig;
    @Autowired private PreProcessor preProcessor;
    @Autowired private IDeviceService iDeviceService;
    //@Autowired private IArtPacketService iArtPacketService;
    public static List<BasePreProcessor> basePreProcessors = new LinkedList<>();
    //五元kafka发送线程
    private final KafkaThread.SendErrorCallback sendErrorCallback = (threadName, o) -> {
        log.error("kafka thread : [{}] msg OVERFLOW",threadName);
    };
    @SuppressWarnings("unchecked")
    private final KafkaThread<FvDimensionLayer> FV_D_SENDER = KafkaThread.createNewKafkaThread("fv_dimension", KafkaTopic.SEND_ALL_PACKET_FV_DIMENSION,sendErrorCallback);
    @SuppressWarnings("unchecked")
    private final KafkaThread<AttackBean> ATTACK_SENDER = KafkaThread.createNewKafkaThread("packet_attack", KafkaTopic.SEND_PACKET_ATTACK,sendErrorCallback);
    @SuppressWarnings("unchecked")
    private final KafkaThread<ArtPacketDetail> ART_PACKET = KafkaThread.createNewKafkaThread("art_packet",KafkaTopic.ART_PACKET,sendErrorCallback);

    public CapturePacketServiceImpl(PacketAnalyzeService packetAnalyzeService) {
        this.packetAnalyzeService = packetAnalyzeService;
        //所有攻击报文的入口
        AttackCommon.registerAttackCallback((attackBean,layer) -> {
            //设置攻击设备和被攻击设备
            setDeviceInfo(attackBean);
            //发送攻击信息保存到数据库
            ATTACK_SENDER.sendMsg(attackBean);
            //统计攻击TOP5
            CacheUtil.addNewAttackBean(attackBean);
            //恶意报文统计【五秒钟一次的延迟推送】
            statisticsBadPacket(attackBean.getDeviceNumber());
            processAttackInfo(attackBean,layer);
        });

        ArtDecodeCommon.registerPacketValidCallback((argName, value, layer, objs) -> {
            if (objs.length == 0) {
                ArtPacketDetail artPacketDetail = ArtPacketDetail.newOne(layer);
                artPacketDetail.setValue(value);
                artPacketDetail.setArtName(argName);
                ART_PACKET.sendMsg(artPacketDetail);
                //iArtPacketService.insertArtPacket(argName,artPacketDetail);
                Map<String, Float> res = AppCommonUtil.getGlobalArtMap();
                AttackCommon.appendArtAnalyze(res, layer);
            }else{
                String event = (String) objs[0];
                switch (event){
                    case "opcda":
                        //TODO SAVE TO DB
                        break;
                }
            }
        });
    }

    private void processAttackInfo(AttackBean attackBean, FvDimensionLayer layer) {
        if (attackBean.getAttackType().equals(AttackTypePro.ART_EXCEPTION)){
            SocketServiceCenter.updateAllClient(SocketIoEvent.ATTACK_INFO, attackBean);
        }else {
            //通知攻击到达
            SocketServiceCenter.updateAllClient(SocketIoEvent.ATTACK_INFO, attackBean);
        }
    }

    private void statisticsBadPacket(String deviceNumber){
        StatisticsData.attackNumber.incrementAndGet();
        StatisticsData.increaseAttackByDevice(deviceNumber);
    }

    private void setDeviceInfo(AttackBean attackBean){
        String srcDeviceNumber = CacheUtil.getTargetDeviceNumberByTag(attackBean.getSrcIp(),attackBean.getSrcMac());
        if (srcDeviceNumber!=null){
            String deviceName = CacheUtil.convertDeviceNumberToName(srcDeviceNumber);
            if (deviceName!=null) {
                attackBean.setSrcDevice(deviceName);
            }else{
                setUnknownAttackDevice(attackBean,1);
            }
        }else{
            setUnknownAttackDevice(attackBean,1);
        }
        String dstDeviceNumber = CacheUtil.getTargetDeviceNumberByTag(attackBean.getDstIp(),attackBean.getDstMac());
        if (dstDeviceNumber!=null){
            String deviceName = CacheUtil.convertDeviceNumberToName(dstDeviceNumber);
            if (deviceName!=null) {
                attackBean.setDstDevice(deviceName);
            }else {
                setUnknownAttackDevice(attackBean,0);
            }
        }else{
            setUnknownAttackDevice(attackBean,0);
        }
        //设置被攻击的对
        if (srcDeviceNumber!=null && dstDeviceNumber!=null){
            CacheUtil.addD2DAttackPair(dstDeviceNumber,srcDeviceNumber);
        }
    }

    private void setUnknownAttackDevice(AttackBean attackBean, int i) {
        if (i == 0){
            if (!attackBean.getDstIp().equals("--")){
                attackBean.setDstDevice(attackBean.getDstIp());
            }else{
                attackBean.setDstDevice(attackBean.getDstMac());
            }
        }else{
            if (!attackBean.getSrcIp().equals("--")){
                attackBean.setSrcDevice(attackBean.getSrcIp());
            }else{
                attackBean.setSrcDevice(attackBean.getSrcMac());
            }
        }
    }

    private void startAllKafkaThread(){
        FV_D_SENDER.startService();
        ATTACK_SENDER.startService();
        ART_PACKET.startService();
    }
    private void stopAllKafkaThread(){
        FV_D_SENDER.stopService();
        ATTACK_SENDER.stopService();
        ART_PACKET.stopService();
    }

    private NewFvDimensionCallback newFvDimensionCallback;

    private ProcessCallback<String,String> callback;

    //恶意报文handler
    private BadPacketAnalyzeHandler badPacketAnalyzeHandler = new BadPacketAnalyzeHandler(Executors.newFixedThreadPool(1,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("bad_packet_analyze_handler-");
                thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                return thread;
            }));

    /**
     * 接收协议解析好的五元组，并作五元组发送、报文流量统计处理
     * 单线程处理，保证线程安全
     */
    private AbstractAsyncHandler<FvDimensionLayer> fvDimensionLayerAbstractAsyncHandler
            = new AbstractAsyncHandler<FvDimensionLayer>(CommonUtil.getSingleThreadPoolSizeThreadPool(100000,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-fv-dimension-entry-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    log.error("-fv-dimension-entry-捕获异常=====>",e);
                });
                return thread;
            },"-fv-dimension-thread-pool-")) {
        /**
         * 所有报文的入口方法
         * @param t 五元组
         * @return 返回的是设置了功能码的五元组，见setFuncode方法
         */
        @Override
        public FvDimensionLayer handle(Object t) {
            FvDimensionLayer fvDimensionLayer = ((FvDimensionLayer) t);
            //设置协议栈
            fvDimensionLayer.protocol = PacketDecodeUtil.discernPacket(fvDimensionLayer);   //t-s
            //解析原始数据
            byte[] payload = PacketDecodeUtil.hexStringToByteArray2(fvDimensionLayer.custom_ext_raw_data[0]);    //t-s
            fvDimensionLayer.rawData = payload;
            fvDimensionLayer.tcpPayload = PacketDecodeUtil.hexStringToByteArray(fvDimensionLayer.tcp_payload[0]);
            preProcess(fvDimensionLayer);

            fvDimensionLayer.deviceNumber = CacheUtil.getTargetDeviceNumberByTag(fvDimensionLayer.ip_dst[0],fvDimensionLayer.eth_dst[0]);
            //统计所有的IP地址
            if (fvDimensionLayer.ip_dst[0].length() > 0){
                StatisticsData.statisticAllIpAddress(fvDimensionLayer.ip_dst[0]);           //t-s
            }
            //统计协议
            //协议比例
            StatisticsData.addProtocolNum(fvDimensionLayer.protocol,1);         //t-s
            //设置五元组中的功能码以及功能码对应的含义
            if (Common.systemRunType !=0 ) {
                try {
                    setFuncode(fvDimensionLayer);                                           //t-s
                } catch (ProtocolIdNotValidException e) {
                    //缓存中找不到该五元组对应的协议
                    log.error("error set Funcode , msg : {} [缓存中找不到该五元组协议：{} 对应的功能码表]", e.getMsg(), fvDimensionLayer.frame_protocols[0]);
                }
            }
            sendFvDimensionPacket(fvDimensionLayer , payload);                    //发送五元组所有报文到前端
            sendPacketStatisticsEvent(fvDimensionLayer);                          //发送统计信息
            int collectorId = PacketDecodeUtil.decodeCollectorId(payload,24);
            if (collectorId > 0){
                analyzeCollectorState(payload , collectorId);                         //分析采集器状态信息
                fvDimensionLayer.delay = collectorDelayInfo(fvDimensionLayer,payload,collectorId);     //解析时延信息
                fvDimensionLayer.collectorId = collectorId;                           //设置报文采集器ID
            }
            else
                {
//                log.error("error decode collector id Id : [{}] , rawData : [{}] , protocol:[{}]",collectorId,
//                        fvDimensionLayer.custom_ext_raw_data[0],fvDimensionLayer.protocol);
            }
            FV_D_SENDER.sendMsg(fvDimensionLayer);                                //发送消息到数据库服务器
            return fvDimensionLayer;                                              //将五元组发送给BadPacketHandler
        }
    };

    private void preProcess(FvDimensionLayer fvDimensionLayer) {
        if (fvDimensionLayer.protocol.equals("tcp") && !fvDimensionLayer.tcp_payload[0].equals("")){
            byte[] tcpPayload = PacketDecodeUtil.hexStringToByteArray(fvDimensionLayer.tcp_payload[0]);
            //set iec101 protocol 单字节的101怎么设置
            byte startByte = tcpPayload[0];
            if (startByte == 0x68 || startByte == 0x10){
                fvDimensionLayer.protocol = "iec101";
            }
            //set dnp3.0 protocol
            return;
        }
        if (fvDimensionLayer.rawData.length >= 16 && Byte.toUnsignedInt(fvDimensionLayer.rawData[13]) == 0x89
            && fvDimensionLayer.rawData[14] == 0x07 && fvDimensionLayer.rawData[15] == 0x12 && fvDimensionLayer.rawData[16] == 0x34){
            fvDimensionLayer.protocol = "can";
        }
    }

    public void setFuncode(FvDimensionLayer layer) throws ProtocolIdNotValidException {
        if (!(layer instanceof UndefinedPacket.LayersBean)) {
            String funCode = decodeFuncodeAndSetFuncodeMeaning(layer);
            if (funCode != null) {
                layer.funCode = funCode;
            }
            else{
                layer.funCode = "--";
            }
        }
    }

    /**
     * 解析功能码，一些协议有些报文有功能码有些报文没有功能码，要做区分
     * 没有功能码返回-1
     * IEC104有一个特殊的-2功能码
     * @param t 五元组
     * @return 功能码
     */
    private String decodeFuncodeAndSetFuncodeMeaning(FvDimensionLayer t) throws ProtocolIdNotValidException {
        String funCodeStr;
        String funCode = null;
        if (t instanceof S7CommPacket.LayersBean){
            S7CommPacket.LayersBean s7Packet = ((S7CommPacket.LayersBean) t);
            if (s7Packet.s7comm_param_func!=null) {
                //decode s7comm funcode
                funCodeStr = s7Packet.s7comm_param_func[0];
                if (funCodeStr.equals("")){
                    funCodeStr = s7Packet.s7comm_param_userdata_funcgroup[0];
                }
                funCode =  PacketDecodeUtil.decodeFuncode("s7comm", funCodeStr);
                //setFuncodeMeaning of s7comm
                if (s7Packet.s7comm_header_rosctr[0].equals(S7CommPacket.USER_DATA)) {
                    //user data
                    t.funCodeMeaning = CommonConfigUtil.
                            getTargetProtocolFuncodeMeaning(S7_User_data, funCode);
                } else {
                    //other [job/ack_data]
                    t.funCodeMeaning = CommonConfigUtil.
                            getTargetProtocolFuncodeMeaning(S7, funCode);
                }
            }
        }else if (t instanceof ModbusPacket.LayersBean){
            if (((ModbusPacket.LayersBean) t).modbus_func_code!=null) {
                funCodeStr = ((ModbusPacket.LayersBean) t).modbus_func_code[0];
                funCode =  PacketDecodeUtil.decodeFuncode("modbus", funCodeStr);
                t.funCodeMeaning = CommonConfigUtil.
                        getTargetProtocolFuncodeMeaning(MODBUS, funCode);
            }
        }else if (t instanceof IEC104Packet.LayersBean){
            IEC104Packet.LayersBean iec104_packet = ((IEC104Packet.LayersBean) t);
                int type = Integer.decode(iec104_packet.iec104_type[0]);
                switch (type){
                    case 0 :
                        //decode iec funcode
                        funCodeStr = iec104_packet.iec104asdu_typeid[0];
                        funCode =  PacketDecodeUtil.decodeFuncode("iec104", funCodeStr);
                        //decode funcode meaning by funcode
                        t.funCodeMeaning = CommonConfigUtil.
                                getTargetProtocolFuncodeMeaning(PACKET_PROTOCOL.IEC104_ASDU, funCode);
                        break;
                    case 1 :
                        t.funCodeMeaning = "确认数据接收";
                        break;
                    case 3 :
                        //decode iec funcode
                        funCodeStr = iec104_packet.iec104apci_utype[0];
                        funCode =  PacketDecodeUtil.decodeFuncode("iec104", funCodeStr);
                        //decode funcode meaning by funcode
                        t.funCodeMeaning = CommonConfigUtil.
                                getTargetProtocolFuncodeMeaning(PACKET_PROTOCOL.IEC104_APCI, funCode);
                        break;
                }
        }else if (t instanceof Dnp3_0Packet.LayersBean){
            Dnp3_0Packet.LayersBean dnpPacket = ((Dnp3_0Packet.LayersBean) t);
            int type = Integer.decode(dnpPacket.dnp3_ctl_prm[0]);
            switch (type){
                case 1:
                    //decode dnp funcode
                    funCodeStr = dnpPacket.dnp3_ctl_prifunc[0];
                    funCode = PacketDecodeUtil.decodeFuncode("dnp3.0", funCodeStr);
                    //decode funcode meaning by funcode
                    t.funCodeMeaning = CommonConfigUtil.
                            getTargetProtocolFuncodeMeaning(PACKET_PROTOCOL.DNP3_0_PRI, funCode);
                    break;
                case 0:
                    funCodeStr = dnpPacket.dnp3_ctl_secfunc[0];
                    funCode = PacketDecodeUtil.decodeFuncode("dnp3.0", funCodeStr);
                    t.funCodeMeaning = CommonConfigUtil.
                            getTargetProtocolFuncodeMeaning(PACKET_PROTOCOL.DNP3_0_SET, funCode);
                    break;
            }
        }else if (t instanceof CipPacket.LayersBean){
            CipPacket.LayersBean cipPacket = ((CipPacket.LayersBean) t);
            funCode = cipPacket.cip_funcode[0];
            t.funCodeMeaning = CommonConfigUtil.
                    getTargetProtocolFuncodeMeaning(PACKET_PROTOCOL.CIP_IP, funCode);
        }else if (t instanceof MmsPacket.LayersBean){
            MmsPacket.LayersBean mmsPacket = ((MmsPacket.LayersBean) t);
            funCode = mmsPacket.mmsFuncode[0];
            t.funCodeMeaning = CommonConfigUtil.
                    getTargetProtocolFuncodeMeaning(PACKET_PROTOCOL.MMS, funCode);
        }else if (t instanceof UndefinedPacket.LayersBean){
            UndefinedPacket.LayersBean unknownPacket = ((UndefinedPacket.LayersBean) t);
            if (unknownPacket.protocol.equals("iec101")){
                String funCodeMeaningStr = IEC101DecodeMain.decode101Funcode(unknownPacket.tcpPayload);
                if (funCodeMeaningStr!=null){
                    t.funCodeMeaning = funCodeMeaningStr;
                }
            }
        }
        return funCode;
    }

    @Async("common_async")
    @Override
    public CompletableFuture<Exception> start(ProcessCallback<String,String> callback) {
        startAllKafkaThread();
        this.callback = callback;
        basePreProcessors = new LinkedList<>();
        boolean cipMms = false;
        boolean iec104Dnp = false;
        boolean opcuaDa = false;
        for (String preProcessorName : preProcessor.getList()) {
            switch (preProcessorName){
                case "s7comm" :
                    basePreProcessors.add(new S7CommPreProcessor());
                    break;
                case "modbus" :
                    basePreProcessors.add(new ModbusPreProcessor());
                    break;
                case "iec104" :
                case "dnp3_0" :
                    if (!iec104Dnp){
                        basePreProcessors.add(new IEC104DnpPreProcessor());
                        iec104Dnp = true;
                    }
                    break;
                case "pnio" :
                    basePreProcessors.add(new PnioPreProcessor());
                    break;
                case "cip" :
                case "mms":
                    if (!cipMms){
                        basePreProcessors.add(new CipMmsPreProcessor());
                        cipMms = true;
                    }
                    break;
                case "opcua" :
                case "dcerpc" :
                    if (!opcuaDa){
                        basePreProcessors.add(new OpcUaDaPreProcessor());
                        opcuaDa = true;
                    }
                    break;
            }
        }
        basePreProcessors.add(new UndefinedPreProcessor());
        try {
            callback.start(doStart(fvDimensionLayerAbstractAsyncHandler,
                                    basePreProcessors
                                   ));
        } catch (InterruptedException e) {
            return CompletableFuture.completedFuture(e);
        }
        return CompletableFuture.completedFuture(null);
    }

    private int collectorDelayInfo(FvDimensionLayer layer , byte[] payload , int collectorId) {
        if (payload.length > 0){
            if (collectorId > 0){
                //valid packet
                int collectorDelay = PacketDecodeUtil.decodeCollectorDelay(payload,4);
                //设置ID和延时用于发送
                //System.out.println("delay : " + collectorDelay);
                if (collectorDelay > 0) {
                    packetAnalyzeService.setCollectorDelay(collectorId, collectorDelay);
                }else{
                    //log.error("error decode collector : [{}] DELAY , raw data : {}" , collectorId,layer.custom_ext_raw_data[0]);
                }
                return collectorDelay;
            }
            return -1;
        }
        return -1;
    }

    @Async("common_async")
    @Override
    public CompletableFuture<Exception> stop() {
        for (BasePreProcessor basePreProcessor : basePreProcessors) {
            basePreProcessor.stopProcess();
            callback.end("end " + basePreProcessor.getClass().getName());
        }
        basePreProcessors.clear();
        stopAllKafkaThread();
        return CompletableFuture.completedFuture(null);
    }

    private String doStart(AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler ,
                           List<BasePreProcessor> packetPreProcessor) throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(packetPreProcessor.size() - 1);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < packetPreProcessor.size() - 1; i++) {
            BasePreProcessor basePreProcessor = packetPreProcessor.get(i);
            doNow(basePreProcessor , fvDimensionHandler , downLatch , sb);
        }
        downLatch.await(100, TimeUnit.SECONDS);
        doNow(packetPreProcessor.get(i) , fvDimensionHandler,null , sb);
        return sb.toString();
    }

    private void doNow(BasePreProcessor basePreProcessor , AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler,
                       CountDownLatch downLatch , StringBuilder sb){
        String processName = basePreProcessor.getClass().getName();
        DefaultPipeLine pipeLine = new DefaultPipeLine(processName);
        //fv_dimension_handler --> bad_packet_analyze_handler
        pipeLine.addLast(fvDimensionHandler);
        pipeLine.addLast(deviceHandler);
        pipeLine.addLast(attackAnalyzeHandler);
        pipeLine.addLast(packetDetectHandler);
        pipeLine.addLast(badPacketAnalyzeHandler);
        basePreProcessor.setPipeLine(pipeLine);
        Thread processThread = new Thread(() -> {
            basePreProcessor.setCommandBuildFinishCallback(()->{
                if (downLatch!=null) {
                    downLatch.countDown();
                }
            });
            basePreProcessor.execCommand(1 , -1);
        });
        processThread.setName(processName + "-thread");
        processThread.start();
        sb.append(basePreProcessor.getClass().getName()).append(" start");
    }

    private void sendFvDimensionPacket(FvDimensionLayer fvDimensionLayer , byte[] payload){
        fvDimensionLayer.timeStamp = PacketDecodeUtil.decodeTimeStamp(payload,20,fvDimensionLayer);
        //SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,fvDimensionLayer);
        if (newFvDimensionCallback!=null){
            //控制报文发送数量
            newFvDimensionCallback.newCome(fvDimensionLayer);               //not t-s
        }
    }

    private void sendPacketStatisticsEvent(FvDimensionLayer fvDimensionLayer) {
        int capLength = Integer.parseInt(fvDimensionLayer.frame_cap_len[0]);

        StatisticsData.FLOW.addAndGet(capLength);                          //计算报文总流量

        StatisticsData.recvPacketNumber.addAndGet(1);                //总报文数

        String deviceNumber = fvDimensionLayer.deviceNumber;
        //外界 --> PLC[ip_dst]  设备接收的报文数
        StatisticsData.increaseNumberByDeviceIn(deviceNumber,1);
        //外界 <-- PLC[ip_src]  设备发送的报文数
        StatisticsData.increaseNumberByDeviceOut(deviceNumber, 1);
        //外界 --> PLC[ip_dst]  设备接收的报文流量
        StatisticsData.increaseFlowByDeviceIn(deviceNumber,capLength);
        //外界 --> PLC[ip_dst]  设备发送的报文流量
        StatisticsData.increaseFlowByDeviceOut(deviceNumber,capLength);
    }

    private void analyzeCollectorState(byte[] payload , int collectorId){
        CollectorState collectorState = PacketDecodeUtil.decodeCollectorState(payload,24,collectorId);
        if (collectorState!=null){
            //log.info("**********************\n collector state change : {} \n **********************" , collectorState);
            SocketServiceCenter.updateAllClient(SocketIoEvent.COLLECTOR_STATE,collectorState);
        }
        //System.out.println(collectorId + " -- " + collectorState);
    }

    public void setNewFvDimensionCallback(NewFvDimensionCallback newFvDimensionCallback){
        this.newFvDimensionCallback = newFvDimensionCallback;
    }

    @Async("common_async")
    @Override
    public CompletableFuture<Exception> startSimulate() {
        startAllKafkaThread();
        if (simulateThread == null){
            simulateThread = new SimulateThread();
        }
        if (!simulateThread.hasStart){
            DefaultPipeLine pipeLine = new DefaultPipeLine("simulate");
            pipeLine.addLast(fvDimensionLayerAbstractAsyncHandler);
            pipeLine.addLast(deviceHandler);
            pipeLine.addLast(attackAnalyzeHandler);
            pipeLine.addLast(packetDetectHandler);
            pipeLine.addLast(badPacketAnalyzeHandler);
            simulateThread.pipeLine = pipeLine;
            simulateThread.start();
        }else{
            simulateThread.notifyNow();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("common_async")
    @Override
    public CompletableFuture<Exception> stopSimulate() {
        stopAllKafkaThread();
        simulateThread.idleNow();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Map<String, Integer> load() {
        Map<String,Integer> loadMap = new HashMap<>();
        loadMap.put(fvDimensionLayerAbstractAsyncHandler.getClass().getName(), ((ThreadPoolExecutor) fvDimensionLayerAbstractAsyncHandler.getExecutor()).getQueue().size());
        loadMap.put(deviceHandler.getClass().getName(), ((ThreadPoolExecutor) deviceHandler.getExecutor()).getQueue().size());
        loadMap.put(attackAnalyzeHandler.getClass().getName(), ((ThreadPoolExecutor) attackAnalyzeHandler.getExecutor()).getQueue().size());
        loadMap.put(packetDetectHandler.getClass().getName(), ((ThreadPoolExecutor) packetDetectHandler.getExecutor()).getQueue().size());
        loadMap.put(badPacketAnalyzeHandler.getClass().getName(), ((ThreadPoolExecutor) badPacketAnalyzeHandler.getExecutor()).getQueue().size());
        return loadMap;
    }

    private class SimulateThread extends Thread{
        private DefaultPipeLine pipeLine;
        private boolean hasStart = false;
        private volatile boolean idle = false;
        private final byte[] IDLE_LOCK = new byte[1];
        private int hasSendFvDimensionNumber = 0;
        private PreparedStatement preparedStatement;
        private Connection connection;
        private List<FvDimensionLayer> fvDimensionLayers = new LinkedList<>();
        private volatile boolean running = true;

        {
            try {
                String PASSWORD = "920614";
                String USER_NAME = "root";
                String JDBC_URL = "jdbc:mysql://10.15.191.100:3306/csc_db?serverTimezone=UTC";
                connection = DBUtil.getConnection(JDBC_URL, USER_NAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert connection!=null;
        }

        private List<FvDimensionLayer> getPreparedStatement(int num) throws SQLException {
            fvDimensionLayers.clear();
            if (preparedStatement == null) {
                int date;
                if (constantConfig.getReOpenTableName() > 0) {
                    date = constantConfig.getReOpenTableName();
                }else {
                    date = 20190625;
                }
                String baseCurrentTableName = "packet_info_";
                String tableName = baseCurrentTableName + date;
                preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s WHERE id > %s and id < %s",
                        tableName, "?", "?"));
                hasSendFvDimensionNumber += num;
            }
            preparedStatement.setInt(1,hasSendFvDimensionNumber);
            preparedStatement.setInt(2,hasSendFvDimensionNumber + num);
            hasSendFvDimensionNumber+=num;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
                fvDimensionLayer.timeStamp = resultSet.getString("time_stamp");
                fvDimensionLayer.frame_protocols[0] = resultSet.getString("protocol_name");
                fvDimensionLayer.eth_src[0] = resultSet.getString("src_mac");
                fvDimensionLayer.eth_dst[0] = resultSet.getString("dst_mac");
                fvDimensionLayer.ip_src[0] = resultSet.getString("src_ip");
                fvDimensionLayer.ip_dst[0] = resultSet.getString("dst_ip");
                fvDimensionLayer.src_port[0] = resultSet.getString("src_port");
                fvDimensionLayer.dst_port[0] = resultSet.getString("dst_port");
                fvDimensionLayer.funCode = resultSet.getString("fun_code");
                fvDimensionLayer.frame_cap_len[0] = resultSet.getString("length");
                fvDimensionLayer.funCodeMeaning = resultSet.getString("fun_code_meaning");
                fvDimensionLayer.tcp_payload[0] = resultSet.getString("tcp_payload");
                fvDimensionLayer.custom_ext_raw_data[0] = resultSet.getString("raw_data");
                fvDimensionLayers.add(fvDimensionLayer);
            }
            return fvDimensionLayers;
        }

        @Override
        public void run() {
            hasStart = true;
            Random random = new Random();
            for (;running;){
                if (idle){
                    synchronized (IDLE_LOCK){
                        try {
                            IDLE_LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        idle = false;
                    }
                }
                //200 - 500
                int num = (int)(30 * random.nextDouble()) + 50;
                try {
                    List<FvDimensionLayer> layers = getPreparedStatement(num);
                    for (FvDimensionLayer layer : layers) {
                        pipeLine.pushDataAtHead(layer);
                    }
                    Thread.sleep(1000);
                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void idleNow(){
            idle = true;
        }

        private void notifyNow(){
            synchronized (IDLE_LOCK){
                IDLE_LOCK.notifyAll();
            }
        }
    }
    private final AbstractAsyncHandler<FvDimensionLayer> deviceHandler =
            new AbstractAsyncHandler<FvDimensionLayer>(CommonUtil.getSingleThreadPoolSizeThreadPool(100000,
                    r -> {
                        Thread thread = new Thread(r);
                        thread.setUncaughtExceptionHandler((t, e) -> log.error("-device-analyze-捕获异常=====>\n" , e));
                        thread.setName("-device-analyze-");
                        return thread;
                    },"-device-analyze-thread-pool-")) {
                @Override
                public FvDimensionLayer handle(Object t) {
                    //新设备统计
                    FvDimensionLayer layer = ((FvDimensionLayer) t);
                    Device device = CacheUtil.autoAddDevice(layer);      //检测到新设备，推送新设备信息
                    if (device!=null){
                        //come new device
                        //save device async
                        if (device.getDeviceType() > 0) {
                            SocketServiceCenter.updateAllClient(SocketIoEvent.NEW_DEVICE,device);
                            iDeviceService.saveOrUpdateDevice(device);
                        }
                    }
                    //设备流量统计
                    CacheUtil.addTargetDevicePacket(layer);
                    return layer;
                }
            };

    private final AbstractAsyncHandler<FvDimensionLayer> attackAnalyzeHandler = new AbstractAsyncHandler<FvDimensionLayer>
            (CommonUtil.getSingleThreadPoolSizeThreadPool(100000, r -> {
                Thread thread = new Thread(r);
                thread.setName("-attack-analyze-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    log.error("-attack-analyze-捕获异常=====>",e);
                });
                return thread;
            },"-attack-analyze-thread-pool-")) {
        @Override
        public FvDimensionLayer handle(Object t) {
            FvDimensionLayer layer = ((FvDimensionLayer) t);
            //工艺参数分析
            byte[] tcpPayload = layer.tcpPayload;
            Map<String,Float> res = AppCommonUtil.getGlobalArtMap();
            String protocol = layer.protocol;
            if (!(layer instanceof UndefinedPacket.LayersBean)){
                res = artAnalyze(protocol,tcpPayload,layer,res);
                //分析结果
                //数据发送
                StatisticsData.addArtMapData(res);
            }
            AttackCommon.appendDOSAnalyze(layer, DeviceOptUtil.getDstDeviceTag(layer));                     //将五元组添加到攻击分析模块中分析
            try {
                AttackCommon.appendOptAnalyze(res, layer, CacheUtil.convertNameToId(layer.protocol));
            } catch (ProtocolIdNotValidException ignored) {
            }
            return layer;
        }
    };

    private Map<String, Float> artAnalyze(String protocol, byte[] tcpPayload, FvDimensionLayer layer, Map<String, Float> res) {
        switch (protocol){
            case "s7comm":
                if (!layer.tcp_flags_ack[0].equals("") || Common.systemRunType == 0){
                   ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,"s7comm",layer,1);
                }else{
                    ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),layer.rawData,"s7comm",layer,0);
                }
                break;
            case PACKET_PROTOCOL.MODBUS :
                ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,layer.protocol,layer);
                break;
            case PACKET_PROTOCOL.PN_IO :
                ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),layer.rawData,layer.protocol,layer);
                break;
            case PACKET_PROTOCOL.IEC104_ASDU :
                ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),layer.rawData,layer.protocol,layer);
                break;
            case PACKET_PROTOCOL.OPC_UA :
                ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,layer.protocol,layer);
                break;
            case "dnp3" :
                ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,layer.protocol,layer);
                break;
            case PACKET_PROTOCOL.MMS :
                ArtDecodeCommon.artDecodeEntry(AppCommonUtil.getGlobalArtMap(),tcpPayload,layer.protocol,layer);
                break;
        }
        return res;
    }

    private final AbstractAsyncHandler<FvDimensionLayer> packetDetectHandler =
            new AbstractAsyncHandler<FvDimensionLayer>(CommonUtil.getSingleThreadPoolSizeThreadPool(100000, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setUncaughtExceptionHandler((t, e) -> System.err.println("-packet-detect-捕获异常=====>\n" + e));
                    thread.setName("-packet-detect-");
                    return thread;
                }
            },"-packet-detect-thread-pool-"))
            {
                @Override
                public FvDimensionLayer handle(Object t) {
                    FvDimensionLayer layer = ((FvDimensionLayer) t);
                    if (CacheUtil.realTimeFvDimensionFilter(layer)){
                        SocketServiceCenter.updateAllClient(REAL_TIME_PACKET_FILTER,layer);
                    }
                    return layer;
                }
            };


}
