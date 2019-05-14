package com.zjucsc;

import com.zjucsc.application.config.ConstantConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.zjucsc.application.system.mapper"})
@EnableCaching
@EnableConfigurationProperties(ConstantConfig.class)
public class PacketMasterWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacketMasterWebApplication.class, args);
    }

}
