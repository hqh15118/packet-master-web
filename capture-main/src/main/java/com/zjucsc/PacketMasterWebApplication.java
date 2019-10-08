package com.zjucsc;

import com.zjucsc.application.config.properties.ConstantConfig;
import com.zjucsc.application.config.properties.PreProcessor;
import com.zjucsc.common.common_util.PrinterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
@EnableConfigurationProperties({ConstantConfig.class, PreProcessor.class})
@Slf4j
public class PacketMasterWebApplication{

    public static void main(String[] args) {
//        CommonUtil.registerExceptionHandler((r, executor) -> {
//            //SocketServiceCenter.updateAllClient(SocketIoEvent.TASK_QUEUE_OVER_FLOW,executor.);
//            System.err.println(((CustomThreadPoolExecutor) executor).getTag());
//        });
        String attention = "运行该程序前请运行一遍脚本文件，并检查用户环境变量【TEMP】";
        PrinterUtil.printMsg(2,attention);

        SpringApplication.run(PacketMasterWebApplication.class, args);
    }

    private static void checkWiresharkTempPath() {
//        String tempPath = System.getenv("TEMP");
//        if (!"C:\\temp".equals(tempPath)){
//            throw new RuntimeException("TEMP系统目录不存在，或者不为C:\\temp,请重新运行脚本文件！");
//        }
    }
}
