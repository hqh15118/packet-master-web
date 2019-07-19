package com.zjucsc;

import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.config.PreProcessor;
import com.zjucsc.application.util.TsharkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
@EnableConfigurationProperties({ConstantConfig.class, PreProcessor.class})
@Slf4j
public class PacketMasterWebApplication{

    public static void main(String[] args) throws IOException {
        String str = TsharkUtil.checkTsharkValid();
        if (str == null) {
            System.err.println("tshark is not in system PATH , application failed to start");
            return;
        }else{
            TsharkUtil.setTsharkPath(str);
            System.out.println("**************\nfind tshark in: " + str + " \napplication start now >>>\n**************");
        }
        try {
            if(!TsharkUtil.addTsharkPlugin()){
                System.err.println("无法自动创建【tshark插件】，请检查权限或者手动添加到wireshark/plugins目录下");
                return;
            }
        } catch (IOException e) {
            System.err.println("无法自动创建【tshark插件】，请检查权限或者手动添加到wireshark/plugins目录下");
            log.error("创建tshark插件失败***",e);
            return;
        }
        SpringApplication.run(PacketMasterWebApplication.class, args);
    }
}
