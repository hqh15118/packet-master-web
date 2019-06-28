package com.zjucsc.application.system.service.common_impl;

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler;
import com.zjucsc.application.tshark.pre_processor.*;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.attack.common.AttackCommon;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.common.common_util.DBUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.kafka.KafkaThread;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.handler.DefaultPipeLine;
import com.zjucsc.tshark.packets.*;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import javafx.scene.control.RadioMenuItem;
import jdk.nashorn.internal.scripts.JD;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.zjucsc.application.config.Common.COMMON_THREAD_EXCEPTION_HANDLER;
import static com.zjucsc.application.config.PACKET_PROTOCOL.*;

@Slf4j
@Service
public class CapturePacketServiceImpl implements CapturePacketService<String,String> {

    @Autowired private PacketAnalyzeService packetAnalyzeService;

    //五元kafka发送线程
    private final KafkaThread<FvDimensionLayer> FV_D_SENDER = KafkaThread.createNewKafkaThread("fv_dimension","fv_dimension");

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
    public AbstractAsyncHandler<FvDimensionLayer> fvDimensionLayerAbstractAsyncHandler
            = new AbstractAsyncHandler<FvDimensionLayer>(Executors.newFixedThreadPool(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("fv_dimension_handler_thread-");
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
            fvDimensionLayer.frame_protocols[0] = PacketDecodeUtil.discernPacket(fvDimensionLayer.frame_protocols[0]);
            //统计所有的IP地址
            if (fvDimensionLayer.ip_dst[0].length() > 0){
                StatisticsData.statisticAllIpAddress(fvDimensionLayer.ip_dst[0]);
            }
            //统计协议
            StatisticsData.addProtocolNum(fvDimensionLayer.frame_protocols[0],1);
            //解析原始数据
            byte[] payload = ByteUtil.hexStringToByteArray(fvDimensionLayer.custom_ext_raw_data[0]);
            fvDimensionLayer.rawData = payload;
            //设置五元组中的功能码以及功能码对应的含义
            try {
                setFuncode(fvDimensionLayer);
            } catch (ProtocolIdNotValidException e) {
                //缓存中找不到该五元组对应的协议
                log.error("error set Funcode , msg : {} [缓存中找不到该五元组协议：{} 对应的功能码表]" , e.getMsg(),fvDimensionLayer.frame_protocols[0]);
            }
            sendFvDimensionPacket(fvDimensionLayer , payload);                    //发送五元组所有报文到前端
            sendPacketStatisticsEvent(fvDimensionLayer);                          //发送统计信息
            int collectorId = PacketDecodeUtil.decodeCollectorId(payload,24);
            analyzeCollectorState(payload , collectorId);                         //分析采集器状态信息
            fvDimensionLayer.delay = collectorDelayInfo(payload,collectorId);     //解析时延信息
            fvDimensionLayer.collectorId = collectorId;                           //设置报文采集器ID
            FV_D_SENDER.sendMsg(fvDimensionLayer);                                //发送消息到数据库服务器
            AttackCommon.appendFvDimension(fvDimensionLayer);                     //将五元组发送添加到攻击分析模块中分析
            return fvDimensionLayer;                                              //将五元组发送给BadPacketHandler
        }
    };

    public void setFuncode(FvDimensionLayer layer) throws ProtocolIdNotValidException {
        if (!(layer instanceof UndefinedPacket.LayersBean)) {
            int funCode = decodeFuncode(layer);
            if (funCode >= 0){
                layer.funCodeMeaning = CommonConfigUtil.
                        getTargetProtocolFuncodeMeanning(layer.frame_protocols[0],funCode);
                layer.funCode = String.valueOf(funCode);
            }else{
                layer.funCode = "--";
            }
        }
    }

    /**
     * 解析功能码，一些协议有些报文有功能码有些报文没有功能码，要做区分
     * 没有功能码返回-1
     * @param t 五元组
     * @return 功能码
     */
    private int decodeFuncode(FvDimensionLayer t) {
        String funCodeStr;
        if (t instanceof S7CommPacket.LayersBean){
            if (((S7CommPacket.LayersBean) t).s7comm_param_func!=null) {
                funCodeStr = ((S7CommPacket.LayersBean) t).s7comm_param_func[0];
                return PacketDecodeUtil.decodeFuncode(S7, funCodeStr);
            }
        }else if (t instanceof ModbusPacket.LayersBean){
            if (((ModbusPacket.LayersBean) t).modbus_func_code!=null) {
                funCodeStr = ((ModbusPacket.LayersBean) t).modbus_func_code[0];
                return PacketDecodeUtil.decodeFuncode(MODBUS, funCodeStr);
            }
        }else if (t instanceof IEC104Packet.LayersBean){
            IEC104Packet.LayersBean iec104_packet = ((IEC104Packet.LayersBean) t);
            if (iec104_packet.iec104_funcode!=null) {
                funCodeStr = ((IEC104Packet.LayersBean) t).iec104_funcode[0];
                return PacketDecodeUtil.decodeFuncode(IEC104, funCodeStr);
            }
        }
        else{
            //log.error("can not decode funCode of protocol : {} cause it is not defined" , t.frame_protocols[0]);
            return -1;
        }
        return -1;
    }

    @Async
    @Override
    public CompletableFuture<Exception> start(ProcessCallback<String,String> callback) {
        FV_D_SENDER.startService();
        this.callback = callback;
        try {
            callback.start(doStart(fvDimensionLayerAbstractAsyncHandler
                                   ,new ModbusPreProcessor()
                                   ,new S7CommPreProcessor()
                                   ,new IEC104PreProcessor()
                                   ,new PnioPreProcessor()
                                   ,new UndefinedPreProcessor()      //必须放在解析协议的最后
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
        FV_D_SENDER.stopService();
        return CompletableFuture.completedFuture(null);
    }

    private String doStart(AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler ,
                           BasePreProcessor... packetPreProcessor) throws InterruptedException {
        CountDownLatch downLatch = new CountDownLatch(packetPreProcessor.length - 1);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < packetPreProcessor.length - 1; i++) {
            BasePreProcessor basePreProcessor = packetPreProcessor[i];
            doNow(basePreProcessor , fvDimensionHandler , downLatch , sb);
        }
        downLatch.await(100, TimeUnit.SECONDS);
        doNow(packetPreProcessor[i] , fvDimensionHandler,null , sb);
        return sb.toString();
    }

    private void doNow(BasePreProcessor basePreProcessor , AbstractAsyncHandler<FvDimensionLayer> fvDimensionHandler,
                       CountDownLatch downLatch , StringBuilder sb){
        String processName = basePreProcessor.getClass().getName();
        processorList.add(basePreProcessor);
        DefaultPipeLine pipeLine = new DefaultPipeLine(processName);
        //fv_dimension_handler --> bad_packet_analyze_handler
        pipeLine.addLast(fvDimensionHandler);
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
            newFvDimensionCallback.newCome(fvDimensionLayer);
        }
    }

    private void sendPacketStatisticsEvent(FvDimensionLayer fvDimensionLayer) {
        int capLength = Integer.parseInt(fvDimensionLayer.frame_cap_len[0]);

        StatisticsData.FLOW.addAndGet(capLength);                          //计算报文流量

        StatisticsData.recvPacketNumber.addAndGet(1);      //总报文数
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
            log.info("**********************\ncollector state change : {} \n **********************" , collectorState);
            SocketServiceCenter.updateAllClient(SocketIoEvent.COLLECTOR_STATE,collectorState);
        }
        //System.out.println(collectorId + " -- " + collectorState);
    }

    public void setNewFvDimensionCallback(NewFvDimensionCallback newFvDimensionCallback){
        this.newFvDimensionCallback = newFvDimensionCallback;
    }

    private SimulateThread simulateThread = new SimulateThread();

    @Async
    @Override
    public CompletableFuture<Exception> startSimulate() {
        FV_D_SENDER.startService();
        if (!simulateThread.hasStart){
            DefaultPipeLine pipeLine = new DefaultPipeLine("simulate");
            pipeLine.addLast(fvDimensionLayerAbstractAsyncHandler);
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
        FV_D_SENDER.stopService();
        simulateThread.idleNow();
        return CompletableFuture.completedFuture(null);
    }

    private static class SimulateThread extends Thread{
        private DefaultPipeLine pipeLine;
        private boolean hasStart = false;
        private volatile boolean idle = false;
        private static final byte[] IDLE_LOCK = new byte[1];
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
                int date = 20190625;
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
                int num = (int)(300 * random.nextDouble()) + 200;
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
}
