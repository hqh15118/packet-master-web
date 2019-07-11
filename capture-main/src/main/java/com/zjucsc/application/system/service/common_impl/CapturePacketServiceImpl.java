package com.zjucsc.application.system.service.common_impl;

import com.zjucsc.application.config.*;
import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler;
import com.zjucsc.application.tshark.pre_processor.*;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.AttackCommon;
import com.zjucsc.common.common_util.ByteUtil;
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
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.zjucsc.application.config.Common.COMMON_THREAD_EXCEPTION_HANDLER;
import static com.zjucsc.application.config.PACKET_PROTOCOL.*;

@Slf4j
@Service
public class CapturePacketServiceImpl implements CapturePacketService<String,String> {

    private final PacketAnalyzeService packetAnalyzeService;

    private SimulateThread simulateThread;

    @Autowired private ConstantConfig constantConfig;
    @Autowired private PreProcessor preProcessor;
    @Autowired private IDeviceService iDeviceService;

    //五元kafka发送线程
    private final KafkaThread<FvDimensionLayer> FV_D_SENDER = KafkaThread.createNewKafkaThread("fv_dimension",KafkaConfig.SEND_ALL_PACKET_FV_DIMENSION);
    private final KafkaThread<AttackBean> ATTACK_SENDER = KafkaThread.createNewKafkaThread("packet_attack", KafkaConfig.SEND_PACKET_ATTACK);

    public CapturePacketServiceImpl(PacketAnalyzeService packetAnalyzeService) {
        this.packetAnalyzeService = packetAnalyzeService;
        //所有攻击报文的入口
        AttackCommon.registerAttackCallback(attackBean -> {
            setDeviceInfo(attackBean);
            //通知攻击到达
            SocketServiceCenter.updateAllClient(SocketIoEvent.ATTACK_INFO, attackBean);
            //发送攻击信息保存
            ATTACK_SENDER.sendMsg(attackBean);
            //统计攻击类型
            CommonCacheUtil.addNewAttackLog(attackBean.getAttackType());
        });
    }

    private void setDeviceInfo(AttackBean attackBean){
        String srcDeviceNumber = CommonCacheUtil.getTargetDeviceNumberByTag(attackBean.getSrcIp(),attackBean.getSrcMac());
        if (srcDeviceNumber!=null){
            String deviceName = CommonCacheUtil.convertDeviceNumberToName(srcDeviceNumber);
            if (deviceName!=null) {
                attackBean.setSrcDevice(deviceName);
            }else{
                setUnknownAttackDevice(attackBean,1);
            }
        }else{
            setUnknownAttackDevice(attackBean,1);
        }
        String dstDeviceNumber = CommonCacheUtil.getTargetDeviceNumberByTag(attackBean.getDstIp(),attackBean.getDstMac());
        if (dstDeviceNumber!=null){
            String deviceName = CommonCacheUtil.convertDeviceNumberToName(dstDeviceNumber);
            if (deviceName!=null) {
                attackBean.setDstDevice(deviceName);
            }else {
                setUnknownAttackDevice(attackBean,0);
            }
        }else{
            setUnknownAttackDevice(attackBean,0);
        }
    }

    private void setUnknownAttackDevice(AttackBean attackBean, int i) {
        System.out.println(attackBean);
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
    }
    private void stopAllKafkaThread(){
        FV_D_SENDER.stopService();
        ATTACK_SENDER.stopService();
    }

    private NewFvDimensionCallback newFvDimensionCallback;

    private List<BasePreProcessor> processorList = new ArrayList<>();
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
            = new AbstractAsyncHandler<FvDimensionLayer>(Executors.newFixedThreadPool(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("-fv-dimension-handler-thread-");
        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
        return thread;
    })) {

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
            //统计所有的IP地址
            if (fvDimensionLayer.ip_dst[0].length() > 0){
                StatisticsData.statisticAllIpAddress(fvDimensionLayer.ip_dst[0]);           //t-s
            }
            //统计协议
            //协议比例
            StatisticsData.addProtocolNum(fvDimensionLayer.protocol,1);         //t-s
            //解析原始数据
            byte[] payload = ByteUtil.hexStringToByteArray(fvDimensionLayer.custom_ext_raw_data[0]);    //t-s
            fvDimensionLayer.rawData = payload;
            //设置五元组中的功能码以及功能码对应的含义
            if (Common.systemRunType !=0) {
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
            analyzeCollectorState(payload , collectorId);                         //分析采集器状态信息
            fvDimensionLayer.delay = collectorDelayInfo(payload,collectorId);     //解析时延信息
            fvDimensionLayer.collectorId = collectorId;                           //设置报文采集器ID
            FV_D_SENDER.sendMsg(fvDimensionLayer);                                //发送消息到数据库服务器
            AttackCommon.appendFvDimension(fvDimensionLayer);                     //将五元组添加到攻击分析模块中分析
            AttackCommon.appendCommonAttackAnalyze(fvDimensionLayer);
            return fvDimensionLayer;                                              //将五元组发送给BadPacketHandler
        }
    };

    public void setFuncode(FvDimensionLayer layer) throws ProtocolIdNotValidException {
        if (!(layer instanceof UndefinedPacket.LayersBean)) {
            int funCode = decodeFuncodeAndSetFuncodeMeaning(layer);
            if (funCode >= 0) {
                layer.funCode = String.valueOf(funCode);
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
    private int decodeFuncodeAndSetFuncodeMeaning(FvDimensionLayer t) throws ProtocolIdNotValidException {
        String funCodeStr;
        int funCode = -1;
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
            funCode = Integer.decode(cipPacket.cip_funcode[0]);
            t.funCodeMeaning = CommonConfigUtil.
                    getTargetProtocolFuncodeMeaning(PACKET_PROTOCOL.CIP_IP, funCode);
        }
        return funCode;
    }

    @Async
    @Override
    public CompletableFuture<Exception> start(ProcessCallback<String,String> callback) {
        startAllKafkaThread();
        this.callback = callback;
        List<BasePreProcessor> basePreProcessors = new LinkedList<>();
        for (String preProcessorName : preProcessor.getList()) {
            switch (preProcessorName){
                case "s7comm" :
                    basePreProcessors.add(new S7CommPreProcessor());
                    break;
                case "modbus" :
                    basePreProcessors.add(new ModbusPreProcessor());
                    break;
                case "iec104" :
                    basePreProcessors.add(new IEC104PreProcessor());
                    break;
                case "dnp3_0" :
                    basePreProcessors.add(new DNP3_0PreProcessor());
                    break;
                case "pnio" :
                    basePreProcessors.add(new PnioPreProcessor());
                    break;
                case "cip" :
                    basePreProcessors.add(new CipPreProcessor());
                    break;
                case "opcua" :
                    basePreProcessors.add(new OpcuaPreProcessor());
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

    private int collectorDelayInfo(byte[] payload , int collectorId) {
        if (payload.length > 0){
            if (collectorId > 0){
                //valid packet
                int collectorDelay = PacketDecodeUtil.decodeCollectorDelay(payload,4);
                //设置ID和延时用于发送
                //System.out.println("delay : " + collectorDelay);
                packetAnalyzeService.setCollectorDelay(collectorId,collectorDelay);
                return collectorDelay;
            }
            return -1;
        }
        return -1;
    }

    @Async
    @Override
    public CompletableFuture<Exception> stop() {
        for (BasePreProcessor basePreProcessor : processorList) {
            basePreProcessor.stopProcess();
            callback.end("end " + basePreProcessor.getClass().getName());
        }
        processorList.clear();
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
        processorList.add(basePreProcessor);
        DefaultPipeLine pipeLine = new DefaultPipeLine(processName);
        //fv_dimension_handler --> bad_packet_analyze_handler
        pipeLine.addLast(fvDimensionHandler);
        pipeLine.addLast(deviceHandler);
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
        //外界 --> PLC[ip_dst]  设备接收的报文数
        StatisticsData.increaseNumberByDeviceIn(CommonCacheUtil.getTargetDeviceNumberByTag(fvDimensionLayer.ip_dst[0],fvDimensionLayer.eth_dst[0])
            ,1);
        //外界 <-- PLC[ip_src]  设备发送的报文数
        StatisticsData.increaseNumberByDeviceOut(CommonCacheUtil.getTargetDeviceNumberByTag(fvDimensionLayer.ip_dst[0],fvDimensionLayer.eth_dst[0]),
                1);
        //外界 --> PLC[ip_dst]  设备接收的报文流量
        StatisticsData.increaseFlowByDeviceIn(CommonCacheUtil.getTargetDeviceNumberByTag(fvDimensionLayer.ip_dst[0],fvDimensionLayer.eth_dst[0])
        ,capLength);
        //外界 --> PLC[ip_dst]  设备发送的报文流量
        StatisticsData.increaseFlowByDeviceOut(CommonCacheUtil.getTargetDeviceNumberByTag(fvDimensionLayer.ip_dst[0],fvDimensionLayer.eth_dst[0])
                ,capLength);
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

    @Async
    @Override
    public CompletableFuture<Exception> startSimulate() {
        startAllKafkaThread();
        if (simulateThread == null){
            simulateThread = new SimulateThread();
        }
        if (!simulateThread.hasStart){
            DefaultPipeLine pipeLine = new DefaultPipeLine("simulate");
            pipeLine.addLast(fvDimensionLayerAbstractAsyncHandler);
            pipeLine.addLast(badPacketAnalyzeHandler);
            simulateThread.pipeLine = pipeLine;
            simulateThread.start();
        }else{
            simulateThread.notifyNow();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Exception> stopSimulate() {
        stopAllKafkaThread();
        simulateThread.idleNow();
        return CompletableFuture.completedFuture(null);
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
            new AbstractAsyncHandler<FvDimensionLayer>(Executors.newFixedThreadPool(1,
                    r -> {
                        Thread thread = new Thread(r);
                        thread.setName("-device-analyze-");
                        thread.setUncaughtExceptionHandler(Common.COMMON_THREAD_EXCEPTION_HANDLER);
                        return thread;
                    })) {
                @Override
                public FvDimensionLayer handle(Object t) {
                    //新设备统计
                    FvDimensionLayer layer = ((FvDimensionLayer) t);
                    Device[] device = CommonCacheUtil.autoAddDevice(layer);      //检测到新设备，推送新设备信息
                    if (device[0]!=null){
                        //come new device
                        SocketServiceCenter.updateAllClient(SocketIoEvent.NEW_DEVICE,device[0]);
                        //save device async
                        iDeviceService.saveOrUpdateDevice(device[0]);
                    }
                    if (device[1]!=null){
                        //come new device
                        SocketServiceCenter.updateAllClient(SocketIoEvent.NEW_DEVICE,device[1]);
                        //save device async
                        iDeviceService.saveOrUpdateDevice(device[1]);
                    }
                    //设备流量统计
                    CommonCacheUtil.addTargetDevicePacket(layer);
                    return layer;
                }
            };

}
