package com.zjucsc.application.task;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.listener.DataListener;
import com.zjucsc.IProtocolFuncodeMap;
import com.zjucsc.application.config.*;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.system.service.common_impl.NetworkInterfaceServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.*;
import com.zjucsc.application.system.service.hessian_mapper.DeviceMaxFlowMapper;
import com.zjucsc.application.system.service.hessian_mapper.DosConfigMapper;
import com.zjucsc.application.system.service.hessian_mapper.PacketInfoMapper;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.base.util.SpringContextUtil;
import com.zjucsc.common.common_util.PrinterUtil;
import com.zjucsc.common.exceptions.PacketDetailServiceNotValidException;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.socket_io.*;
import com.zjucsc.tshark.TsharkCommon;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.*;

import static com.zjucsc.application.util.CommonCacheUtil.AUTH_MAP;
import static com.zjucsc.application.util.CommonCacheUtil.PROTOCOL_STR_TO_INT;
import static com.zjucsc.application.util.CommonCacheUtil.convertIdToName;
import static com.zjucsc.application.util.CommonConfigUtil.addProtocolFuncodeMeaning;
import static com.zjucsc.application.util.TsharkUtil.startHistoryPacketAnalyzeThread;

/**
 * 加载组态配置到内存中 + 保存到本地数据库
 */
@Slf4j
@Component
@DataSourceClass({SocketIoConfig.class})
public class InitConfigurationService implements ApplicationRunner {

    @Autowired private IArtConfigService iArtConfigService;
    @Autowired private IConfigurationSettingService iConfigurationSettingService;
    @Autowired private IProtocolIdService iProtocolIdService;
    @Autowired private TsharkConfig tsharkConfig;
    @Autowired private PacketInfoMapper packetInfoMapper;
    @Autowired private ConstantConfig constantConfig;
    @Autowired private PreProcessor preProcessor;
    @Autowired private DeviceMaxFlowMapper deviceMaxFlowMapper;
    @Autowired private IGplotService iGplotService;

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, NoSuchFieldException, ProtocolIdNotValidException, IOException {
        /*
        List<String> virTempPath = args.getOptionValues("temp");
        //设置临时文件夹路径
        String command;
        if (virTempPath == null) {
            command = "md C:\\temp\n" +
                    "setx TEMP C:\\temp";
            System.out.println("*******************\\n\" + \"program args [temp<--temp>]: \" + C:\\temp");
        }else{
            command = "md " + virTempPath.get(0) + " \n" + "setx TEMP " + virTempPath.get(0);
            System.out.println("*******************\\n\" + \"program args [temp<--temp>]: \" + " + virTempPath.get(0));
        }
        Runtime.getRuntime().exec(command);
        */
        /***************************
         * RELOAD FROM JAR
         ***************************/
        List<String> virReload = args.getOptionValues("reload");
        PrinterUtil.printStarter();
        PrinterUtil.printMsg("program args [reload <--reload>]: " + virReload);
        boolean reload = false;
        if (virReload!=null && virReload.size() > 0){
            if ("true".equals(virReload.get(0))){
                //重新到jar包中加载功能码含义
                reload = true;
            }
        }

        /***************************
         * IP OR MAC_ADDRESS
         ***************************/
        List<String> virFilterStatement = args.getOptionValues("statement");
        PrinterUtil.printMsg("program args : [filterStatement <--statement>]" + virFilterStatement);
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
        PrinterUtil.printMsg("program args [pre_processor<--processor>]: " + virPreProcessor);
        if (virPreProcessor!=null && virPreProcessor.size() > 0){
            Common.TSHARK_PRE_PROCESSOR_PROTOCOLS.addAll(virPreProcessor);
        }

        /****************************
         * 演示/真实场景
         ***************************/
        List<String> virType = args.getOptionValues("type");
        PrinterUtil.printMsg("program args [type--type]: " + virType);
        if (virType!=null && virType.size() > 0){
            Common.systemRunType = Integer.valueOf(virType.get(0));
        }

        /***************************
         * INIT FILTER
         ***************************/
        List<String> virFilter = args.getOptionValues("filter");
        PrinterUtil.printMsg("program args : [filter<--filter>]" + virFilter);
        if (virFilter!=null && virFilter.size() > 0){
            //通用filter，特殊的filter如S7Common-Filter/Modbus-Filter会覆盖这个通用的Filter
            TsharkCommon.filter = virFilter.get(0);
        }
        /***************************
         * INIT -M <Tshark Session Reset>
         ***************************/
        List<String> virSessionReset = args.getOptionValues("session");
        PrinterUtil.printMsg("program args : [session reset<--session>]" + virSessionReset);
        if (virSessionReset!=null && virSessionReset.size() > 0){
            TsharkCommon.sessionReset = virSessionReset.get(0);
        }
        PrinterUtil.printEnder();

        /***************************
         * INIT PROTOCOL STR TO INT
         ***************************/
        //如果数据库中没有条目，就从代码中加载之前配置好的，如果数据库中有条目，
        //就判断是否需要重新加载使用数据库中的条目初始化 PROTOCOL_STR_TO_INT ， 用户协议ID和协议字符串之间的转换
        List<Protocol> protocols = new ArrayList<>();
        String str_name = "java.lang.String";
        if (iProtocolIdService.selectAll().size() == 0 ){
            Class<PACKET_PROTOCOL> packet_protocolClass = PACKET_PROTOCOL.class;
            Field[] allField = packet_protocolClass.getDeclaredFields();
            for (Field field : allField) {
                field.setAccessible(true);
                if (field.getType().getTypeName().equals(str_name) && field.getAnnotation(ProtocolIgnore.class) == null){
                    String protocol_name = (String) field.get(null);
                    int protocol_id = (int) packet_protocolClass.getDeclaredField(field.getName() + "_ID").get(null);
                    PROTOCOL_STR_TO_INT.put(protocol_id,protocol_name);
                    protocols.add(new Protocol(protocol_id , protocol_name));
                }
            }
            iProtocolIdService.saveOrUpdateBatch(protocols);
        }else{
            protocols = iProtocolIdService.selectAll();
            for (Protocol protocol : protocols) {
                PROTOCOL_STR_TO_INT.put(protocol.getProtocolId() , protocol.getProtocolName());
            }
        }
        PROTOCOL_STR_TO_INT.put(2,"s7comm");
        if(reload || iConfigurationSettingService.selectAll().size() == 0) {
            if(!reload) {
                log.info("no configuration in database and ready to load from libs ... ");
            }
            /*
             * INIT ALL FUN_CODE MAP
             */
            ServiceLoader<IProtocolFuncodeMap> serviceLoader = ServiceLoader.load(IProtocolFuncodeMap.class);
            for (IProtocolFuncodeMap iProtocolFuncodeMap : serviceLoader) {
                HashMap<String, String> funcodeStatements = new HashMap<>();
                String protocolName = iProtocolFuncodeMap.protocolAnalyzerName();
                Map<String, String> map = iProtocolFuncodeMap.initProtocol();
                List<ConfigurationSetting> configurationSettings = new ArrayList<>();
                for (String fun_code : map.keySet()) {
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
                AUTH_MAP.put(auth,authName);
            }
        }

        /***************************
         * INIT ART|OPT ATTACK DECODER
         ***************************/
        ArtDecodeCommon.init();
        AttackCommon.init();
        /***************************
         * 初始化工艺参数配置
         **************************/
        PagedArtConfig pagedArtConfig = new PagedArtConfig();
        pagedArtConfig.setPage(1);
        pagedArtConfig.setLimit(999);
        pagedArtConfig.setTag("");
        List<Integer> protocolIds = Arrays.asList(PACKET_PROTOCOL.MODBUS_ID,PACKET_PROTOCOL.S7_ID,
                PACKET_PROTOCOL.IEC104_ASDU_ID,PACKET_PROTOCOL.OPC_UA_ID,
                PACKET_PROTOCOL.DNP3_0_PRI_ID,PACKET_PROTOCOL.MMS_ID,PACKET_PROTOCOL.PN_IO_ID);
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
                    //添加工艺参数配置到缓存中
                    ArtDecodeCommon.addArtDecodeConfig(baseConfig);
                    //初始化工艺参数全局map，往里面放工艺参数名字和初始值0F
                    AppCommonUtil.initArtMap(baseConfig.getTag());
                    if (baseConfig.getShowGraph() == 1){
                        //添加要显示的工艺参数名字到缓存中
                        CommonCacheUtil.addShowGraphArg(baseConfig.getProtocolId(),baseConfig.getTag());
                    }
                }
            }
        }

        /****************************
         * INIT TSHARK COMMON CONFIG
         ***************************/
        TsharkCommon.s7comm_filter = tsharkConfig.getS7comm_filter();
        TsharkCommon.modbus_filter = tsharkConfig.getModbus_filter();

        /****************************
         * 获取工艺参数攻击配置
         ***************************/
        List<ArtAttackConfigDB> configDBS = packetInfoMapper.selectArtAttackConfigPaged(999,1);

        for (ArtAttackConfigDB configDB : configDBS) {
            if (configDB.isEnable()){
                List<String> strings = new ArrayList<>();
                List<ArtAttack2Config> artAttack2Configs = JSON.parseArray(configDB.getRuleJson(),ArtAttack2Config.class);
                for (ArtAttack2Config artAttack2Config : artAttack2Configs)
                {
                    strings.add(artAttack2Config.getValue());
                }
                AttackCommon.addArtAttackAnalyzeConfig(new ArtAttackAnalyzeConfig(strings,configDB.getDetail(),
                        configDB.isEnable(),configDB.getId()));
            }
        }

        CommonCacheUtil.initIProtocol(preProcessor.getI_protocols());

        initArtTest();
        /**************************
         *  PRINT INIT RESULT
         ***************************/
        //log.info("\n******************** \n AUTH_MAP : {} \n********************" , Common.AUTH_MAP);
        //log.info("\n******************** \n size : {} ; CONFIGURATION_MAP : {}\n********************" ,  Common.CONFIGURATION_MAP.size() , Common.CONFIGURATION_MAP);
        //log.info("\n******************** \n size : {} ; PROTOCOL_STR_TO_INT : {} \n********************" , Common.PROTOCOL_STR_TO_INT.size() ,  Common.PROTOCOL_STR_TO_INT  );
        //log.info("\n******************** \n size : {} ; ALL_ART_CONFIG : {} \n********************",ArtDecodeCommon.getAllArtConfigs().size(),ArtDecodeCommon.getAllArtConfigs());
        //System.out.println("spring boot admin address : http://your-address:8989");
        //System.out.println("swagger-ui address : http://your-address:your-port/swagger-ui.html#/greeting-controller");

        String virtualNetworkcardName = constantConfig.getVirtual_network();
        List<String> ipAddress = new ArrayList<>();
        try {
            List<NetworkInterface> networkInterfaces = NetworkInterfaceServiceImpl.doGetAllNetworkInterface();
            for (NetworkInterface networkInterface : networkInterfaces) {
                if (virtualNetworkcardName != null){
                    if (virtualNetworkcardName.equals(networkInterface.getDeviceRealName())){
                        ipAddress.addAll(networkInterface.getIpAddressed());
                        break;
                    }
                }else {
                    if (networkInterface.getDescription().contains("Npcap Loopback Adapter")) {
                        virtualNetworkcardName = networkInterface.getDeviceRealName();
                        ipAddress.addAll(networkInterface.getIpAddressed());
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (virtualNetworkcardName == null){
            System.err.println("未检测到回环网卡，报文分析无法进行");
            throw new PacketDetailServiceNotValidException("未检测到回环网卡，报文分析无法进行");
        }
        doStartPacketDetailThread(virtualNetworkcardName,ipAddress);

        /***********************************
         * 初始化设备最大上行和下行流量
         **********************************/
        List<DeviceMaxFlow> deviceMaxFlows = deviceMaxFlowMapper.selectBatch();
        for (DeviceMaxFlow deviceMaxFlow : deviceMaxFlows) {
            CommonCacheUtil.addOrUpdateDeviceMaxFlow(deviceMaxFlow);
        }

        /************************************
         * 初始化CPU和内存检测参数
         ***********************************/
        CommonCacheUtil.initCpuAndMemState();

        MainServer.initSocketIoServer(constantConfig.getGlobal_address(), Common.SOCKET_IO_PORT);
        /************************************
         * 设置图的ID，初始化一些设置
         ***********************************/
        iGplotService.changeGplot(Common.GPLOT_ID);

        /*************************************
         * 扫描注册SocketIo事件
         ************************************/
        DataSourceClass dataSourceClass = getClass().getAnnotation(DataSourceClass.class);
        Class<?>[] dataSourceClasses = dataSourceClass.value();
        for (Class<?> sourceClass : dataSourceClasses) {
            handleDataSourceClass(sourceClass, SpringContextUtil.getBean(sourceClass));
        }
        /************************************
         * 启动结束
         ***********************************/
        PrinterUtil.printMsg(1,"程序启动完成");
    }

    private void handleDataSourceClass(Class<?> clazz,Object obj){
        Method[] method = clazz.getDeclaredMethods();
        for (Method declaredMethod : method) {
            EventHandler eventHandler = declaredMethod.getAnnotation(EventHandler.class);
            if (eventHandler!=null){
                handleEventHandlerMethod(declaredMethod,obj);
            }
        }
    }

    private void handleEventHandlerMethod(Method declaredMethod,Object obj) {
        try {
            Object res = declaredMethod.invoke(obj);
            List dataListeners = (List) res;
            for (Object dataListener : dataListeners) {
                Event event = dataListener.getClass().getAnnotation(Event.class);
                String eventMsg = event.event();
                Class<?> eventType = event.eventType();
                SocketServiceCenter.registerSocketIoDataListenter(eventMsg,eventType, ((DataListener) dataListener));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void doStartPacketDetailThread(String virtualLoopback , List<String> ipAddresses) {
        StringBuilder commandBuilder = new StringBuilder();
        //ipv6.src != fe80::d19:df03:c48a:11a8 && ip.src != 192.168.232.123
        String ipV4 = null;
        for (String ipAddress : ipAddresses) {
            if (!ipAddress.contains(":")){
                ipV4 = ipAddress;
                break;
            }
        }
        //assert ipV4 != null;
        PrinterUtil.printStarter();
        PrinterUtil.printMsg("【回环IP地址】：" + ipV4);
        PrinterUtil.printEnder();
        //tshark -i "{device_name}" -T ek -Y "ip.src!=192.168.123.232"
        commandBuilder.append("tshark -i \"").append(virtualLoopback).append("\"")
                .append(" -T ek").append(" -Y \"ip.src!=")
                .append(ipV4).append("\"").append(" -M 1000");
        try {
            startHistoryPacketAnalyzeThread(commandBuilder.toString(),virtualLoopback);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    private void initArtTest(){
//        DNP3Config dnp3Config = new DNP3Config();
//        dnp3Config.setProtocol("dnp3");
//        dnp3Config.setProtocolId(PACKET_PROTOCOL.DNP3_0_PRI_ID);
//        dnp3Config.setShowGraph(1);
//        dnp3Config.setTag("Ub");
//        dnp3Config.setindex(1);
//        dnp3Config.setObjGroup(30);
//        ArtDecodeCommon.addArtDecodeConfig(dnp3Config);
//        StatisticsData.initArtArgs("Ub");
//        CommonCacheUtil.addShowGraphArg(PACKET_PROTOCOL.DNP3_0_PRI_ID,"Ub");
    }


}
