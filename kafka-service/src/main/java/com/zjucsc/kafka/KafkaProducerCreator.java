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

    private static Properties properties;
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

    static synchronized <K,V> KafkaProducer<K, V> getProducer(String service , Class<K> keyClass , Class<V> valueClass){
        if (properties == null) {
            properties = getKafkaProperties();
        }
        if (properties == null){
            properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.KAFKA_BROKERS);//10.15.191.100:9092
            properties.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstant.CLIENT_ID);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        }
        return getProducer(properties, service, keyClass, valueClass);
    }

    @SuppressWarnings("unchecked")
    private static <K,V> KafkaProducer<K, V> getProducer(Properties properties, String service,
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
