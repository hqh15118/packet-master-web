package com.zjucsc.application.task;

import com.zjucsc.IProtocolFuncodeMap;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.domain.analyzer.EmptyPacketAnalyzer;
import com.zjucsc.application.domain.bean.ConfigurationForNewProtocol;
import com.zjucsc.application.domain.bean.ConfigurationForFront;
import com.zjucsc.application.domain.filter.EmptyFilter;
import com.zjucsc.application.system.controller.ConfigurationController;
import com.zjucsc.application.system.entity.Configuration;
import com.zjucsc.application.system.entity.ProtocolId;
import com.zjucsc.application.system.service.IProtocolIdService;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * 加载组态配置到内存中 + 保存到本地数据库
 */
@Slf4j
@Component
public class InitConfigurationService implements ApplicationRunner {

    private final String str_name = "java.lang.String";
    private final String int_name = "int";

    @Autowired private ConfigurationController configurationController;
    @Autowired private ConfigurationService configurationService;
    @Autowired private IProtocolIdService iProtocolIdService;

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, NoSuchFieldException {

        /*
         * INIT PROTOCOL STR TO INT
         */
        //如果数据库中没有条目，就从代码中加载之前配置好的，如果数据库中有条目，
        //就直接使用数据库中的条目初始化 PROTOCOL_STR_TO_INT ， 用户协议ID和协议字符串之间的转换
        if (iProtocolIdService.list().size() == 0){
            List<ProtocolId> protocolIds = new ArrayList<>();
            Class<PACKET_PROTOCOL> packet_protocolClass = PACKET_PROTOCOL.class;
            Field[] allField = packet_protocolClass.getDeclaredFields();
            for (Field field : allField) {
                if (field.getType().getTypeName().equals(str_name)){
                    String protocol_name = (String) field.get(null);
                    int protocol_id = (int) packet_protocolClass.getDeclaredField(field.getName() + "_ID").get(null);
                    Common.PROTOCOL_STR_TO_INT.put(protocol_id,protocol_name);
                    protocolIds.add(new ProtocolId(protocol_id , protocol_name));
                }
            }
            iProtocolIdService.saveOrUpdateBatch(protocolIds);
        }else{
            List<ProtocolId> list = iProtocolIdService.list();
            for (ProtocolId protocolId : list) {
                Common.PROTOCOL_STR_TO_INT.put(protocolId.getProtocolId() , protocolId.getProtocolName());
            }
        }

        if(configurationService.list().size() == 0) {
            log.info("no configuration in database and ready to load from libs ... ");
            /*
             * INIT ALL FUN_CODE MAP
             */
            ServiceLoader<IProtocolFuncodeMap> serviceLoader = ServiceLoader.load(IProtocolFuncodeMap.class);
            ArrayList<ConfigurationForNewProtocol> list = new ArrayList<>();
            for (IProtocolFuncodeMap iProtocolFuncodeMap : serviceLoader) {
                HashMap<Integer, String> funcodeStatements = new HashMap<>();
                String protocolName = iProtocolFuncodeMap.protocolAnalyzerName();
                Map<Integer, String> map = iProtocolFuncodeMap.initProtocol();
                log.info("load configuration : {} \n {} ", protocolName, map);
                ConfigurationForNewProtocol configurationForNewProtocol = new ConfigurationForNewProtocol();
                List<ConfigurationForFront.ConfigurationWrapper> wrappers = new ArrayList<>();
                for (int fun_code : map.keySet()) {
                    funcodeStatements.put(fun_code, map.get(fun_code));
                    wrappers.add(new ConfigurationForFront.ConfigurationWrapper(fun_code , map.get(fun_code)));
                }
                configurationForNewProtocol.setConfigurationWrappers(wrappers);
                configurationForNewProtocol.setProtocolName(protocolName);
                list.add(configurationForNewProtocol);
                Common.CONFIGURATION_MAP.put(protocolName, funcodeStatements);
            }
            log.info(" load from libs successfully : \n {} " , Common.CONFIGURATION_MAP);
            configurationController.addConfiguration(list);
        }else{
            for (Configuration configuration : configurationService.list()) {
                Common.CONFIGURATION_MAP.computeIfAbsent(Common.PROTOCOL_STR_TO_INT.get(configuration.getProtocol_id()),
                        new Function<String, HashMap<Integer, String>>() {
                            @Override
                            public HashMap<Integer, String> apply(String s) {
                                HashMap<Integer,String> map = new HashMap<>();
                                map.put(configuration.getProtocol_id(),s);
                                return map;
                            }
                        });
                Common.CONFIGURATION_MAP.get(Common.PROTOCOL_STR_TO_INT.get(configuration.getProtocol_id()))
                        .put(configuration.getProtocol_id() , configuration.getOpt());
            }
        }
        /*
         * INIT AUTH
         */
        Field[] allField;
        Class<Auth> authClass = Auth.class;
        allField = authClass.getDeclaredFields();
        for (Field field : allField) {
            if (field.getType().getTypeName().equals(str_name)){
                String authName = (String) field.get(null);
                int auth = (int) authClass.getDeclaredField(field.getName() + "_ID").get(null);
                Common.AUTH_MAP.put(auth,authName);
            }
        }
        System.out.println("load Common.PROTOCOL_STR_TO_INT : \n" + Common.PROTOCOL_STR_TO_INT);
        System.out.println("load Common.AUTH_MAP : \n" + Common.AUTH_MAP);
    }
}
