package com.zjucsc.zuul;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
@EnableConfigurationProperties(ConstantConfig.class)
public class ZuulApplication  {

    @Autowired private ConstantConfig constantConfig;

    public static void main(String[] args){
        SpringApplication.run(ZuulApplication.class, args);
    }

    @Bean
    public CustomHostRoutingFilter simpleHostRoutingFilter(ProxyRequestHelper helper,
                                                           ZuulProperties zuulProperties,
                                                           ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
                                                           ApacheHttpClientFactory httpClientFactory) {
        CustomHostRoutingFilter customHostRoutingFilter =  new CustomHostRoutingFilter(helper, zuulProperties,
                connectionManagerFactory, httpClientFactory);
        customHostRoutingFilter.setHosts(constantConfig.getHost());
        customHostRoutingFilter.isDebug(constantConfig.isShowLog());
        return customHostRoutingFilter;
    }

    @Bean
    public ErrorFilter errorFilter(){
        return new ErrorFilter();
    }
}
