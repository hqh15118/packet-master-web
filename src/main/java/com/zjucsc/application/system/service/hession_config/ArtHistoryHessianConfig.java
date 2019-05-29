package com.zjucsc.application.system.service.hession_config;


import com.zjucsc.application.system.service.iservice.IArtHistoryData;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

@Configuration
@ConfigurationProperties(prefix = "hessian-config")
@Data
public class ArtHistoryHessianConfig {

    private String art_history_data;

    @Bean
    public HessianProxyFactoryBean artHistoryHessianBean(){
        HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
        System.out.println(art_history_data);
        factory.setServiceUrl(art_history_data);
        factory.setServiceInterface(IArtHistoryData.class);
        return factory;
    }


}
