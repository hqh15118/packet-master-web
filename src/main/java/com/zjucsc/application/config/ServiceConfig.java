package com.zjucsc.application.config;

import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.system.service.TsharkMainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

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
        return getNewExecutor("global_single_thread_executor");
    }

    @Bean("attack_info_thread_pool")
    public Executor async_attack_info(){
        return getNewExecutor("attack_info_thread_pool");
    }

    private Executor getNewExecutor(String name){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                r.run();
            }
        });
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setThreadGroupName(name);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public Executor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }


}
