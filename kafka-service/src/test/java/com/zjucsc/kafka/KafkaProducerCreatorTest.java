package com.zjucsc.kafka;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class KafkaProducerCreatorTest {

    @Test
    public void getProducer() {
    }

    @Test
    public void removeProducer() {
    }

    @Test
    public void getProperties(){
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
}