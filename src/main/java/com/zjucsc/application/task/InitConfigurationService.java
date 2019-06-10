package com.zjucsc.application.task;

import com.zjucsc.application.config.ProtocolIgnore;
import com.zjucsc.application.domain.bean.ConfigurationSetting;
import com.zjucsc.application.system.service.hessian_iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.hessian_iservice.IProtocolIdService;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.artdecoder.S7CommDecode;
import com.zjucsc.art_decode.base.IArtDecode;
import com.zjucsc.IProtocolFuncodeMap;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.domain.bean.Protocol;
import com.zjucsc.application.tshark.analyzer.ArtAnalyzer;
import com.zjucsc.application.util.CommonCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

import static com.zjucsc.application.config.Common.ART_FILTER;
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
    @Autowired private ConstantConfig constantConfig;

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, NoSuchFieldException, ProtocolIdNotValidException {
        /***************************
         * RELOAD FROM JAR
         ***************************/
        List<String> virReload = args.getOptionValues("reload");
        System.out.println("*******************\n" + "program args : " + virReload + "\n");
        boolean reload = false;
        if (virReload!=null && virReload.size() > 0){
            if ("true".equals(virReload.get(0))){
                //重新到jar包中加载功能码含义
                reload = true;
                log.info("force reload [funCode Meaning]from jar file");
            }
        }

        /***************************
         * IP OR MAC_ADDRESS
         ***************************/
        List<String> virFilterStatement = args.getOptionValues("statement");
        System.out.println("program args : " + virFilterStatement + "\n*******************");
        if (virFilterStatement!=null && virFilterStatement.size() > 0){
            Common.filterStatement = Integer.parseInt(virFilterStatement.get(0));
            if (Common.filterStatement == 0){
                log.info("以[IP]地址作为准则进行过滤器的设置");
            }else{
                log.info("以[MAC]地址作为准则进行过滤器的设置");
            }
        }

        /***************************
         * INIT PROTOCOL STR TO INT
         ***************************/
        //如果数据库中没有条目，就从代码中加载之前配置好的，如果数据库中有条目，
        //就判断是否需要重新加载使用数据库中的条目初始化 PROTOCOL_STR_TO_INT ， 用户协议ID和协议字符串之间的转换
        if (iProtocolIdService.selectAll().size() == 0 ){
            List<Protocol> protocols = new ArrayList<>();
            Class<PACKET_PROTOCOL> packet_protocolClass = PACKET_PROTOCOL.class;
            Field[] allField = packet_protocolClass.getDeclaredFields();
            for (Field field : allField) {
                field.setAccessible(true);
                if (field.getType().getTypeName().equals(str_name) && field.getAnnotation(ProtocolIgnore.class) == null){
                    String protocol_name = (String) field.get(null);
                    int protocol_id = (int) packet_protocolClass.getDeclaredField(field.getName() + "_ID").get(null);
                    Common.PROTOCOL_STR_TO_INT.put(protocol_id,protocol_name);
                    protocols.add(new Protocol(protocol_id , protocol_name));
                }
            }
            iProtocolIdService.saveOrUpdateBatch(protocols);
        }else{
            List<Protocol> list = iProtocolIdService.selectAll();
            for (Protocol protocol : list) {
                Common.PROTOCOL_STR_TO_INT.put(protocol.getProtocolId() , protocol.getProtocolName());
            }
        }

        if(reload || iConfigurationSettingService.selectAll().size() == 0) {
            if(!reload) {
                log.info("no configuration in database and ready to load from libs ... ");
            }
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
                iConfigurationSettingService.saveOrUpdateBatch(configurationSettings);
                addProtocolFuncodeMeaning(protocolName,funcodeStatements);
            }
        }else{
            for (ConfigurationSetting configuration : iConfigurationSettingService.selectAll()) {
                addProtocolFuncodeMeaning(convertIdToName(configuration.getProtocolId()),
                        configuration.getFunCode(),configuration.getOpt());
            }
        }

        /***************************
         * INIT AUTH
         ***************************/
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

        /***************************
         * INIT ART_ANALYZER
         ***************************/
        Map<String,IArtDecode> artServiceMap = new HashMap<>();
        S7CommDecode s7commDecode = new S7CommDecode();
        artServiceMap.put(s7commDecode.protocol(),s7commDecode);
        ART_FILTER = new ArtAnalyzer(artServiceMap);

        /***************************
         * INIT ART DECODER
         ***************************/
        ArtDecodeCommon.init();

        /**************************
         *  PRINT INIT RESULT
         ***************************/
        log.info("\n******************** \n AUTH_MAP : {} \n********************" , Common.AUTH_MAP);
        log.info("\n******************** \n size : {} ; CONFIGURATION_MAP : {}\n********************" ,  Common.CONFIGURATION_MAP.size() , Common.CONFIGURATION_MAP);
        log.info("\n******************** \n size : {} ; PROTOCOL_STR_TO_INT : {} \n********************" , Common.PROTOCOL_STR_TO_INT.size() ,  Common.PROTOCOL_STR_TO_INT  );
        log.info("\n******************** \n size : {} ; ART_FILTER MAP: {} \n********************" , ART_FILTER.getAnalyzer().size() , ART_FILTER.getAnalyzer());

        System.out.println("spring boot admin address : http://your-address:8989");
        System.out.println("swagger-ui address : http://your-address:your-port/swagger-ui.html#/greeting-controller");
    }
}
