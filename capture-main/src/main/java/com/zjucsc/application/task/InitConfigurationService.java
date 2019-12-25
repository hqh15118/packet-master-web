package com.zjucsc.application.task;

import com.corundumstudio.socketio.listener.DataListener;
import com.zjucsc.application.config.*;
import com.zjucsc.application.config.auth.Auth;
import com.zjucsc.application.config.properties.ConstantConfig;
import com.zjucsc.application.config.properties.PreProcessor;
import com.zjucsc.application.config.properties.TsharkConfig;
import com.zjucsc.application.config.sys.SocketIoConfig;
import com.zjucsc.application.controller.attack.AttackConfigController;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.system.service.hessian_iservice.IConfigurationSettingService;
import com.zjucsc.application.system.service.hessian_iservice.IGplotService;
import com.zjucsc.application.system.service.hessian_iservice.IProtocolIdService;
import com.zjucsc.application.system.service.hessian_mapper.*;
import com.zjucsc.application.util.AppCommonUtil;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.art_decode.ArtDecodeUtil;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.attack.bean.BaseOptConfig;
import com.zjucsc.attack.config.S7OptCommandConfig;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import com.zjucsc.base.util.SpringContextUtil;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.common.util.PrinterUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.socket_io.*;
import com.zjucsc.tshark.TsharkCommon;
import javassist.CannotCompileException;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.zjucsc.application.util.CacheUtil.*;
import static com.zjucsc.application.util.ProtocolUtil.addProtocolFuncodeMeaning;

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
    @Autowired private OptAttackMapper optAttackMapper;
    @Autowired private ArtOptNameMapper artOptNameMapper;
    @Autowired private ArtOptCommandMapper artOptCommandMapper;

    @Override
    public void run(ApplicationArguments args) throws IllegalAccessException, NoSuchFieldException, ProtocolIdNotValidException {
        globalInit();
        /***************************
         * RELOAD FROM JAR
         ***************************/
        List<String> virReload = args.getOptionValues("reload");
        PrinterUtil.printStarter();
        PrinterUtil.printMsg("program args [reload <--reload>]: " + virReload);
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
         * 演示/ 真实场景
         ***************************/
        List<String> virType = args.getOptionValues("type");
        PrinterUtil.printMsg("program args [type--type]: " + virType);
        if (virType!=null && virType.size() > 0){
            Common.systemRunType = Integer.parseInt(virType.get(0));
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
        List<Protocol> protocols;
        String str_name = "java.lang.String";
//        if (iProtocolIdService.selectAll().size() == 0 ){
//            Class<PACKET_PROTOCOL> packet_protocolClass = PACKET_PROTOCOL.class;
//            Field[] allField = packet_protocolClass.getDeclaredFields();
//            for (Field field : allField) {
//                field.setAccessible(true);
//                if (field.getType().getTypeName().equals(str_name) && field.getAnnotation(ProtocolIgnore.class) == null){
//                    String protocol_name = (String) field.get(null);
//                    int protocol_id = (int) packet_protocolClass.getDeclaredField(field.getName() + "_ID").get(null);
//                    PROTOCOL_STR_TO_INT.put(protocol_id,protocol_name);
//                    protocols.add(new Protocol(protocol_id , protocol_name));
//                }
//            }
//            iProtocolIdService.saveOrUpdateBatch(protocols);
//        }else{
        PrinterUtil.printMsg("start init infos --> protocol str to id start");
            protocols = iProtocolIdService.selectAll();
            for (Protocol protocol : protocols) {
                PROTOCOL_STR_TO_INT.put(protocol.getProtocolId() , protocol.getProtocolName());
            }
//        }
        PrinterUtil.printMsg("protocol str to id end --> protocol configuration start");
        for (ConfigurationSetting configuration : iConfigurationSettingService.selectAll()) {
            addProtocolFuncodeMeaning(convertIdToName(configuration.getProtocolId()),
                    configuration.getFunCode(),configuration.getOpt());
        }
        PrinterUtil.printMsg("protocol configuration end --> auth start");
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
        PrinterUtil.printMsg("auth end --> art decode start");
        /***************************
         * INIT ART|OPT ATTACK DECODER
         ***************************/
        ArtDecodeUtil.init();
        PrinterUtil.printMsg("art decode end --> attack common start");
        AttackCommon.init();
        PrinterUtil.printMsg("attack common to id end --> art decode start");
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

        initArtArgConfig(protocolIds,pagedArtConfig);
        /****************************
         * INIT TSHARK COMMON CONFIG
         ***************************/
        TsharkCommon.s7comm_filter = tsharkConfig.getS7comm_filter();
        TsharkCommon.modbus_filter = tsharkConfig.getModbus_filter();
        /****************************
         * 获取工艺参数攻击配置
         ***************************/
        PrinterUtil.printMsg("art decode end --> art attack start");
        List<ArtAttackConfigDB> configDBS = packetInfoMapper.selectArtAttackConfigPaged(999,1);
        try {
            AttackConfigController.addArtAttackConfig2Cache(configDBS);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        /****************************
         * 初始化工控协议（设备筛选的时候使用）
         ***************************/
        CacheUtil.initIProtocol(preProcessor.getI_protocols());

        /*****************************
         * 初始化所有的正常报文
         ****************************/
        PrinterUtil.printMsg("art attack end --> right packet start");
        List<RightPacketInfo> rightPacketInfos = packetInfoMapper.selectAllRightPacketInfo();
        for (RightPacketInfo rightPacketInfo : rightPacketInfos) {
            CacheUtil.addNormalRightPacketInfo(rightPacketInfo);
        }

        /***********************************
         * 初始化设备最大上行和下行流量
         **********************************/
        PrinterUtil.printMsg("right packet end --> device max flow start");
        List<DeviceMaxFlow> deviceMaxFlows = deviceMaxFlowMapper.selectBatch();
        for (DeviceMaxFlow deviceMaxFlow : deviceMaxFlows) {
            CacheUtil.addOrUpdateDeviceMaxFlow(deviceMaxFlow);
        }

        /************************************
         * 初始化CPU和内存检测参数
         ***********************************/
        CacheUtil.initCpuAndMemState();

        MainServer.initSocketIoServer(constantConfig.getGlobal_address(), Common.SOCKET_IO_PORT);
        /************************************
         * 设置图的ID，初始化一些设置
         ***********************************/
        PrinterUtil.printMsg("device max flow end --> change glot start");
        iGplotService.changeGplot(Common.GPLOT_ID);
        PrinterUtil.printMsg("change glot end --> art opt attack start");
        /*************************************
         * 扫描注册SocketIo事件
         ************************************/
        initSocketIoEvent();
        /************************************
         * 初始化工艺参数操作攻击配置
         ***********************************/
        List<Integer> protocolIdsForOptAttack = Arrays.asList(PACKET_PROTOCOL.MODBUS_ID,PACKET_PROTOCOL.S7_ID);
        for (Integer protocolId : protocolIdsForOptAttack) {
            List<BaseOptConfig> baseOptConfigs = optAttackMapper.selectAllOptAttackConfigByProtocol(protocolId,1,999);
            for (BaseOptConfig baseOptConfig : baseOptConfigs) {
                if (baseOptConfig.isEnable()) {
                    AttackCommon.addOptAttackConfig(baseOptConfig);
                }
            }
        }
        PrinterUtil.printMsg("art opt attack end --> opt command decode start");
        /*************************************
         * 加载操作指令解析
         ************************************/
        List<BaseOpName> s7OptNameList = artOptNameMapper.selectBatch();
        ArtOptAttackUtil.resetOpName2OptConfig(s7OptNameList);
        /*************************************
         * 加载操作指令公式
         ************************************/
        PrinterUtil.printMsg("opt command decode end --> opt command operation start");
        List<S7OptCommandConfig> s7OptCommandConfigs = artOptCommandMapper.selectBatch();
        ArtOptAttackUtil.resetProtocol2OptCommandConfig(s7OptCommandConfigs);
        PrinterUtil.printMsg("opt command operation end");
        /************************************
         * 启动结束
         ***********************************/
        PrinterUtil.printMsg(1,"程序启动完成--v10.28");
    }

    private void globalInit() {
        if (tsharkConfig.getS7comm_filter().trim().equals("tcp")){
            tsharkConfig.setTcp(true);
        }
    }

    private void initSocketIoEvent() {
        DataSourceClass dataSourceClass = getClass().getAnnotation(DataSourceClass.class);
        Class<?>[] dataSourceClasses = dataSourceClass.value();
        for (Class<?> sourceClass : dataSourceClasses) {
            handleDataSourceClass(sourceClass, SpringContextUtil.getBean(sourceClass));
        }
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
            declaredMethod.setAccessible(true);
            Object res = declaredMethod.invoke(obj);
            List dataListeners = (List) res;
            for (Object dataListener : dataListeners) {
                Event event = dataListener.getClass().getAnnotation(Event.class);
                String eventMsg = event.event();
                Class<?> eventType = event.eventType();
                SocketServiceCenter.registerSocketIoDataListener(eventMsg,eventType, ((DataListener) dataListener));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void initArtArgConfig(List<Integer> protocolIds,PagedArtConfig pagedArtConfig) throws ProtocolIdNotValidException {
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
                        baseConfig.setProtocol(CacheUtil.convertIdToName(baseConfig.getProtocolId()));
                    }
                    //添加工艺参数解析配置到缓存中
                    ArtDecodeUtil.addArtDecodeConfig(baseConfig);
                    //初始化工艺参数全局map，往里面放工艺参数名字和初始值0F
                    AppCommonUtil.initArtMap2Show(baseConfig.getTag());
                    CacheUtil.addOrUpdateArtName2ArtGroup(baseConfig.getTag(),baseConfig.getGroup());
                    if (baseConfig.getShowGraph() == 1){
                        //添加要显示的工艺参数名字到缓存中
                        CacheUtil.addShowGraphArg(baseConfig.getProtocolId(),baseConfig.getTag());
                    }
                }
            }
        }
    }

}
