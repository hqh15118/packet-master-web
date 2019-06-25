package com.zjucsc.application.system.service.hession_config;


import com.caucho.hessian.client.HessianProxyFactory;
import com.zjucsc.application.system.service.hessian_mapper.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

@Configuration
@ConfigurationProperties(prefix = "hessian-config")
@Data
public class HessianConfig {

    private String art_history_data;
    private String art_config;
    private String configuration_setting;
    private String device;
    private String fv_dimension_filter;
    private String gplot;
    private String opt_filter;
    private String protocol_id;
    private String user_opt;
    private String art_filter;
    private String attack_info;
    private String packet_history;


    @Bean
    public ArtHistoryDataMapper artHistoryHessianBean() throws MalformedURLException {
        return (ArtHistoryDataMapper) new HessianProxyFactory().create(ArtHistoryDataMapper.class, art_history_data);
    }

    @Bean
    public ArtConfigMapper artConfigHessianBean() throws MalformedURLException {
        return (ArtConfigMapper) new HessianProxyFactory().create(ArtConfigMapper.class, art_config);
    }

    @Bean
    public ConfigurationSettingMapper configurationSettingHessianBean() throws MalformedURLException {
        return (ConfigurationSettingMapper) new HessianProxyFactory().create(ConfigurationSettingMapper.class, configuration_setting);
    }

    @Bean
    public DeviceMapper deviceHessianBean() throws MalformedURLException {
        return (DeviceMapper) new HessianProxyFactory().create(DeviceMapper.class, device);
    }

    @Bean
    public FvDimensionFilterMapper fvDimensionFilterHessianBean() throws MalformedURLException {
        return (FvDimensionFilterMapper) new HessianProxyFactory().create(FvDimensionFilterMapper.class, fv_dimension_filter);
    }

    @Bean
    public GplotMapper gplotHessianBean() throws MalformedURLException {
        return (GplotMapper) new HessianProxyFactory().create(GplotMapper.class, gplot);
    }

    @Bean
    public OptFilterMapper optFilterHessianBean() throws MalformedURLException {
        return (OptFilterMapper) new HessianProxyFactory().create(OptFilterMapper.class, opt_filter);
    }

    @Bean
    public ProtocolIdMapper protocolIdHessianBean() throws MalformedURLException {
        return (ProtocolIdMapper) new HessianProxyFactory().create(ProtocolIdMapper.class, protocol_id);
    }

    @Bean
    public UserOptMapper userOptHessianBean() throws MalformedURLException {
        return (UserOptMapper) new HessianProxyFactory().create(UserOptMapper.class, user_opt);
    }

    @Bean
    public ArtifactFilterMapper artifactFilterHessianBean() throws MalformedURLException {
        return (ArtifactFilterMapper) new HessianProxyFactory().create(ArtifactFilterMapper.class, art_filter);
    }

    @Bean
    public PacketInfoMapper packetInfoHessianBean() throws MalformedURLException {
        return (PacketInfoMapper) new HessianProxyFactory().create(PacketInfoMapper.class, packet_history);
    }
}
