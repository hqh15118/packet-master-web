package com.zjucsc.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class KafkaProducerCreatorTest {

    @Test
    public void getProducer() {

        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            KafkaProducer<String,String> kafkaProducer = KafkaProducerCreator.
                    getProducer("fv_dimension",String.class,String.class);
            ProducerRecord<String,String> producerRecord = new ProducerRecord<>("fv_dimension","str");
            kafkaProducer.send(producerRecord);
        }
        System.out.println(System.currentTimeMillis() - time1);
    }

    @Test
    public void removeProducer() {
    }

    @Test
    public void getProperties(){
        File file1 = new File("");
        System.out.println(file1.getAbsolutePath());
        File file = new File("config/kafka-config-producer.properties");
        assert file.exists();
        if (file.exists()){
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(properties);
        }
        else{
            System.out.println("file not exist");
        }
    }

    @Test
    public void kafkaThreadTest() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KafkaThread<String> kafkaThread = KafkaThread.createNewKafkaThread("fv_dimension","fv_dimension");
                for (int j = 0; j < 10000; j++) {
                    kafkaThread.sendMsg("test_msg");
                }
                System.out.println("finish fv_dimension");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                KafkaThread<String> kafkaThread = KafkaThread.createNewKafkaThread("test","test");
                for (int j = 0; j < 10000; j++) {
                    kafkaThread.sendMsg("test_msg");
                }
                System.out.println("finish test");
            }
        }).start();

        Thread.sleep(5000);
    }
}