package com.zjucsc.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

    @Bean("common_schedule")
    public Executor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean("common_async")
    public Executor commonAsync() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean("device_schedule_service")
    public Executor deviceScheduleService(){
        return Executors.newScheduledThreadPool(1);
    }

    @Bean("d2d_schedule_service")
    public Executor d2dScheduleService(){
        return Executors.newScheduledThreadPool(1);
    }

    @Bean("top5_schedule_service")
    public Executor top5ScheduleService(){
        return Executors.newScheduledThreadPool(1);
    }
}
