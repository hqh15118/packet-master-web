package com.zjucsc.application.task;

import com.zjucsc.IProtocolFuncodeMap;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.ConfigurationSetting;
import com.zjucsc.application.system.entity.ProtocolId;
import com.zjucsc.application.system.service.iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.iservice.IProtocolIdService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

import static com.zjucsc.application.util.CommonCacheUtil.convertIdToName;
import static com.zjucsc.application.util.CommonConfigUtil.addProtocolFuncodeMeaning;

/**
 * 加载组态配置到内存中 + 保存到本地数据库
 */
@Slf4j
@Component
public class InitConfigurationService implements ApplicationRunner {

    private final String str_name = "java.lang.String";

    @Autowired private IConfigurationSettingService iConfigurationSettingService;
    @Autowired private IProtocolIdService iProtocolIdService;

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, NoSuchFieldException, ProtocolIdNotValidException {

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
                    CommonConfigUtil.addProtocolFuncodeMeaning(protocol_name,new HashMap<>());
                    protocolIds.add(new ProtocolId(protocol_id , protocol_name));
                }
            }
            iProtocolIdService.saveOrUpdateBatch(protocolIds);
        }else{
            List<ProtocolId> list = iProtocolIdService.list();
            for (ProtocolId protocolId : list) {
                Common.PROTOCOL_STR_TO_INT.put(protocolId.getProtocolId() , protocolId.getProtocolName());
                CommonConfigUtil.addProtocolFuncodeMeaning(protocolId.getProtocolName(),new HashMap<>());
            }
        }
        if(iConfigurationSettingService.list().size() == 0) {
            log.info("no configuration in database and ready to load from libs ... ");
            /*
             * INIT ALL FUN_CODE MAP
             */
            ServiceLoader<IProtocolFuncodeMap> serviceLoader = ServiceLoader.load(IProtocolFuncodeMap.class);
            for (IProtocolFuncodeMap iProtocolFuncodeMap : serviceLoader) {
                HashMap<Integer, String> funcodeStatements = new HashMap<>();
                String protocolName = iProtocolFuncodeMap.protocolAnalyzerName();
                Map<Integer, String> map = iProtocolFuncodeMap.initProtocol();
                List<ConfigurationSetting> configurationSettings = new ArrayList<>();
                for (int fun_code : map.keySet()) {
                    funcodeStatements.put(fun_code, map.get(fun_code));
                    //添加到protocol_id表
                    ConfigurationSetting configurationSetting = new ConfigurationSetting();
                    int protocolId = CommonCacheUtil.convertNameToId(protocolName);
                    configurationSetting.setProtocolId(protocolId);
                    configurationSetting.setOpt(map.get(fun_code));
                    configurationSetting.setFunCode(fun_code);
                    configurationSettings.add(configurationSetting);
                }
                iConfigurationSettingService.saveBatch(configurationSettings);
                addProtocolFuncodeMeaning(protocolName,funcodeStatements);
            }
        }else{
            for (ConfigurationSetting configuration : iConfigurationSettingService.list()) {
                addProtocolFuncodeMeaning(convertIdToName(configuration.getProtocolId()),
                        configuration.getFunCode(),configuration.getOpt());
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
        log.info("AUTH_MAP : {} " , Common.AUTH_MAP);
        log.info("size : {} ; CONFIGURATION_MAP : {}  " ,  Common.CONFIGURATION_MAP.size() , Common.CONFIGURATION_MAP);
        log.info("size : {} ; PROTOCOL_STR_TO_INT : {} " , Common.PROTOCOL_STR_TO_INT.size() ,  Common.PROTOCOL_STR_TO_INT  );
    }
}
