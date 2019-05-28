package com.zjucsc.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class KafkaProducerCreator {

    private static ConcurrentHashMap<String, KafkaProducer> cachedKafkaProducer = new ConcurrentHashMap<>();

    /**
     *
     * @return
     */
    private static Properties getKafkaProperties(){
        File file = new File("config/kafka-config-producer.properties");
        if (file.exists()){
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException e) {
                System.err.println("error load properties");
                return null;
            }
            return properties;
        }else{
            return null;
        }
    }

    public static <K,V> Producer<K, V> getProducer(String service , Class<K> keyClass , Class<V> valueClass){
        Properties props = getKafkaProperties();
        if (props == null){
            props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.KAFKA_BROKERS);//10.15.191.100:9092
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstant.CLIENT_ID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        }
        return getProducer(props, service, keyClass, valueClass);
    }

    @SuppressWarnings("unchecked")
    private static <K,V> Producer<K, V> getProducer(Properties properties, String service,
                                                    Class<K> keyClass, Class<V> valueClass) {
        KafkaProducer<K,V> kafkaProducer = cachedKafkaProducer.get(service);
        if (kafkaProducer==null){
            kafkaProducer = new KafkaProducer<>(properties);
            cachedKafkaProducer.put(service,kafkaProducer);
        }
        return kafkaProducer;
    }

    public static void removeProducer(String service){
        cachedKafkaProducer.remove(service);
    }
}
