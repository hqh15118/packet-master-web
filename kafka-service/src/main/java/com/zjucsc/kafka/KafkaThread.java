package com.zjucsc.kafka;


import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class KafkaThread<V> extends Thread implements IKafka<V> {

    private final LinkedBlockingQueue<V> TASK_QUEUE =
            new LinkedBlockingQueue<>();
    private KafkaProducer<String,String> kafkaProducer;
    private String topic;
    private int partition = -1;
    private volatile boolean running = false;
    private String bindService;

    @SuppressWarnings("unchecked")
    public static <V> KafkaThread<V> createNewKafkaThread(String service,String bindTopic){
        KafkaThread kafkaThread = new KafkaThread<>(service, bindTopic);
        KafkaCommon.register(kafkaThread);
        return kafkaThread;
    }

    private KafkaThread(String service,String bindTopic){
        kafkaProducer = KafkaProducerCreator.getProducer(service,String.class,String.class);
        this.topic = bindTopic;
        this.bindService = service;
        setName("kafka-sender-" + service + "-" + bindTopic);
    }

    @Override
    public void run() {
        for (;;){
            try {
                V v = TASK_QUEUE.poll(2, TimeUnit.SECONDS); //poll msg from queue
                String msg = convertObjectToString(v);
                if (v != null){
                    //valid msg
                    ProducerRecord<String,String> kvProducerRecord;
                    if (partition >= 0)
                        kvProducerRecord = new ProducerRecord<>(topic, partition, null, msg);
                    else
                        kvProducerRecord = new ProducerRecord<>(topic,msg);
                    kafkaProducer.send(kvProducerRecord);
                }
                if (!running){
                    break;
                }
            } catch (InterruptedException e) {
                //time out just ignore
            }
        }
    }

    private String convertObjectToString(V v){
        //性能瓶颈？
        return JSON.toJSONString(v);
    }

    @Override
    public void sendMsg(V v) {
        TASK_QUEUE.offer(v);
    }

    @Override
    public void startService() {
        running = true; //设置开始运行标志位
        start();        //开始运行run()方法
    }

    @Override
    public void stopService() {
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
                "idle_task_num=" + TASK_QUEUE.size() +
                ", topic='" + topic + '\'' +
                ", partition=" + partition +
                ", running=" + running +
                ", bindService='" + bindService + '\'' +
                '}';
    }
}
