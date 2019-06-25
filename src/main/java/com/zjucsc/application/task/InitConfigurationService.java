package com.zjucsc.application.task;

import com.zjucsc.IProtocolFuncodeMap;
import com.zjucsc.application.config.*;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.ConfigurationSetting;
import com.zjucsc.application.domain.bean.PagedArtConfig;
import com.zjucsc.application.domain.bean.Protocol;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.system.service.hessian_iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.hessian_iservice.IProtocolIdService;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.artconfig.IEC104Config;
import com.zjucsc.art_decode.artconfig.PnioConfig;
import com.zjucsc.art_decode.artconfig.S7Config;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.common.AttackCommon;
import com.zjucsc.tshark.TsharkCommon;
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

    @Autowired private IArtConfigService iArtConfigService;
    @Autowired private IConfigurationSettingService iConfigurationSettingService;
    @Autowired private IProtocolIdService iProtocolIdService;

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, NoSuchFieldException, ProtocolIdNotValidException {
        /***************************
         * RELOAD FROM JAR
         ***************************/
        List<String> virReload = args.getOptionValues("reload");
        System.out.println("*******************\n" + "program args [reload]: " + virReload);
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
        System.out.println("program args : [filterStatement]" + virFilterStatement + "\n*******************");
        if (virFilterStatement!=null && virFilterStatement.size() > 0){
            Common.filterStatement = Integer.parseInt(virFilterStatement.get(0));
            if (Common.filterStatement == 0){
                log.info("以[IP]地址作为准则进行过滤器的设置");
            }else{
                log.info("以[MAC]地址作为准则进行过滤器的设置");
            }
        }
        /***************************
         * TSHARK PRE_PROCESSOR
         ***************************/
        List<String> virPreProcessor = args.getOptionValues("processor");
        System.out.println("program args [pre_processor]: " + virPreProcessor + "\n*******************");
        if (virPreProcessor!=null && virPreProcessor.size() > 0){
            Common.TSHARK_PRE_PROCESSOR_PROTOCOLS.addAll(virPreProcessor);
        }

        /***************************
         * INIT FILTER
         ***************************/
        List<String> virFilter = args.getOptionValues("filter");
        System.out.println("program args : " + virFilter + "\n*******************");
        if (virFilter!=null && virFilter.size() > 0){
            TsharkCommon.filter = virFilter.get(0);
        }
        /***************************
         * INIT -M <Tshark Session Reset>
         ***************************/
        List<String> virSessionReset = args.getOptionValues("session");
        System.out.println("program args : " + virSessionReset + "\n*******************");
        if (virSessionReset!=null && virSessionReset.size() > 0){
            TsharkCommon.sessionReset = virSessionReset.get(0);
        }

        /***************************
         * INIT PROTOCOL STR TO INT
         ***************************/
        //如果数据库中没有条目，就从代码中加载之前配置好的，如果数据库中有条目，
        //就判断是否需要重新加载使用数据库中的条目初始化 PROTOCOL_STR_TO_INT ， 用户协议ID和协议字符串之间的转换
        List<Protocol> protocols = new ArrayList<>();
        if (iProtocolIdService.selectAll().size() == 0 ){
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
            protocols = iProtocolIdService.selectAll();
            for (Protocol protocol : protocols) {
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
         * INIT ART DECODER
         ***************************/
        ArtDecodeCommon.init();
        /***************************
         * 初始化工艺参数配置
         **************************/
        PagedArtConfig pagedArtConfig = new PagedArtConfig();
        pagedArtConfig.setPage(1);
        pagedArtConfig.setLimit(999);
        pagedArtConfig.setTag("");
        List<Integer> protocolIds = Arrays.asList(PACKET_PROTOCOL.MODBUS_ID,PACKET_PROTOCOL.S7_ID);
        for (Integer protocolId : protocolIds) {
            pagedArtConfig.setProtocolId(protocolId);
            BaseResponse baseResponse = iArtConfigService.getConfigPaged(pagedArtConfig);
            if (baseResponse.data!=null){
                List datas = (List) baseResponse.data;
                for (Object data : datas) {
                    BaseConfig baseConfig = ((BaseConfig) data);
                    if (baseConfig.getProtocolId() == PACKET_PROTOCOL.S7_ID){
                        baseConfig.setProtocol("s7comm");
                    }else {
                        baseConfig.setProtocol(CommonCacheUtil.convertIdToName(baseConfig.getProtocolId()));
                    }
                    ArtDecodeCommon.addArtDecodeConfig(baseConfig);
                    AppCommonUtil.initArtMap(baseConfig.getTag());
                }
            }
        }

        /***************************
         * pn_io
         **************************/
//        PnioConfig pnioConfig = new PnioConfig();
//        pnioConfig.setRange(new float[]{0f,120f});
//        pnioConfig.setByteoffset(3);
//        pnioConfig.setBitoffset(0);
//        pnioConfig.setType("short");
//        pnioConfig.setLength(2);
//        pnioConfig.setProtocol(PACKET_PROTOCOL.PN_IO);
//        pnioConfig.setMacaddress(new byte[]{0x28,0x63,0x36,(byte)0xef,0x31,(byte)0xcc});
//        pnioConfig.setTag("test");
//        AppCommonUtil.initArtMap(pnioConfig.getTag());
//        CommonCacheUtil.addShowGraphArg(pnioConfig.getProtocolId(),pnioConfig.getTag());
//        ArtDecodeCommon.addArtDecodeConfig(pnioConfig);
        /***************************
         * IEC104
         **************************/
//        IEC104Config iec104Config = new IEC104Config();
//        iec104Config.setMVIOAAddress(16385);
//        //iec104Config.setSetIOAAddress(24592);
//        iec104Config.setTag("UA");
//        iec104Config.setProtocol(PACKET_PROTOCOL.IEC104);
//        iec104Config.setProtocolId(PACKET_PROTOCOL.IEC104_ID);
//        AppCommonUtil.initArtMap(iec104Config.getTag());
//        CommonCacheUtil.addShowGraphArg(iec104Config.getProtocolId(),iec104Config.getTag());
//        ArtDecodeCommon.addArtDecodeConfig(iec104Config);

        /***************************
         * s7comm test
         **************************/
        /*
        S7Config s7Config = new S7Config();
        s7Config.setBitoffset(0);
        s7Config.setByteoffset(6);
        s7Config.setDatabase(1);
        s7Config.setLength(4);
        s7Config.setTag("水位1");
        s7Config.setType("float");
        s7Config.setShowGraph(1);
        s7Config.setProtocol(PACKET_PROTOCOL.S7);
        AppCommonUtil.initArtMap(s7Config.getTag());
        CommonCacheUtil.addShowGraphArg(s7Config.getProtocolId(),s7Config.getTag());
        ArtDecodeCommon.addArtDecodeConfig(s7Config);

        S7Config s7Config1 = new S7Config();
        s7Config1.setBitoffset(1);
        s7Config1.setByteoffset(0);
        s7Config1.setDatabase(2);
        s7Config1.setLength(0);
        s7Config1.setType("bool");
        s7Config1.setTag("开关1");
        s7Config1.setProtocol(PACKET_PROTOCOL.S7);
        AppCommonUtil.initArtMap(s7Config1.getTag());
        CommonCacheUtil.addShowGraphArg(s7Config1.getProtocolId(),s7Config1.getTag());
        ArtDecodeCommon.addArtDecodeConfig(s7Config1);
        */
        /*
        List<String> list = new ArrayList<String>()
        {
            {
                add("水位1");
                add("<");
                add("100");
                add("&&");
                add("开关1");
                add("=");
                add("1");
            }
        };
        */

        //init art detection
        //AttackCommon.addArtAttackAnalyzeConfig(new ArtAttackAnalyzeConfig(list,"test - -- - - "));

        /***************************
         * INIT PROTOCOL COMMON
         * 1. IEC104
         * 2.
         **************************/
        ProtocolCommon.init();

        /**************************
         *  PRINT INIT RESULT
         ***************************/
        log.info("\n******************** \n AUTH_MAP : {} \n********************" , Common.AUTH_MAP);
        log.info("\n******************** \n size : {} ; CONFIGURATION_MAP : {}\n********************" ,  Common.CONFIGURATION_MAP.size() , Common.CONFIGURATION_MAP);
        log.info("\n******************** \n size : {} ; PROTOCOL_STR_TO_INT : {} \n********************" , Common.PROTOCOL_STR_TO_INT.size() ,  Common.PROTOCOL_STR_TO_INT  );
        log.info("\n******************** \n size : {} ; ALL_ART_CONFIG : {} \n********************",ArtDecodeCommon.getAllArtConfigs().size(),ArtDecodeCommon.getAllArtConfigs());
        System.out.println("spring boot admin address : http://your-address:8989");
        System.out.println("swagger-ui address : http://your-address:your-port/swagger-ui.html#/greeting-controller");
    }
}
