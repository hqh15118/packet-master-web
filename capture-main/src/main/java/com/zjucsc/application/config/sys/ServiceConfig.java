package com.zjucsc.application.config.sys;

import com.zjucsc.base.util.ThreadPoolUtil;
import com.zjucsc.common.bean.CustomThreadPoolExecutor;
import com.zjucsc.common.util.CommonUtil;
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

    @Bean("common_schedule")
    public Executor taskExecutor() {
        return generateCommonThreadPool("-common-schedule-", "error of common schedule service ", "common schedule service reject task");
    }

    @Bean("common_async")
    public Executor commonAsync() {
        CustomThreadPoolExecutor customThreadPoolExecutor = (CustomThreadPoolExecutor) CommonUtil.getFixThreadPoolSizeThreadPool(
                5, 50, r -> {
                    Thread thread = new Thread(r);
                    thread.setName("-common_async-");
                    thread.setUncaughtExceptionHandler((t, e) -> log.error("error of common async service ",e));
                    return thread;
                }
                , "common async service", (r, executor) -> log.error("common async service reject  task"));
        ThreadPoolUtil.registerThreadPoolExecutor(customThreadPoolExecutor);
        return customThreadPoolExecutor;
    }

    @Bean("common_service")
    public Executor taskExecutor1() {
        return generateCommonThreadPool("-common-service-", "error of common service service ", "common service service reject task");
    }

    @Bean("device_schedule_service")
    public Executor deviceScheduleService(){
        return generateCommonThreadPool("-device-schedule-service-", "error of device schedule service ", "device schedule service reject task");
    }

    private CustomThreadPoolExecutor generateCommonThreadPool(String threadName, String threadErrorMsg, String rejectErrorMsg) {
        CustomThreadPoolExecutor customThreadPoolExecutor = (CustomThreadPoolExecutor) CommonUtil.getFixThreadPoolSizeThreadPool(50, r -> {
            Thread thread = new Thread(r);
            thread.setName(threadName);
            thread.setUncaughtExceptionHandler((t, e) -> log.error(threadErrorMsg,e));
            return thread;
        },threadName, (r, executor) -> log.error(rejectErrorMsg));
        ThreadPoolUtil.registerThreadPoolExecutor(customThreadPoolExecutor);
        return customThreadPoolExecutor;
    }

    @Bean("d2d_schedule_service")
    public Executor d2dScheduleService(){
        CustomThreadPoolExecutor customThreadPoolExecutor = (CustomThreadPoolExecutor) CommonUtil.getFixThreadPoolSizeThreadPool(10, r -> {
            Thread thread = new Thread(r);
            thread.setName("-d2d-schedule-service-");
            thread.setUncaughtExceptionHandler((t, e) -> System.err.println("error of d2d schedule service " + e));
            return thread;
        }, "d2d_schedule_service", (r, executor) -> {
            log.error("d2d_schedule_service reject task");
        });
        ThreadPoolUtil.registerThreadPoolExecutor(customThreadPoolExecutor);
        return customThreadPoolExecutor;
    }

    @Bean("top5_schedule_service")
    public Executor top5ScheduleService(){
        return generateCommonThreadPool("-top5-schedule-service-",
                "error of top5 schedule service ","top5 schedule service reject task");
    }
}
