package com.zjucsc.application.system.service.hession_config;


import com.caucho.hessian.client.HessianProxyFactory;
import com.zjucsc.application.system.service.hessian_mapper.ArtHistoryDataMapper;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

@Configuration
@ConfigurationProperties(prefix = "hessian-config")
@Data
public class ArtHistoryHessianConfig {

    private String art_history_data;

    @Bean
    public ArtHistoryDataMapper artHistoryHessianBean() throws MalformedURLException {
        return (ArtHistoryDataMapper) new HessianProxyFactory().create(ArtHistoryDataMapper.class, art_history_data);
    }



}
