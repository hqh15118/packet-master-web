package com.zjucsc;

import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.config.PreProcessor;
import com.zjucsc.application.util.TsharkUtil;
import com.zjucsc.common.bean.CustomThreadPoolExecutor;
import com.zjucsc.common.common_util.CommonUtil;
import com.zjucsc.common.common_util.PrinterUtil;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

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
        String str = TsharkUtil.checkTsharkValid();
        if (str == null) {
            System.err.println("tshark is not in system PATH , application failed to start");
            return;
        }else{
            TsharkUtil.setTsharkPath(str);
            PrinterUtil.printMsg(0,"find tshark in: " + str);
        }
        checkWiresharkTempPath();
        try {
            if(!TsharkUtil.addTsharkPlugin()){
                PrinterUtil.printError("无法自动创建【tshark插件】，请检查权限或者手动添加到wireshark/plugins目录下");
                return;
            }
        } catch (IOException e) {
            PrinterUtil.printError("无法自动创建【tshark插件】，请检查权限或者手动添加到wireshark/plugins目录下");
            log.error("创建tshark插件失败***",e);
            return;
        }
        SpringApplication.run(PacketMasterWebApplication.class, args);
    }

    private static void checkWiresharkTempPath() {
//        String tempPath = System.getenv("TEMP");
//        if (!"C:\\temp".equals(tempPath)){
//            throw new RuntimeException("TEMP系统目录不存在，或者不为C:\\temp,请重新运行脚本文件！");
//        }
    }
}
