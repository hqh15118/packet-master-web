package com.zjucsc.application.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ConfigUtil {
    private static Properties properties;
    public synchronized static void init(){
        if (properties!=null){
            return;
        }
        File configFile = new File("application-ext.properties");
        if (!configFile.exists()){
            try {
                if(configFile.createNewFile()){
                    log.info("成功创建【application-ext.properties】文件");
                }
            } catch (IOException e) {
                log.error("无法创建【application-ext.properties】");
            }
        }
        properties = new Properties();
        try {
            properties.load(new FileInputStream(configFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties getProperties(){
        if (properties == null){
            init();
        }
        return properties;
    }

    public static void setData(String key,String value){
        getProperties().setProperty(key,value);
    }

    public static Object getData(String key){
        return getProperties().get(key);
    }
}
