package com.zjucsc.application.system.service;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.GraphInfo;
import com.zjucsc.application.domain.bean.GraphInfoCollection;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.tshark.capture.CapturePacketService;
import com.zjucsc.application.tshark.capture.CapturePacketServiceImpl;
import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import com.zjucsc.application.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import static com.zjucsc.application.config.StatisticsData.*;

@Slf4j
@Service
public class ScheduledService {

    @Autowired public PacketAnalyzeService packetAnalyzeService;

    private LinkedBlockingQueue<FvDimensionLayer> fvDimensionLayers = new LinkedBlockingQueue<>(5);

    @Autowired
    public ScheduledService(CapturePacketService capturePacketService){
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


    /**
     * 5秒钟发送一次统计信息
     */
    @Scheduled(fixedRate = 5000)
    public void sendPacketStatisticsMsg(){
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
    }

    @Scheduled(fixedRate = 1000)
    public void sendAllFvDimensionPacket(){
        for (int i = 0; i < 5; i++) {
            doSend(fvDimensionLayers.poll());
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendGraphInfo(){
        SocketServiceCenter.updateAllClient(SocketIoEvent.ART_INFO, StatisticsData.ART_INFO);
        addArtData("timestamp", CommonUtil.getDateFormat().format(new Date()));
        //System.out.println(StatisticsData.ART_INFO);
    }

    private void doSend(FvDimensionLayer layer){
        if (layer!=null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,layer);
        }
    }

    private static class SenderConsumer implements BiConsumer<String, Object>{
        private HashMap<String,Integer> map;
        private int index;
        private HashMap<String,GraphInfo> graphMap;
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
//                Integer var = null;
//                if ((var = map.get(deviceNumber)) == null) {//未添加过该设备
//                    res = data.getAndSet(0);
//                    map.put(deviceNumber, res);
//                } else {
//                    int var1 = data.get();
//                    res = var1 - var;
//                    map.put(deviceNumber, res);
//                }
                res = data.getAndSet(0);
                map.put(deviceNumber , res);
            }else{
                res = ((Integer) obj);
            }
            switch (index){
                case 1://ATTACK_BY_DEVICE
                    graphInfo.setAttack(res);
                    break;
                case 2://EXCEPTION_BY_DEVICE
                    graphInfo.setException(res);
                    break;
                case 3://NUMBER_BY_DEVICE_IN
                    graphInfo.setPacketIn(res);
                    break;
                case 4://NUMBER_BY_DEVICE_OUT
                    graphInfo.setPacketOut(res);
                    break;
                case 5://DELAY_INFO
                    graphInfo.setDelay(res);
                    break;
                default:log.info("*************************索引错误*************************");
                    break;
            }
        }
        public SenderConsumer setMap(HashMap<String,Integer> map , int index){
            this.map = map;
            this.index = index;
            return this;
        }
        public void setListMap(HashMap<String,GraphInfo> graphMap){
            this.graphMap = graphMap;
        }
    }
}
