package com.zjucsc.application.system.service.hession_config;


import com.zjucsc.application.system.service.iservice.IArtHistoryData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

@Configuration
@ConfigurationProperties(prefix = "hessian_service_config.hessian_url")
public class ArtHistoryHessianConfig {

    @Value("art_history_data")
    private String artHistoryData;


    @Bean
    public HessianProxyFactoryBean artHistoryHessianBean(){
        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
        factory.setServiceUrl(artHistoryData);
        factory.setServiceInterface(IArtHistoryData.class);
        return factory;
    }


}
