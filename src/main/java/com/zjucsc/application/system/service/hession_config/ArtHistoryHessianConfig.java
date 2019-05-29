package com.zjucsc.application.system.service.hession_config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hessian-service-config")
public class ArtHistoryHessianConfig {

//    @Value("art-history-data")
//    private String artHistoryData;
//
//
//    @Bean
//    public HessianProxyFactoryBean artHistoryHessianBean(){
//        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
//        factory.setServiceUrl(artHistoryData);
//        factory.setServiceInterface(IArtHistoryData.class);
//        return factory;
//    }


}
