package com.zjucsc.application.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.application.domain.bean.StatisticInfoSaveBean;
import com.zjucsc.application.domain.bean.RightPacketInfo;
import com.zjucsc.application.domain.non_hessian.FvDimensionFilterCondition;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.common.common_util.CommonUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Slf4j
public class CommonCacheUtil {

    /*********************************
     * 清楚当前组态图下所有缓存的配置
     ********************************/
    public static void clearAllCacheByGplotId(){
        removeAllDeviceNumberToName();
    }

    public static final BiMap<Integer,String> AUTH_MAP = HashBiMap.create();

    /*********************************
     *
     *  CONFIGURATION_MAP
     *
     **********************************/
    /**
     * cache7
     * 所有可配置的协议
     * 协议 --> 功能码 以及 对应的含义 --> 从serviceLoader中加载
     */
    public static final HashMap<String , HashMap<Integer,String>> CONFIGURATION_MAP = new HashMap<>();

    public static void deleteCachedProtocolByName(String protocolName) {
        CONFIGURATION_MAP.remove(protocolName);
        PROTOCOL_STR_TO_INT.inverse().remove(protocolName);
    }

    public static void deleteCachedProtocolByID(int protocolId) throws ProtocolIdNotValidException {
        String protocolName = convertIdToName(protocolId);
        CONFIGURATION_MAP.remove(protocolName);
        PROTOCOL_STR_TO_INT.inverse().remove(protocolName);
    }

    public static void deleteCachedFuncodeByName(String protocolName,
                                                 int funcode) throws ProtocolIdNotValidException {
        HashMap<Integer, String> funcodeMeaningMap;
        if ((funcodeMeaningMap = CONFIGURATION_MAP.get(protocolName)) == null) {
            throw new ProtocolIdNotValidException("deleteCachedFuncodeByName " + protocolName + " protocol name not exist and the CONFIGURATION_MAP is : \n" + CONFIGURATION_MAP);
        } else {
            funcodeMeaningMap.remove(funcode);
        }
        log.info("delete funcode [{}] of protocol [{}] ", funcode, protocolName);
    }

    public static void deleteCachedFuncodeById(int protocolId,
                                               int funcode) throws ProtocolIdNotValidException {
        String name = convertIdToName(protocolId);
        deleteCachedFuncodeByName(name, funcode);
        log.info("【接上】delete funcode [{}] of protocol [{}]", funcode, name);
    }

    /**
     * 添加新的协议，不增加该协议的功能码
     *
     * @param protocol
     * @param protocolId
     */
    public static void addNewProtocolToCache(String protocol,
                                             int protocolId) throws ProtocolIdNotValidException {
        doAddNewProtocolToCache(protocol, protocolId, null);
    }

    private static void doAddNewProtocolToCache(String protocol, int protocolId, HashMap<Integer, String> funCodeMeaningMap) throws ProtocolIdNotValidException {
        if (CONFIGURATION_MAP.get(protocol) != null || PROTOCOL_STR_TO_INT.get(protocolId) != null) {
            throw new ProtocolIdNotValidException("protocol " + protocol + " is not valid , cause it exists in CONFIGURATION_MAP or PROTOCOL_STR_TO_INT \n CONFIGURATION_MAP is" +
                    CONFIGURATION_MAP + " PROTOCOL_STR_TO_INT" + PROTOCOL_STR_TO_INT);
        } else {
            if (funCodeMeaningMap == null) {
                CONFIGURATION_MAP.put(protocol, new HashMap<>());
            } else {
                CONFIGURATION_MAP.put(protocol, funCodeMeaningMap);
            }
            addNewProtocolToId(protocol, protocolId);
        }
    }

    /**
     * 添加新有协议，同时添加该协议对应的功能码
     *
     * @param protocol
     * @param protocolId
     * @param funcodeMeaningMap
     */
    public static void addNewProtocolAndFuncodeMapToCache(String protocol, int protocolId, HashMap<Integer, String> funcodeMeaningMap) throws ProtocolIdNotValidException {
        doAddNewProtocolToCache(protocol, protocolId, funcodeMeaningMap);
    }

    /**
     * 增加已有协议对应的功能码-操作
     *
     * @param protocol
     * @param funcode
     * @param opt
     * @throws ProtocolIdNotValidException
     */
    public static void updateOldProtocolCacheByName(String protocol,
                                                    int funcode,
                                                    String opt) throws ProtocolIdNotValidException {
        HashMap<Integer, String> funcodeMeaningMap;

        if ((funcodeMeaningMap = CONFIGURATION_MAP.get(protocol)) == null) {
            throw new ProtocolIdNotValidException("updateOldProtocolCacheByName " + protocol + " protocol name not exist and the CONFIGURATION_MAP is : \n" + CONFIGURATION_MAP);
        } else {
            funcodeMeaningMap.put(funcode, opt);
        }
        log.info("update CONFIGURATION_MAP protocol : {} funcode : {}  opt : {}", protocol, funcode, opt);
    }

    /**
     * @param protocolId
     * @param funcode
     * @param opt
     * @throws ProtocolIdNotValidException
     */
    public static void updateOldProtocolCacheById(int protocolId,
                                                  int funcode,
                                                  String opt) throws ProtocolIdNotValidException {
        String protocolName;
        if ((protocolName = PROTOCOL_STR_TO_INT.get(protocolId)) == null) {
            throw new ProtocolIdNotValidException(protocolId + " protocol id not exist and the PROTOCOL_STR_TO_INT is : \n" + PROTOCOL_STR_TO_INT);
        } else {
            HashMap<Integer, String> funcodeMeaningMap;
            if ((funcodeMeaningMap = CONFIGURATION_MAP.get(protocolName)) == null) {
                throw new ProtocolIdNotValidException(protocolName + "protocol name not exist and the CONFIGURATION_MAP is : \n" + CONFIGURATION_MAP);
            } else {
                funcodeMeaningMap.put(funcode, opt);
            }
        }
    }


    /*********************************
     *
     *  PROTOCOL_STR_TO_INT
     *
     **********************************/

    /**
     * cache2
     *  init in
     * @see com.zjucsc.application.task.InitConfigurationService
     */
    public static final BiMap<Integer,String> PROTOCOL_STR_TO_INT = HashBiMap.create();
    /**
     * @param protocolId 协议ID
     * @return 协议名字
     * @throws ProtocolIdNotValidException 未发现指定协议ID
     */
    public static String convertIdToName(int protocolId) throws ProtocolIdNotValidException {
        if (PROTOCOL_STR_TO_INT.get(protocolId) == null) {
            throw new ProtocolIdNotValidException(protocolId + " << PROTOCOL ID not exist and the PROTOCOL_STR_TO_INT is : \n" + PROTOCOL_STR_TO_INT);
        }
        return PROTOCOL_STR_TO_INT.get(protocolId);
    }

    public static int convertNameToId(String protocolName) throws ProtocolIdNotValidException {
        if (PROTOCOL_STR_TO_INT.inverse().get(protocolName) == null) {
            throw new ProtocolIdNotValidException(protocolName + " << PROTOCOL NAME not exist and the PROTOCOL_STR_TO_INT is : \n" + PROTOCOL_STR_TO_INT);
        }
        return PROTOCOL_STR_TO_INT.inverse().get(protocolName);
    }


    public static boolean protocolExist(String protocolName) {
        try {
            convertNameToId(protocolName);
        } catch (ProtocolIdNotValidException e) {
            return false;
        }
        return true;
    }

    public static void addNewProtocolToId(String protocolName, int protocolId) throws ProtocolIdNotValidException {
        if (PROTOCOL_STR_TO_INT.get(protocolId) != null) {
            throw new ProtocolIdNotValidException(protocolId + " << is existed ,can not update PROTOCOL_STR_TO_INT");
        }
        PROTOCOL_STR_TO_INT.put(protocolId, protocolName);
        log.info("add new protocol into PROTOCOL_STR_TO_INT ; protocolId : {} ; protocolName : {} ", protocolId, protocolName);
    }

    /*********************************
     *
     *  DEVICE_IP_TO_NAME
     *
     **********************************/
    /**
     * cache1
     * 设备IP和DEVICE_NUMBER之间互相转换
     */
    public static final BiMap<String,String> DEVICE_NUMBER_TO_TAG = HashBiMap.create();
    /**
     * 【key 设备deviceNumber deviceTag设备标识（Ip/Mac）】
     * 每台设备的统计信息
     */
    private static final ConcurrentHashMap<String, StatisticInfoSaveBean> STATISTICS_INFO_BEAN =
            new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, StatisticInfoSaveBean> getStatisticsInfoBean(){
        return STATISTICS_INFO_BEAN;
    }
    /**
     * @param deviceNumber 设备Number
     * @param deviceTag    device 的 ip地址或者mac地址
     */
    public static void addOrUpdateDeviceNumberAndTAG(String deviceNumber, String deviceTag) {
        DEVICE_NUMBER_TO_TAG.forcePut(deviceNumber, deviceTag);
        STATISTICS_INFO_BEAN.putIfAbsent(deviceNumber, new StatisticInfoSaveBean());
    }

    public static void removeDeviceNumberToTag(String deviceNumber) {
        DEVICE_NUMBER_TO_TAG.remove(deviceNumber);
        STATISTICS_INFO_BEAN.remove(deviceNumber);
    }

    public static String getTargetDeviceNumberByTag(String tag) {
        return DEVICE_NUMBER_TO_TAG.get(tag);
    }

    public static String getTargetDeviceNumberByTag(String ip_dst, String eth_dst) {
        String tag = DEVICE_NUMBER_TO_TAG.inverse().get(ip_dst);
        if (tag != null) {
            return tag;
        }
        tag = DEVICE_NUMBER_TO_TAG.inverse().get(eth_dst);
        return tag;
    }

    public static String getTargetDeviceTagByNumber(String deviceNumber) {
        return DEVICE_NUMBER_TO_TAG.get(deviceNumber);
    }

    public static void removeAllCachedDeviceNumber() {
        DEVICE_NUMBER_TO_TAG.clear();
        STATISTICS_INFO_BEAN.clear();
    }

    /*********************************
     *
     *  CACHED SHOW GRAPH ARGS
     *
     **********************************/
    /**
     * 要显示的工艺参数集合【将这个set里面的工艺参数数据传输到前端，其他的不用传】
     */
    public static final Set<String> SHOW_GRAPH_SET = Collections.synchronizedSet(new HashSet<>());

    public static void addShowGraphArg(int protocolId, String artArg) {
        SHOW_GRAPH_SET.add(artArg);
    }

    public static boolean removeShowGraph(int protocolId, String artArg) {
        if (SHOW_GRAPH_SET.remove(artArg)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isArtShow(String name){
        return SHOW_GRAPH_SET.contains(name);
    }

    /*********************************
     *
     *  CACHED ART DECODE INSTANCE
     *
     **********************************/

    /**********************************
     *
     * AttackCommon STATISTICS_INFO_BEAN 发送到数据库的统计信息Map
     *
     *********************************/

    private static BiConsumer<String, StatisticInfoSaveBean> biConsumer =
            (s, saveBean) -> {
                saveBean.setAttack(0);
                saveBean.setDeviceNumber(s);
                saveBean.setDownload(0);
                saveBean.setException(0);
                saveBean.setUpload(0);
            };

    /**
     * 重置，每次统计时都需要重置统计信息
     */
    public static void resetSaveBean() {
        STATISTICS_INFO_BEAN.forEach(biConsumer);
    }

     /**********************************
     *
     * 设置 SCHEDULE SERVICE 运行状态
     *
     *********************************/
    private static volatile boolean SCHEDULE_RUNNING = false;
    public static void setScheduleServiceRunningState(boolean running) {
        SCHEDULE_RUNNING = running;
    }

    public static boolean getScheduleServiceRunningState() {
        return SCHEDULE_RUNNING;
    }

    /**********************************
     *
     * 获取五元组过滤器
     *
     *********************************/
    public static FiveDimensionAnalyzer getFvDimensionFilter(FvDimensionLayer layer) {
        FiveDimensionAnalyzer fiveDimensionAnalyzer = Common.FV_DIMENSION_FILTER_PRO.get(layer.ip_dst[0]);
        if (fiveDimensionAnalyzer != null) {
            return fiveDimensionAnalyzer;
        }
        if (!layer.ip_dst[0].equals("--")) {
            //报文有IP地址
            return null;
        }
        return Common.FV_DIMENSION_FILTER_PRO.get(layer.eth_dst[0]);
    }

    /**********************************
     *
     * 获取功能码过滤器
     *
     *********************************/
    public static ConcurrentHashMap<String, OperationAnalyzer> getOptAnalyzer(FvDimensionLayer layer) {
        ConcurrentHashMap<String, OperationAnalyzer> map = CommonOptFilterUtil.getTargetProtocolToOperationAnalyzerByDeviceTag(layer.ip_dst[0]);
        if (map != null) {
            return map;
        }
        return CommonOptFilterUtil.getTargetProtocolToOperationAnalyzerByDeviceTag(layer.eth_dst[0]);
    }

    /***********************************
     * 五元组白名单
     **********************************/
    private static final byte[] LOCK_RIGHT_PROTOCOL_LIST = new byte[1];
    private static final Map<String,Map<String,String>> RIGHT_PROTOCOL_LIST = new HashMap<>();

    public static void addWhiteProtocolToCache(String deviceNumber , String protocolName[]) {
        synchronized (LOCK_RIGHT_PROTOCOL_LIST) {
            Map<String, String> whiteProtocolMap = RIGHT_PROTOCOL_LIST.computeIfAbsent(deviceNumber, k -> new HashMap<>());
            for (String name : protocolName) {
                whiteProtocolMap.put(name, "");
            }
        }
    }

    public static int removeWhiteProtocolFromCache(String deviceNumber , String protocolName[]) {
        int count = 0;
        synchronized (LOCK_RIGHT_PROTOCOL_LIST) {
            Map<String, String> whiteProtocolMap = RIGHT_PROTOCOL_LIST.get(deviceNumber);
            if (whiteProtocolMap != null) {
                for (String s : protocolName) {
                    if (whiteProtocolMap.remove(s) != null) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void clearAllWhiteProtocols(String deviceNumber) {
        synchronized (LOCK_RIGHT_PROTOCOL_LIST) {
            Map<String, String> whiteProtocolMap = RIGHT_PROTOCOL_LIST.get(deviceNumber);
            if (whiteProtocolMap != null) {
                whiteProtocolMap.clear();
            }
        }
    }

    public static boolean isNormalWhiteProtocol(String deviceNumber,String protocolName){
        Map<String,String> whiteProtocolMap = RIGHT_PROTOCOL_LIST.get(deviceNumber);
        if (whiteProtocolMap!=null){
            return whiteProtocolMap.containsKey(protocolName);
        }else{
            return false;
        }
    }

    /************************************
     *
     * 通用正常报文 [协议：报文List]
     *
     ***********************************/
    private static final byte[] NORMAL_LOCK = new byte[1];
    private static final HashMap<String, HashMap<RightPacketInfo,String>> RIGHT_PACKET_INFOS =
            new HashMap<>();
    public static void addNormalRightPacketInfo(RightPacketInfo rightPacketInfo){
        synchronized (NORMAL_LOCK){
            String protocol = rightPacketInfo.getProtocol();
            HashMap<RightPacketInfo,String> rightPacketInfos = RIGHT_PACKET_INFOS.computeIfAbsent(protocol, k -> new HashMap<>());
            rightPacketInfos.put(rightPacketInfo,"");
        }
    }
    public static boolean isNormalRightPacket(RightPacketInfo rightPacketInfo){
        synchronized (NORMAL_LOCK){
            String protocol = rightPacketInfo.getProtocol();
            Map<RightPacketInfo,String> map = RIGHT_PACKET_INFOS.get(protocol);
            if (map == null){
                return false;
            }
            return map.containsKey(rightPacketInfo);
        }
    }
    public static Collection<HashMap<RightPacketInfo,String>> getNormalPacketInfo(){
        synchronized (NORMAL_LOCK){
            return RIGHT_PACKET_INFOS.values();
        }
    }
    /************************************
     *
     * 攻击占比统计 key : 攻击类型  value 攻击数目
     *
     ***********************************/
    private static final HashMap<String,Integer> ATTACK_PERCENT = new HashMap<>();
    private static final byte[] LOCK_ATTACK_PERCENT = new byte[1];
    public static void addNewAttackLog(String attackName){
        synchronized (LOCK_ATTACK_PERCENT){
            int attackNum = ATTACK_PERCENT.computeIfAbsent(attackName,k -> 0);
            ATTACK_PERCENT.put(attackName,++attackNum);
        }
    }
    public static void updateAttackLog(){
        synchronized (LOCK_ATTACK_PERCENT){
            SocketServiceCenter.updateAllClient(SocketIoEvent.ATTACK_STATISTICS,ATTACK_PERCENT);
        }
    }

    /*************************************
     * [deviceNumber - deviceName]
     ************************************/
    private static final Map<String,String> DEVICE_NUMBER_TO_NAME =
            new HashMap<>();
    public static void addDeviceNumberToName(String deviceNumber,String deviceName){
        DEVICE_NUMBER_TO_NAME.put(deviceNumber, deviceName);
    }
    public static void removeAllDeviceNumberToName(){
        DEVICE_NUMBER_TO_NAME.clear();
    }
    public static String convertDeviceNumberToName(String deviceNumber){
        return DEVICE_NUMBER_TO_NAME.get(deviceNumber);
    }
    public static void removeDeviceNumberToName(String deviceNumber){
        DEVICE_NUMBER_TO_NAME.remove(deviceNumber);
    }

    /*************************************
     *
     * 所有IP地址和MAC地址
     *
     ************************************/
    private static final ConcurrentHashMap<String, Device> ALL_DEVICES = new ConcurrentHashMap<>();

    public static void addOrUpdateDeviceManually(Device device){
        ALL_DEVICES.put(device.getDeviceTag(),device);
        ConcurrentHashMap<Device, AtomicInteger> map = DEVICE_TO_DEVICE_PACKETS.remove(device);
        if (map!=null){
            DEVICE_TO_DEVICE_PACKETS.put(device,map);
        }
    }

    public static void removeAllDevices(){
        ALL_DEVICES.clear();
    }

    public static void removeAllDeviceListByMacAddress(String deviceTag){
        ALL_DEVICES.remove(deviceTag);
    }

    /**
     *
     * @param layer 五元组
     * @return 新增之前的设备，如果不存在为null
     */
    public static Device autoAddDevice(FvDimensionLayer layer){
//        Device dstDevice = null;
//        String deviceTag = layer.ip_dst[0].equals("--") ? layer.eth_dst[0] : layer.ip_dst[0];
//        if (!ALL_DEVICES.containsKey(deviceTag)){
//            if (!layer.eth_dst[0].equals("ff:ff:ff:ff:ff:ff") && !layer.ip_dst[0].equals("0.0.0.0")
//            && !layer.ip_dst[0].equals("255.255.255.255")) {
//                //非广播mac地址
//                dstDevice = createDevice(layer);
//                ALL_DEVICES.put(dstDevice.getDeviceTag(), dstDevice);
//                //new device
//                CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(dstDevice.getDeviceNumber(), dstDevice.getDeviceTag());
//                CommonCacheUtil.addDeviceNumberToName(dstDevice.getDeviceNumber(), dstDevice.getDeviceInfo());
//            }
//        }
        if (/*CommonCacheUtil.iProtocolExist(layer.protocol) && */DeviceOptUtil.validPacketInfo(layer)){
            String deviceTag = DeviceOptUtil.getSrcDeviceTag(layer);
            if (!ALL_DEVICES.containsKey(deviceTag)) {
                    Device srcDevice;
                    srcDevice = createDeviceInverse(layer, deviceTag);
                    ALL_DEVICES.put(srcDevice.getDeviceTag(), srcDevice);
                    //new device
                    CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(srcDevice.getDeviceNumber(), srcDevice.getDeviceTag());
                    CommonCacheUtil.addDeviceNumberToName(srcDevice.getDeviceNumber(), srcDevice.getDeviceInfo());
                    return srcDevice;
            }
        }
        /*
        else{
            String deviceTag = DeviceOptUtil.getSrcDeviceTag(layer);
            CommonCacheUtil.addDropProtocol(layer.protocol);
            if (!ALL_DEVICES.containsKey(deviceTag)) {
                Device srcDevice;
                //srcDevice = createDeviceInverse(layer, deviceTag);
                //ALL_DEVICES.put(srcDevice.getDeviceTag(), srcDevice);
                //new device
                //CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(srcDevice.getDeviceNumber(), srcDevice.getDeviceTag());
                //CommonCacheUtil.addDeviceNumberToName(srcDevice.getDeviceNumber(), srcDevice.getDeviceInfo());
                return addUnKnownDevice(deviceTag,layer);
            }
        }
        //return new Device[]{dstDevice,srcDevice};
        */
        return null;
    }
    private static Device getDeviceByTag(String deviceTag){
        return ALL_DEVICES.get(deviceTag);
    }
    public static ConcurrentHashMap<String, Device> getAllDevices(){
        return ALL_DEVICES;
    }
    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    /**
     * 根据源地址创建设备
     * @param layer 五元组
     * @return 设备
     */
    private static Device createDeviceInverse(FvDimensionLayer layer , String deviceTag){
        StringBuilder sb = CommonUtil.getGlobalStringBuilder();
        long timeNow = System.nanoTime();                       //防止太快重复设备
        Device device = new Device();
        sb.append("设备").append(CommonUtil.getDateFormat2().format(new Date()))
                .append("_").append(threadLocalRandom.nextInt(10000));
        device.setDeviceInfo(sb.toString());
        device.setDeviceNumber(String.valueOf(timeNow));
        device.setDeviceType(1);
        device.setDeviceMac(layer.eth_src[0]);
        device.setDeviceTag(deviceTag);
        device.setGPlotId(Common.GPLOT_ID);
        device.setDeviceIp(layer.ip_src[0]);
        return device;
    }

    /*************************************
     * 报文 : 当前设备【srcMacAddress】 --> 目的设备【dstMacAddress】
     * 源设备发送到其他设备的报文数量 [当前设备，[目的设备，报文流量]]
     ************************************/
    private static final ConcurrentHashMap<Device,ConcurrentHashMap<Device, AtomicInteger>> DEVICE_TO_DEVICE_PACKETS
            = new ConcurrentHashMap<>();

    public static void addTargetDevicePacket(FvDimensionLayer layer){
        String srcTag = DeviceOptUtil.getSrcDeviceTag(layer);
        Device srcDevice = getDeviceByTag(srcTag);        //发送设备
        String dstTag = DeviceOptUtil.getDstDeviceTag(layer);
        Device dstDevice = getDeviceByTag(dstTag);        //接收设备
        if (srcDevice!=null && dstDevice!=null) {
            ConcurrentHashMap<Device, AtomicInteger> device2PacketNum = DEVICE_TO_DEVICE_PACKETS.get(dstDevice);
            if (device2PacketNum == null) {
                device2PacketNum = new ConcurrentHashMap<>();
                DEVICE_TO_DEVICE_PACKETS.put(dstDevice, device2PacketNum);
            }
            AtomicInteger atomicInteger = device2PacketNum.putIfAbsent(srcDevice, new AtomicInteger(1));
            if (atomicInteger != null) {
                atomicInteger.incrementAndGet();
            }
        }

    }

    public static ConcurrentHashMap<Device,ConcurrentHashMap<Device, AtomicInteger>> getDeviceToDevicePackets(){
        return DEVICE_TO_DEVICE_PACKETS;
    }

    /************************************
     *
     * 所有要识别的工控协议【用于设备识别】
     *
     ************************************/
    private static final HashMap<String,String> ALL_IPROTOCOL = new HashMap<>();
    public static void initIProtocol(List<String> protocols){
        for (String protocol : protocols) {
            ALL_IPROTOCOL.put(protocol,protocol);
        }
    }
    public static boolean iProtocolExist(String protocolName){
        return ALL_IPROTOCOL.containsKey(protocolName);
    }
    private static final HashMap<String,String> ALL_DROP_PROTOCOL = new HashMap<>();
    public static void addDropProtocol(String protocolName){
        ALL_DROP_PROTOCOL.put(protocolName,"");
    }
    public static Set<String> getAllDropProtocol(){
        return ALL_DROP_PROTOCOL.keySet();
    }

    /************************************
     * 所有的未知设备
     ***********************************/
//    private static final ConcurrentHashMap<String, Device> UNKNOWN_DEVICE_CONCURRENT_HASH_MAP
//            = new ConcurrentHashMap<>();
//    public static Device addUnKnownDevice(String deviceTag,FvDimensionLayer layer){
//        UNKNOWN_DEVICE_CONCURRENT_HASH_MAP.putIfAbsent(deviceTag,createUnknownDevice(layer));
//        return UNKNOWN_DEVICE_CONCURRENT_HASH_MAP.get(deviceTag);
//    }
//    public static void transferUnKnownDeviceToKnownDevice(String deviceTag){
//        Device device = UNKNOWN_DEVICE_CONCURRENT_HASH_MAP.remove(deviceTag);
//        addOrUpdateDeviceManually(device);
//    }
    private static Device createUnknownDevice(FvDimensionLayer layer){
        StringBuilder sb = CommonUtil.getGlobalStringBuilder();
        long timeNow = System.nanoTime();
        Device device = new Device();
        sb.append("未知设备").append(CommonUtil.getDateFormat2().format(new Date()))
                .append("_").append(threadLocalRandom.nextInt(10000));
        device.setDeviceInfo(sb.toString());
        device.setDeviceNumber(String.valueOf(timeNow));
        device.setDeviceType(-1);
        device.setDeviceMac(layer.eth_dst[0]);
        device.setDeviceTag(layer.ip_dst[0].equals("--")?layer.eth_dst[0] : layer.ip_dst[0]);
        device.setGPlotId(Common.GPLOT_ID);
        device.setDeviceIp(layer.ip_dst[0]);
        return device;
    }

    /*************************************
     * 实时五元组过滤规则
     ************************************/
    private static FvDimensionFilterCondition fvDimensionFilterCondition;
    public static void addOrUpdateFvDimensionFilterCondition(FvDimensionFilterCondition fvDimensionFilterCondition){
        CommonCacheUtil.fvDimensionFilterCondition = fvDimensionFilterCondition;
    }
    public static boolean realTimeFvDimensionFilter(FvDimensionLayer layer){
        if (fvDimensionFilterCondition == null){
            return false;
        }
        return match(fvDimensionFilterCondition.getSrcMac(),layer.eth_src[0])&&
            match(fvDimensionFilterCondition.getSrcIp(),layer.ip_src[0])&&
            match(fvDimensionFilterCondition.getSrcPort(),layer.src_port[0])&&
            match(fvDimensionFilterCondition.getDstMac(),layer.eth_dst[0])&&
            match(fvDimensionFilterCondition.getDstIp(),layer.ip_dst[0])&&
            match(fvDimensionFilterCondition.getDstPort(),layer.dst_port[0])&&
            match(fvDimensionFilterCondition.getProtocol(),layer.protocol)&&
            match(fvDimensionFilterCondition.getFunCode(),layer.funCode);
    }

    /**
     * @param matcher 匹配规则
     * @param matchor 被匹配
     * @return
     */
    private static boolean match(String matcher,String matchor){
        if (StringUtils.isBlank(matcher)){
            return true;
        }else{
            return matcher.equals(matchor);
        }
    }
}