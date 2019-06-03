package com.zjucsc.application.system.service;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.config.StatisticsData;
import com.zjucsc.application.domain.bean.FlowError;
import com.zjucsc.application.domain.bean.GraphInfo;
import com.zjucsc.application.domain.bean.StatisticsDataWrapper;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.CommonUtil;
import com.zjucsc.kafka.KafkaProducerCreator;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static com.zjucsc.application.config.KafkaConfig.SEND_ALL_PACKET_FV_DIMENSION;
import static com.zjucsc.application.config.StatisticsData.*;

@Slf4j
@Service
public class ScheduledService {

    @Autowired public PacketAnalyzeService packetAnalyzeService;

    @Autowired private RedisTemplate<String,String> redisTemplate;

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

    private int count = 1;

    @Scheduled(fixedRate = 1000)
    public void commonScheduledService(){
        count++;
        //每5秒
        if (count % 5 == 0){
            sendPacketStatisticsMsg();
            sendGraphInfo();
            statisticFlow();
        }
        //每1秒
        sendAllFvDimensionPacket();
    }

    /**
     * 5秒钟发送一次统计信息
     */
    //@Scheduled(fixedRate = 5000)
    private void sendPacketStatisticsMsg(){
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

    //@Scheduled(fixedRate = 1000)
    private void sendAllFvDimensionPacket(){
        for (int i = 0; i < 5; i++) {
            doSend(fvDimensionLayers.poll());
        }
    }



    //@Scheduled(fixedRate = 2000)
    private void sendToRemoteByKafka() {
        ListOperations<String,String> opsForList = redisTemplate.opsForList();
        //先取出旧值
        String key = CommonConfigUtil.getFvDimensionKeyInRedis();
        //先更新Redis中的键
        CommonConfigUtil.updateFvDimensionKeyInRedis();
        //取出已经存储好的所有五元组
        List<String> fvDimensionStr = opsForList.range(key,0,-1);
        KafkaProducer<String,String> kafkaProducer = KafkaProducerCreator.getProducer("send_fv_dimension",
                String.class,String.class);
        if (fvDimensionStr!=null){
            long time1 = System.currentTimeMillis();
            for (String fv : fvDimensionStr) {
                ProducerRecord<String,String> record = new ProducerRecord<>(SEND_ALL_PACKET_FV_DIMENSION,fv);
                kafkaProducer.send(record);
            }
            log.info("发送 {} 条五元组数据花费了 {} ms",fvDimensionStr.size() , System.currentTimeMillis() - time1);
        }else{
            log.error("redis缓存中不存在键{} 对应的五元组数据集合",key);
        }
        redisTemplate.delete(key);
    }

    /**
     * 工艺参数信息
     */
    //@Scheduled(fixedRate = 5000)
    private void sendGraphInfo(){
        SocketServiceCenter.updateAllClient(SocketIoEvent.ART_INFO, StatisticsData.ART_INFO);
        addArtData("timestamp", CommonUtil.getDateFormat().format(new Date()));
        //System.out.println(StatisticsData.ART_INFO);
    }

    private void doSend(FvDimensionLayer layer){
        if (layer!=null){
            SocketServiceCenter.updateAllClient(SocketIoEvent.ALL_PACKET,layer);
        }
    }

    /**
     * 流量信息
     */
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
