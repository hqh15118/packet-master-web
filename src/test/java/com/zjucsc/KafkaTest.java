package com.zjucsc;

import com.zjucsc.kafka.KafkaProducerCreator;
import com.zjucsc.kafka.KafkaThread;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;

public class KafkaTest {

    @Test
    public void kafkaSendFvDimension() throws InterruptedException {
        KafkaProducer<String,String> kafkaProducer = KafkaProducerCreator.getProducer("fv_dimension",String.class,String.class);
        for (int i = 0; i < 1000; i++) {
            String str = "test";
            ProducerRecord producerRecord = new ProducerRecord("fv_dimension",str);
            kafkaProducer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("****************");
                    System.out.println(e);
                }
            });
        }

        Thread.sleep(1000000);
    }

    @Test
    public void kafkaSenderThreadTest() throws InterruptedException {
        KafkaThread<String> kafkaThread = KafkaThread.createNewKafkaThread("fv_dimension","fv_dimension");
        kafkaThread.start();
        for (int i = 0; i < 10; i++) {
            kafkaThread.sendMsg("test");
        }
        Thread.sleep(1000000);
    }
}
