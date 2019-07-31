package com.zjucsc.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

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
        return Executors.newFixedThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("-common-schedule-service-");
            thread.setUncaughtExceptionHandler((t, e) -> {
                System.err.println("error of common schedule service " + e);
            });
            return thread;
        });
    }

    @Bean("common_async")
    public Executor commonAsync() {
        return Executors.newFixedThreadPool(5, r -> {
            Thread thread = new Thread(r);
            thread.setName("-common-async-service-");
            thread.setUncaughtExceptionHandler((t, e) -> System.err.println("error of common async service " + e));
            return thread;
        });
    }

    @Bean("device_schedule_service")
    public Executor deviceScheduleService(){
        return Executors.newScheduledThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("-device-schedule-service-");
            thread.setUncaughtExceptionHandler((t, e) -> System.err.println("error of device schedule service " + e));
            return thread;
        });
    }

    @Bean("d2d_schedule_service")
    public Executor d2dScheduleService(){
        return Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("-d2d-schedule-service-");
                thread.setUncaughtExceptionHandler((t, e) -> System.err.println("error of d2d schedule service " + e));
                return thread;
            }
        });
    }

    @Bean("top5_schedule_service")
    public Executor top5ScheduleService(){
        return Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("-top5-schedule-service-");
                thread.setUncaughtExceptionHandler((t, e) -> System.err.println("error of top5 schedule service " + e));
                return thread;
            }
        });
    }
}
