package com.zjucsc.application.task;

import com.zjucsc.IProtocolFuncodeMap;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.domain.analyzer.EmptyPacketAnalyzer;
import com.zjucsc.application.domain.bean.FuncodeStatement;
import com.zjucsc.application.domain.filter.EmptyFilter;
import com.zjucsc.application.domain.filter.OtherPacketFilter;
import com.zjucsc.application.domain.analyzer.OtherPacketAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 加载组态配置到内存中 + 保存到本地数据库
 */
@Slf4j
@Component
public class InitConfigurationService implements ApplicationRunner {

    private final String str_name = "java.lang.String";
    private final String int_name = "int";

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, NoSuchFieldException {
        /*
         * INIT ALL FUN_CODE MAP
         */
        ServiceLoader<IProtocolFuncodeMap> serviceLoader = ServiceLoader.load(IProtocolFuncodeMap.class);
        for (IProtocolFuncodeMap iProtocolFuncodeMap : serviceLoader) {
            HashMap<Integer,String> funcodeStatements = new HashMap<>();
            String protocolName = iProtocolFuncodeMap.protocolAnalyzerName();
            Map<Integer,String> map = iProtocolFuncodeMap.initProtocol();
            log.info("load configuration : {} \n {} " , protocolName , map);
            for (int fun_code : map.keySet()){
                funcodeStatements.put(fun_code,map.get(fun_code));
            }
            Common.CONFIGURATION_MAP.put(protocolName,funcodeStatements);
        }

        /*
         * INIT PROTOCOL STR TO INT
         */
        Class<PACKET_PROTOCOL> packet_protocolClass = PACKET_PROTOCOL.class;
        Field[] allField = packet_protocolClass.getDeclaredFields();
        for (Field field : allField) {
            if (field.getType().getTypeName().equals(str_name)){
                String protocol_name = (String) field.get(null);
                int protocol_id = (int) packet_protocolClass.getDeclaredField(field.getName() + "_ID").get(null);
                Common.PROTOCOL_STR_TO_INT.put(protocol_id,protocol_name);
                /*
                 * 如果 BAD_PACKET_FILTER_PRO_1 初始化中不包含已定义的协议，那么就添加一个OtherPacketFilter防止NullPointerException
                 * 也可以统一对这些OTHER的报文作统一分析
                 */
                if (!Common.BAD_PACKET_FILTER_PRO_1.containsKey(protocol_name)){
                    Common.BAD_PACKET_FILTER_PRO_1.put(protocol_name,new EmptyPacketAnalyzer(new EmptyFilter(),protocol_name + "-empty filter"));
                }
            }
        }
        System.out.println("bad packet filter pro map : \n" + Common.BAD_PACKET_FILTER_PRO_1);
        System.out.println("load Common.PROTOCOL_STR_TO_INT : \n" + Common.PROTOCOL_STR_TO_INT);
        System.out.println("load Common.CONFIGURATION_MAP : \n" + Common.CONFIGURATION_MAP);
    }
}
