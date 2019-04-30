package com.zjucsc.application.task;

import com.zjucsc.IProtocolFuncodeMap;
import com.zjucsc.application.config.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * 加载组态配置到内存中
 */
@Slf4j
public class InitConfigurationService implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        ServiceLoader<IProtocolFuncodeMap> serviceLoader = ServiceLoader.load(IProtocolFuncodeMap.class);
        for (IProtocolFuncodeMap iProtocolFuncodeMap : serviceLoader) {
            String protocolName = iProtocolFuncodeMap.protocolAnalyzerName();
            Map<Integer,String> map = iProtocolFuncodeMap.initProtocol();
            log.info("load configuration : {} \n {} " , protocolName , map);
            Common.CONFIGURATION_MAP.put(protocolName,map);
        }
    }
}
