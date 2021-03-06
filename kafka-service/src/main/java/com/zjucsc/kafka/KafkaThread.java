package com.zjucsc.kafka;


import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KafkaThread<V> extends Thread implements IKafka<V> {
    private final static int maxSize = 100000;
    private final LinkedBlockingQueue<V> TASK_QUEUE =
            new LinkedBlockingQueue<>(maxSize);
    private KafkaProducer<String,String> kafkaProducer;
    private String topic;
    private int partition = -1;
    private volatile boolean running = true;
    private String bindService;
    private boolean hasStart = false;
    private SendErrorCallback<V> sendErrorCallback;

    @SuppressWarnings("unchecked")
    public static <V> KafkaThread<V> createNewKafkaThread(String service,String bindTopic){
        KafkaThread kafkaThread = new KafkaThread<>(service, bindTopic);
        KafkaCommon.register(kafkaThread);
        return kafkaThread;
    }

    @SuppressWarnings("unchecked")
    public static <V> KafkaThread<V> createNewKafkaThread(String service,String bindTopic,SendErrorCallback<V> sendErrorCallback){
        KafkaThread kafkaThread = new KafkaThread<>(service, bindTopic);
        kafkaThread.registerErrorCallback(sendErrorCallback);
        KafkaCommon.register(kafkaThread);
        return kafkaThread;
    }

    private KafkaThread(String service,String bindTopic){
        kafkaProducer = KafkaProducerCreator.getProducer(service,String.class,String.class);
        this.topic = bindTopic;
        this.bindService = service;
        setName("-kafka-sender-" + service + "-" + bindTopic);
    }

    @Override
    public void run() {
        try {
            for (;;) {
                V v = TASK_QUEUE.take();
                String msg = convertObjectToString(v);
                //valid msg
                ProducerRecord<String, String> kvProducerRecord;
                if (partition >= 0)
                    kvProducerRecord = new ProducerRecord<>(topic, partition, null, msg);
                else
                    kvProducerRecord = new ProducerRecord<>(topic, msg);
                kafkaProducer.send(kvProducerRecord);
                if (!running) {
                    break;
                }
            }
        }catch(InterruptedException e){
            //time out just ignore
        }
    }

    private String convertObjectToString(V v){
        return JSON.toJSONString(v);
    }

    @Override
    public void sendMsg(V v) {
        if(!TASK_QUEUE.offer(v) && sendErrorCallback!=null){
            sendErrorCallback.fail(getName(),v);
        }
    }

    public void registerErrorCallback(SendErrorCallback<V> sendErrorCallback){
        this.sendErrorCallback = sendErrorCallback;
    }

    @Override
    public void startService() {
        running = true; //设置开始运行标志位
        if (!hasStart) {
            start();        //开始运行run()方法
            hasStart = true;
        }
    }

    @Override
    public void stopService() {
        interrupt();
        running = false;
    }

    @Override
    public void quitKafka() {
        stopService();
        kafkaProducer.close();
        KafkaProducerCreator.removeProducer(bindService);
        KafkaCommon.unregister(this);
    }

    public void setPartition(int partition){
        this.partition = partition;
    }

    public int getIdleTask(){
        return TASK_QUEUE.size();
    }

    @Override
    public String toString() {
        return "KafkaThread{" +
                "idle_task_num=" + TASK_QUEUE.size() + "\n" +
                ", topic='" + topic + '\'' + "\n" +
                ", partition=" + partition + "\n" +
                ", running=" + running + "\n" +
                ", bindService='" + bindService + '\'' + "\n" +
                '}';
    }

    public interface SendErrorCallback<V>{
        void fail(String threadName , V v);
    }
}
