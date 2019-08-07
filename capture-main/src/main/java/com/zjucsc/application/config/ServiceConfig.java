package com.zjucsc.application.config;

import com.zjucsc.application.util.ThreadPoolUtil;
import com.zjucsc.common.bean.CustomThreadPoolExecutor;
import com.zjucsc.common.common_util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ServiceConfig {

    @Bean("common_schedule")
    public Executor taskExecutor() {
        CustomThreadPoolExecutor customThreadPoolExecutor = generateCommonThreadPool("-common-schedule-", "error of common schedule service ", "common schedule service reject task");
        ThreadPoolUtil.registerThreadPoolExecutor(customThreadPoolExecutor);
        return customThreadPoolExecutor;
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

    @Bean("device_schedule_service")
    public Executor deviceScheduleService(){
        CustomThreadPoolExecutor customThreadPoolExecutor = generateCommonThreadPool("-device-schedule-service-", "error of device schedule service ", "device schedule service reject task");;
        ThreadPoolUtil.registerThreadPoolExecutor(customThreadPoolExecutor);
        return customThreadPoolExecutor;
    }

    private CustomThreadPoolExecutor generateCommonThreadPool(String s, String threadErrorMsg, String rejectErrorMsg) {
        CustomThreadPoolExecutor customThreadPoolExecutor = (CustomThreadPoolExecutor) CommonUtil.getFixThreadPoolSizeThreadPool(50, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(s);
                thread.setUncaughtExceptionHandler((t, e) -> log.error(threadErrorMsg,e));
                return thread;
            }
        }, s, (r, executor) -> log.error(rejectErrorMsg));
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
        CustomThreadPoolExecutor customThreadPoolExecutor = generateCommonThreadPool("-top5-schedule-service-",
                "error of top5 schedule service ","top5 schedule service reject task");
        ThreadPoolUtil.registerThreadPoolExecutor(customThreadPoolExecutor);
        return customThreadPoolExecutor;
    }
}
