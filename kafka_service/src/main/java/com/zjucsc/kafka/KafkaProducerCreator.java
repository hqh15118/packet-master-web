package com.zjucsc.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaProducerCreator {

    private static ConcurrentHashMap<String, KafkaProducer> cachedKafkaProducer = new ConcurrentHashMap<>();

    private static Properties getKafkaProperties(){

    }

    public static <K,V> Producer<K, V> getProducer(String service , Class<K> keyClass , Class<V> valueClass) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.KAFKA_BROKERS);//10.15.191.100:9092
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstant.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return getProducer(props,service,keyClass,valueClass);
    }

    @SuppressWarnings("unchecked")
    public static <K,V> Producer<K, V> getProducer(Properties properties , String service,
                                                   Class<K> keyClass , Class<V> valueClass) {
        KafkaProducer<K,V> kafkaProducer = cachedKafkaProducer.get(service);
        if (kafkaProducer==null){
            kafkaProducer = new KafkaProducer<>(properties);
            cachedKafkaProducer.put(service,kafkaProducer);
        }
        return kafkaProducer;
    }
}
