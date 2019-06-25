package com.zjucsc;

import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.util.TsharkUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableCaching
@EnableConfigurationProperties(ConstantConfig.class)
public class PacketMasterWebApplication{

    public static void main(String[] args) {
        String str = TsharkUtil.checkTsharkValid();
        if (str == null) {
            System.err.println("tshark not in system PATH,application failed to start");
            return;
        }else{
            System.out.println("**************\nfind tshark in: " + str + " \napplication start now >>>\n**************");
        }
        SpringApplication.run(PacketMasterWebApplication.class, args);
    }

}
