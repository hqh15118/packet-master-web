package com.zjucsc.application.config;

import com.zjucsc.application.system.service.impl.PacketAnalyzeService;
import com.zjucsc.application.system.service.impl.TsharkMainService;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-30 - 22:32
 */
@Configuration
@EnableAsync
public class ServiceConfig {

    @Bean(name = "tshark_main_service")
    public TsharkMainService initTsharkMainService(){
        return new TsharkMainService();
    }

    @Bean(name = "packet_analyze_service")
    public PacketAnalyzeService initPacketAnalyzeService(){
        return new PacketAnalyzeService();
    }

    @Bean("global_single_thread_executor")
    public Executor asyncThreadPoolConfig(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                r.run();
            }
        });
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setThreadGroupName("global-single-thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }

//    @Bean
//    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
//        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
//            @Override
//            public void customize(ConcurrentMapCacheManager cacheManager) {
//                cacheManager.setAllowNullValues(false);
//            }
//        };
//    }
}
