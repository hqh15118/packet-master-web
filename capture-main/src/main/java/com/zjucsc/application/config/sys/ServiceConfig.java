package com.zjucsc.application.config.sys;

import com.zjucsc.common.bean.CustomThreadPoolExecutor;
import com.zjucsc.common.util.CommonUtil;
import com.zjucsc.common.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-30 - 22:32
 */
@Configuration
@EnableAsync
@Slf4j
public class ServiceConfig {

    @Bean("sendAllFvDimensionPacket2")
    public Executor taskExecutor() {
        return generateCommonThreadPool("-sendAllFvDimensionPacket2-", "error of sendAllFvDimensionPacket2 service ", "sendAllFvDimensionPacket2 reject task");
    }

    @Bean("common_async")
    public Executor commonAsync() {
        return ThreadPoolUtil.getFixThreadPoolSizeThreadPool(
                5, 50, r -> {
                    Thread thread = new Thread(r);
                    thread.setName("-common_async-");
                    thread.setUncaughtExceptionHandler((t, e) -> log.error("error of common async service ",e));
                    return thread;
                }
                , "common async service", (r, executor) -> log.error("common async service reject  task"));
    }

    @Bean("common_service")
    public Executor taskExecutor1() {
        return generateCommonThreadPool("-common-service-", "error of common service service ", "common service service reject task");
    }

    @Bean("art_info_service")
    public Executor artInfoService(){
        return generateCommonThreadPool("-art-info-service-", "error of art info service ", "art info service reject task");
    }

    @Bean("device_schedule_service")
    public Executor deviceScheduleService(){
        return generateCommonThreadPool("-device-schedule-service-", "error of device schedule service ", "device schedule service reject task");
    }

    private CustomThreadPoolExecutor generateCommonThreadPool(String threadName, String threadErrorMsg, String rejectErrorMsg) {
        return (CustomThreadPoolExecutor) ThreadPoolUtil.getSingleThreadPoolSizeThreadPool(50, r -> {
            Thread thread = new Thread(r);
            thread.setName(threadName);
            thread.setUncaughtExceptionHandler((t, e) -> log.error(threadErrorMsg,e));
            return thread;
        },threadName, (r, executor) -> {
            log.error(rejectErrorMsg);
        });
    }

    @Bean("d2d_schedule_service")
    public Executor d2dScheduleService(){
        return ThreadPoolUtil.getSingleThreadPoolSizeThreadPool(100, r -> {
            Thread thread = new Thread(r);
            thread.setName("-d2d-schedule-service-");
            thread.setUncaughtExceptionHandler((t, e) -> System.err.println("error of d2d schedule service " + e));
            return thread;
        }, "d2d_schedule_service", (r, executor) -> {
            log.error("d2d_schedule_service reject task");
        });
    }

    @Bean("top5_schedule_service")
    public Executor top5ScheduleService(){
        return generateCommonThreadPool("-top5-schedule-service-",
                "error of top5 schedule service ","top5 schedule service reject task");
    }
}
