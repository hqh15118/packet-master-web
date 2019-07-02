package com.zjucsc.zuul;

import com.netflix.zuul.context.RequestContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CustomHostRoutingFilter extends SimpleHostRoutingFilter {

    private URL defaultUrl;
    private List<String> hosts;
    private boolean debug;

    public CustomHostRoutingFilter(ProxyRequestHelper helper, ZuulProperties properties, ApacheHttpClientConnectionManagerFactory connectionManagerFactory, ApacheHttpClientFactory httpClientFactory) {
        super(helper, properties, connectionManagerFactory, httpClientFactory);
    }

    public CustomHostRoutingFilter(ProxyRequestHelper helper, ZuulProperties properties, CloseableHttpClient httpClient) {
        super(helper, properties, httpClient);
    }

    public void isDebug(boolean debug){
        this.debug = debug;
    }
    public void setHosts(List<String> hosts){
        this.hosts = hosts;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        if (defaultUrl == null){
            defaultUrl = context.getRouteHost();
        }
        long startTime = System.currentTimeMillis();
        for (String host : hosts) {
            try {
                context.setRouteHost(new URL(host));
            } catch (MalformedURLException e) {
                context.setRouteHost(defaultUrl);
            }
            super.run();
        }
        if (debug){
            System.out.println(String.format("**********\n请求URL：%s\n请求耗时：%s\n**********",
                    request.getRequestURI(),String.valueOf(System.currentTimeMillis() - startTime)));
        }
        return null;
    }


}
