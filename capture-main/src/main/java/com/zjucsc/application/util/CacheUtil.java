package com.zjucsc.application.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.application.domain.bean.StatisticInfoSaveBean;
import com.zjucsc.application.domain.bean.RightPacketInfo;
import com.zjucsc.application.domain.non_hessian.DeviceMaxFlow;
import com.zjucsc.application.domain.non_hessian.FvDimensionFilterCondition;
import com.zjucsc.application.domain.non_hessian.SysRunArg;
import com.zjucsc.application.domain.non_hessian.Top5Statistic;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.base.util.SysRunStateUtil;
import com.zjucsc.base.util.limit.LimitServiceEntry;
import com.zjucsc.base.util.limit.ValidInstanceCallback;
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
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
public class CacheUtil {

    /*********************************
     * 清楚当前组态图下所有缓存的配置
     ********************************/
    /*
    public static void clearAllCacheByGplotId(){
        removeAllDeviceNumberToName();
    }
    */
    public static final BiMap<Integer,String> AUTH_MAP = HashBiMap.create();        //初始化时候已经加载
    /**************************************
     * 所有可配置的协议
     * 协议 --> 功能码 以及 对应的含义
     *************************************/
    public static final HashMap<String , HashMap<String,String>> CONFIGURATION_MAP = new HashMap<>();   //初始化时候已经加载
    /**************************************
     * init in
     * @see com.zjucsc.application.task.InitConfigurationService
     *************************************/
    public static final BiMap<Integer,String> PROTOCOL_STR_TO_INT = HashBiMap.create();//初始化时候已经加载
    /**************************************
     * 设备IP和DEVICE_NUMBER之间互相转换
     *************************************/
    public static final BiMap<String,String> DEVICE_NUMBER_TO_TAG = HashBiMap.create();//初始化时候已经加载
    /**************************************
     * 【key 设备deviceNumber deviceTag设备标识（Ip/Mac）】
     * 每台设备的统计信息
     *************************************/
    private static final ConcurrentHashMap<String, StatisticInfoSaveBean> STATISTICS_INFO_BEAN = //初始化时候不需要加载
            new ConcurrentHashMap<>();
    /*************************************
     * 要显示的工艺参数集合
     * 【将这个set里面的工艺参数数据传输到前端，其他的不用传】
     ************************************/
    public static final Set<String> SHOW_GRAPH_SET = Collections.synchronizedSet(new HashSet<>());  //初始化时候已经加载
    /************************************
     * 通用正常报文 [协议：报文List]
     ***********************************/
    private static final HashMap<String, HashMap<RightPacketInfo,String>> RIGHT_PACKET_INFOS = new HashMap<>(); //初始化时候已经加载
    /***********************************
     * 五元组白名单
     * deviceNumber -- 协议名字map
     **********************************/
    private static final Map<String,Map<String,String>> RIGHT_PROTOCOL_LIST = new ConcurrentHashMap<>();    // 初始化时候已经加载
    /*************************************
     * 所有IP地址和MAC地址
     * deviceTag -- device
     * 所有设备
     ************************************/
    private static final ConcurrentHashMap<String, Device> ALL_DEVICES = new ConcurrentHashMap<>();     // 初始化时候已经加载
    //存储所有的mac地址，防止设备重复
    private static final ConcurrentHashMap<String,String> ALL_MAC_ADDRESS = new ConcurrentHashMap<>(); // 初始化时候已经加载
    /************************************
     * 所有要识别的工控协议【用于设备识别】
     ************************************/
    private static final Map<String,String> ALL_IPROTOCOL = new ConcurrentHashMap<>();  // 初始化时候已经加载
    /*************************************
     * [deviceNumber - deviceName]
     ************************************/
    private static final Map<String,String> DEVICE_NUMBER_TO_NAME = new ConcurrentHashMap<>(); // 初始化时候已经加载
    /*************************************
     * 报文 : 当前设备【srcMacAddress】 --> 目的设备【dstMacAddress】
     * 源设备发送到其他设备的报文数量 [当前设备，[目的设备，报文流量]]
     ************************************/
    private static final ConcurrentHashMap<Device,ConcurrentHashMap<Device, AtomicInteger>> DEVICE_TO_DEVICE_PACKETS // 不需要初始化
            = new ConcurrentHashMap<>();
    /*************************************
     * 配置设备最大流量
     ************************************/
    private static final Map<String,DeviceMaxFlow> DEVICE_MAX_FLOW = new ConcurrentHashMap<>();  // 初始化时候已经加载
    /*************************************
     * 设备和设备匹配
     ************************************/
    private static ConcurrentHashMap<String,String> D2DAttackPair = new ConcurrentHashMap<>(); //不需要初始化
    /*************************************
     * 所有被丢弃的协议【设备识别】
     ************************************/
    private static final HashMap<String,String> ALL_DROP_PROTOCOL = new HashMap<>(); //不需要初始化

    /**********************************
     * 工艺参数名字 -- 工艺参数组别
     **********************************/
    public static ConcurrentHashMap<String,String> ART_NAME_TO_GROUP = new ConcurrentHashMap<>();
    /*********************************
     *
     *  CONFIGURATION_MAP
     *
     **********************************/

//    public static void deleteCachedProtocolByName(String protocolName) {
//        CONFIGURATION_MAP.remove(protocolName);
//        PROTOCOL_STR_TO_INT.inverse().remove(protocolName);
//    }
    public static void deleteCachedProtocolByID(int protocolId) throws ProtocolIdNotValidException {
        String protocolName = convertIdToName(protocolId);
        CONFIGURATION_MAP.remove(protocolName);
        PROTOCOL_STR_TO_INT.inverse().remove(protocolName);
    }

    private static void deleteCachedFuncodeByName(String protocolName, String funcode) throws ProtocolIdNotValidException {
        HashMap<String, String> funcodeMeaningMap;
        if ((funcodeMeaningMap = CONFIGURATION_MAP.get(protocolName)) == null) {
            throw new ProtocolIdNotValidException("deleteCachedFuncodeByName " + protocolName + " protocol name not exist and the CONFIGURATION_MAP is : \n" + CONFIGURATION_MAP);
        } else {
            funcodeMeaningMap.remove(funcode);
        }
    }

    public static void deleteCachedFuncodeById(int protocolId,
                                               String funcode) throws ProtocolIdNotValidException {
        String name = convertIdToName(protocolId);
        deleteCachedFuncodeByName(name, funcode);
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

    private static void doAddNewProtocolToCache(String protocol, int protocolId, HashMap<String, String> funCodeMeaningMap) throws ProtocolIdNotValidException {
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
    public static void addNewProtocolAndFuncodeMapToCache(String protocol, int protocolId, HashMap<String, String> funcodeMeaningMap) throws ProtocolIdNotValidException {
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
                                                    String funcode,
                                                    String opt) throws ProtocolIdNotValidException {
        HashMap<String, String> funcodeMeaningMap;
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
                                                  String funcode,
                                                  String opt) throws ProtocolIdNotValidException {
        String protocolName;
        if ((protocolName = PROTOCOL_STR_TO_INT.get(protocolId)) == null) {
            throw new ProtocolIdNotValidException(protocolId + " protocol id not exist and the PROTOCOL_STR_TO_INT is : \n" + PROTOCOL_STR_TO_INT);
        } else {
            HashMap<String, String> funcodeMeaningMap;
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
    public static ConcurrentHashMap<String, StatisticInfoSaveBean> getStatisticsInfoBean(){
        return STATISTICS_INFO_BEAN;
    }
    /**
     * @param deviceNumber 设备Number
     * @param deviceTag    device的ip地址或者mac地址
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

    public static String getDeviceNameByDeviceTag(String deviceTag){
        return ALL_DEVICES.get(deviceTag).getDeviceInfo();
    }

    public static String getTargetDeviceTagByNumber(String deviceNumber) {
        return DEVICE_NUMBER_TO_TAG.get(deviceNumber);
    }

    public static void removeAllCachedDeviceNumber() {
        DEVICE_NUMBER_TO_TAG.clear();
        STATISTICS_INFO_BEAN.clear();
    }

    /*********************************
     *  CACHED SHOW GRAPH ARGS
     **********************************/

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
        FiveDimensionAnalyzer fiveDimensionAnalyzer = getAnalyzerByTag(layer.ip_dst[0],layer.ip_src[0]);
        if (fiveDimensionAnalyzer == null && !layer.ip_dst[0].equals("--")) {
            //报文有IP地址
            return null;
        }
        return getAnalyzerByTag(layer.eth_dst[0],layer.eth_src[0]);
    }

    public static Map<String,FiveDimensionAnalyzer> getSrcAnalyzerMap(FvDimensionLayer layer){
        Map<String,FiveDimensionAnalyzer> fiveDimensionAnalyzerMap = Common.FV_DIMENSION_FILTER_PRO.get(layer.ip_dst[0]);
        if (fiveDimensionAnalyzerMap == null){
            return Common.FV_DIMENSION_FILTER_PRO.get(layer.eth_dst[0]);
        }
        return fiveDimensionAnalyzerMap;
    }

    /**
     *
     * @param srcAnalyzeMap can not be null
     * @param layer 五元组
     * @return
     */
    public static FiveDimensionAnalyzer getAnalyzerBySrcTag(Map<String,FiveDimensionAnalyzer> srcAnalyzeMap , FvDimensionLayer layer){
        FiveDimensionAnalyzer fiveDimensionAnalyzer = srcAnalyzeMap.get(layer.ip_src[0]);
        if (fiveDimensionAnalyzer == null)
        {
            return srcAnalyzeMap.get(layer.eth_src[0]);
        }
        return fiveDimensionAnalyzer;
    }

    private static FiveDimensionAnalyzer getAnalyzerByTag(String dstTag,String srcTag){
        Map<String,FiveDimensionAnalyzer> fiveDimensionAnalyzerMap = Common.FV_DIMENSION_FILTER_PRO.get(dstTag);
        if (fiveDimensionAnalyzerMap!=null){
            return fiveDimensionAnalyzerMap.get(srcTag);
        }
        return null;
    }

    /**********************************
     *
     * 获取功能码过滤器 key是协议，OperationAnalyzer是分析器
     *
     *********************************/
    /**
     * @param layer
     * @return null ==> 没有针对该目的设备配置过功能码规则或者没有配置过源设备
     */
    public static ConcurrentHashMap<String, OperationAnalyzer> getOptAnalyzer(FvDimensionLayer layer) {
        ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> map = getSrcOptAnalyzer(layer);
        if (map == null){
            return null;    //没有针对该目的设备配置过对应的源设备的分析器
        }
        ConcurrentHashMap<String,OperationAnalyzer> targetMap = map.get(layer.ip_src[0]);
        if (targetMap == null){
            return map.get(layer.eth_src[0]);
        }
        return targetMap;
    }

    /**
     * @param layer
     * 根据目的地址获取【源地址，【协议，分析器】】组
     * @return
     */
    private static ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> getSrcOptAnalyzer(FvDimensionLayer layer){
        ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> map = OptFilterUtil.getSrcAnalyzerByDstTag(layer.ip_dst[0]);
        if (map == null){
            return OptFilterUtil.getSrcAnalyzerByDstTag(layer.eth_dst[0]);
        }
        return map;
    }


    private static final byte[] LOCK_RIGHT_PROTOCOL_LIST = new byte[1];
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

    private static final byte[] NORMAL_LOCK = new byte[1];

    public static void addNormalRightPacketInfo(RightPacketInfo rightPacketInfo){
        synchronized (NORMAL_LOCK){
            String protocol = rightPacketInfo.getProtocol();
            HashMap<RightPacketInfo,String> rightPacketInfos = RIGHT_PACKET_INFOS.computeIfAbsent(protocol, k -> new HashMap<>());
            rightPacketInfos.put(rightPacketInfo,"");
        }
    }
    public static void removeNormalRightPacketInfo(RightPacketInfo packetInfo){
        synchronized (NORMAL_LOCK){
            String protocol = packetInfo.getProtocol();
            Map<RightPacketInfo,String> map = RIGHT_PACKET_INFOS.get(protocol);
            if (map == null){
                return;
            }
            map.remove(packetInfo);
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
     * 攻击IP地址TOP5 key : 攻击IP地址 value 攻击数目
     * 被攻击IP地址TOP5 key : 被攻击IP地址 value 被攻击数目
     * 攻击利用协议TOP5 key : 攻击利用协议 value 攻击数目
     *
     ***********************************/
    private static final ConcurrentHashMap<String,Integer> ATTACK_TYPE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,Integer> ATTACK_IPS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,Integer> ATTACKED_IPS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,Integer> ATTACK_PROTOCOL = new ConcurrentHashMap<>();
    public static void addNewAttackBean(AttackBean attackBean)
    {
        newAttackCome(ATTACK_TYPE,attackBean.getAttackType());
        if (!attackBean.getSrcIp().equals("--")) {
            newAttackCome(ATTACK_IPS, attackBean.getSrcIp());
        }else{
            newAttackCome(ATTACK_IPS, attackBean.getSrcMac());
        }
        if (!attackBean.getDstIp().equals("--")){
            newAttackCome(ATTACKED_IPS,attackBean.getDstIp());
        }else{
            newAttackCome(ATTACKED_IPS,attackBean.getDstMac());
        }
        newAttackCome(ATTACK_PROTOCOL,attackBean.getProtocolName());
    }
    private static void newAttackCome(Map<String,Integer> map,String key){
        int attackNum = map.computeIfAbsent(key,k -> 0);
        map.put(key,++attackNum);
    }
    //FIRST 最大，LAST最小
    private static final LinkedList<Top5Statistic.Top5Wrapper> ATTACK_TYPE_LIST = new LinkedList<>();
    private static final LinkedList<Top5Statistic.Top5Wrapper> ATTACK_IPS_LIST = new LinkedList<>();
    private static final LinkedList<Top5Statistic.Top5Wrapper> ATTACKED_IPS_LIST = new LinkedList<>();
    private static final LinkedList<Top5Statistic.Top5Wrapper> ATTACK_PROTOCOL_LIST = new LinkedList<>();
    private static Top5Statistic top5Statistic = new Top5Statistic();

    //单线程统计
    public static Top5Statistic getTop5StatisticsData(){
        ATTACK_TYPE_LIST.clear();
        ATTACK_IPS_LIST.clear();
        ATTACKED_IPS_LIST.clear();
        ATTACK_PROTOCOL_LIST.clear();

        ATTACK_TYPE.forEach((type, count) -> {
            getTop5List(ATTACK_TYPE_LIST,type,count);
        });
        ATTACK_IPS.forEach((type, count) -> {
            getTop5List(ATTACK_IPS_LIST,type,count);
        });
        ATTACKED_IPS.forEach((type, count) -> {
            getTop5List(ATTACKED_IPS_LIST,type,count);
        });
        ATTACK_PROTOCOL.forEach((type, count) -> {
            getTop5List(ATTACK_PROTOCOL_LIST,type,count);
        });

        top5Statistic.setAttackType(ATTACK_TYPE_LIST);
        top5Statistic.setAttackIps(ATTACK_IPS_LIST);
        top5Statistic.setAttackedIps(ATTACKED_IPS_LIST);
        top5Statistic.setAttackProtocol(ATTACK_PROTOCOL_LIST);
        return top5Statistic;
    }

    private static void getTop5List(LinkedList<Top5Statistic.Top5Wrapper> list , String type , int count){
        if (list.size() == 0){
            list.add(new Top5Statistic.Top5Wrapper(type,count));
        }else{
            if (list.getLast().getCount() < count){
                //从first开始
                if (list.size() == 5) {
                    list.removeLast();
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCount() < count){
                        list.add(i,new Top5Statistic.Top5Wrapper(type,count));
                        break;
                    }
                }
            }
        }
    }

    public static void updateAttackLog(){
        SocketServiceCenter.updateAllClient(SocketIoEvent.ATTACK_STATISTICS,ATTACK_TYPE);
    }

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

    public static void addOrUpdateDeviceManually(Device device){
        ALL_DEVICES.put(device.getDeviceTag(),device);
        ALL_MAC_ADDRESS.put(device.getDeviceMac(),"");
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

    public static int getAllDeviceCount(){
        return ALL_DEVICES.size();
    }

    public static void addMacAddress(String srcMacAddress){
        ALL_MAC_ADDRESS.put(srcMacAddress,"");
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
//                dstDevice = createDevice更新(layer);
//                ALL_DEVICES.put(dstDevice.getDeviceTag(), dstDevice);
//                //new device
//                CommonCacheUtil.addOrUpdateDeviceNumberAndTAG(dstDevice.getDeviceNumber(), dstDevice.getDeviceTag());
//                CommonCacheUtil.addDeviceNumberToName(dstDevice.getDeviceNumber(), dstDevice.getDeviceInfo());
//            }
//        }
        if (/*CommonCacheUtil.iProtocolExist(layer.protocol) && */DeviceOptUtil.validPacketInfo(layer)){
            String deviceTag = DeviceOptUtil.getSrcDeviceTag(layer);
            Device srcDevice;
            if (((ALL_DEVICES.putIfAbsent(deviceTag,(srcDevice = createDeviceInverse(layer, deviceTag)))) == null/*不存在该设备tag*/)
                &&
                ALL_MAC_ADDRESS.putIfAbsent(layer.eth_src[0],"") == null/*未存过该MAC地址*/) {
                //缓存中的设备名和设备TAG
                addOrUpdateDeviceNumberAndTAG(srcDevice.getDeviceNumber(),srcDevice.getDeviceTag());
                //缓存找那个的设备Number和Name更新
                addDeviceNumberToName(srcDevice.getDeviceNumber(),srcDevice.getDeviceInfo());
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

    public static void removeDeviceCachedMac(String macAddress){
        ALL_MAC_ADDRESS.remove(macAddress);
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
        Date date = new Date();
        sb.append("设备").append(CommonUtil.getDateFormat2().format(date))
                .append("_").append(threadLocalRandom.nextInt(10000));
        device.setDeviceInfo(sb.toString());
        device.setDeviceNumber(String.valueOf(timeNow));
        device.setDeviceType(1);
        device.setDeviceMac(layer.eth_src[0]);
        device.setDeviceTag(deviceTag);
        device.setGPlotId(Common.GPLOT_ID);
        device.setDeviceIp(layer.ip_src[0]);
        device.setProtocol(layer.protocol);
        device.setCreateTime(CommonUtil.getDateFormat3().format(date));
        return device;
    }


    public static void addTargetDevicePacket(FvDimensionLayer layer){
        String srcTag = DeviceOptUtil.getSrcDeviceTag(layer);
        Device srcDevice = getDeviceByTag(srcTag);        //发送设备
        String dstTag = DeviceOptUtil.getDstDeviceTag(layer);
        Device dstDevice = getDeviceByTag(dstTag);        //接收设备
        if (srcDevice!=null && dstDevice!=null) {
            ConcurrentHashMap<Device, AtomicInteger> device2PacketNum =
                    DEVICE_TO_DEVICE_PACKETS.computeIfAbsent(dstDevice, device -> new ConcurrentHashMap<>());
            AtomicInteger atomicInteger = device2PacketNum.putIfAbsent(srcDevice, new AtomicInteger(1));
            if (atomicInteger != null) {
                atomicInteger.incrementAndGet();
            }
        }
    }

    public static void removeDeviceToDevicePacketPair(Device device){
        //删除key
        DEVICE_TO_DEVICE_PACKETS.remove(device);
        //删除value
        DEVICE_TO_DEVICE_PACKETS.forEach((srcDevice, deviceAtomicIntegerConcurrentHashMap) -> {
            deviceAtomicIntegerConcurrentHashMap.remove(device);
        });
    }

    public static ConcurrentHashMap<Device,ConcurrentHashMap<Device, AtomicInteger>> getDeviceToDevicePackets(){
        return DEVICE_TO_DEVICE_PACKETS;
    }

    public static void initIProtocol(List<String> protocols){
        for (String protocol : protocols) {
            ALL_IPROTOCOL.put(protocol,protocol);
        }
    }
    public static boolean iProtocolExist(String protocolName){
        return ALL_IPROTOCOL.containsKey(protocolName);
    }

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
        CacheUtil.fvDimensionFilterCondition = fvDimensionFilterCondition;
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

    public static void addOrUpdateDeviceMaxFlow(DeviceMaxFlow deviceMaxFlowError){
        DEVICE_MAX_FLOW.put(deviceMaxFlowError.getDeviceNumber(),deviceMaxFlowError);
    }
    private static final DeviceMaxFlow DEVICE_MAX_FLOW_ERROR = new DeviceMaxFlow();
    public static DeviceMaxFlow getDeviceMaxFlow(String deviceNumber){
        DeviceMaxFlow deviceMaxFlowError = DEVICE_MAX_FLOW.get(deviceNumber);
        return deviceMaxFlowError == null ? DEVICE_MAX_FLOW_ERROR : deviceMaxFlowError;
    }

    private static double maxCPURate = 75;  //75%
    private static double maxMemoryRate = 75;   //75%
    private static int cpuCount = 30;   // 1分钟
    private static int memCount = 30;   // 1分钟
    private static int cpuCurCount = 0;
    private static int memCurCount = 0;
    private static final byte[] RUN_STATE_LOCK = new byte[1];
    public static SysRunArg getSysRunConfig(){
        initCpuAndMemState();
        SysRunArg sysRunArg = new SysRunArg();
        sysRunArg.setCpuCount(cpuCount);
        sysRunArg.setMemCount(memCount);
        sysRunArg.setCpuRate(maxCPURate);
        sysRunArg.setMemRate(maxMemoryRate);
        return sysRunArg;
    }
    public static void initCpuAndMemState(){
        maxCPURate = Double.parseDouble(ConfigUtil.getData("cpu_rate","75"));
        maxMemoryRate = Double.parseDouble(ConfigUtil.getData("mem_rate","75"));
        cpuCount = Integer.parseInt(ConfigUtil.getData("cpu","30"));
        memCount = Integer.parseInt(ConfigUtil.getData("mem","30"));
    }
    public static void updateCpuOrMemoryRate(double cpu,double mem){
        maxCPURate = cpu;
        maxMemoryRate = mem;
        ConfigUtil.setData("cpu_rate",String.valueOf(cpu));
        ConfigUtil.setData("mem_rate",String.valueOf(mem));
    }
    public static void updateCpuOrMemCount(int cpu,int mem){
        cpuCount = cpu;
        memCount = mem;
        ConfigUtil.setData("cpu",String.valueOf(cpu));
        ConfigUtil.setData("mem",String.valueOf(mem));
    }

    /**
     * @param stateWrapper 当前运行状态
     * @return -1 正常 1 CPU异常 2 内存异常 3 CPU和内存均异常
     */
    public static int runStateDetect(SysRunStateUtil.StateWrapper stateWrapper){
        int state = -1;
        synchronized (RUN_STATE_LOCK){
            if (stateWrapper.getCpu() >= maxCPURate){
                cpuCurCount++;
                if (cpuCurCount >= cpuCount){
                    state = 1;
                }
            }else{
                cpuCurCount = 0;
            }
            if (stateWrapper.getMemoryWrapper().getPercent() >= maxMemoryRate){
                memCurCount++;
                if (memCurCount >= memCount){
                    if (state == 1){
                        state = 3;
                    }else{
                        state = 2;
                    }
                }
            }else{
                memCurCount = 0;
            }
        }
        return state;
    }

    public static void addD2DAttackPair(String dstDeviceNumber,String srcDeviceNumber){
        StringBuilder sb = CommonUtil.getGlobalStringBuilder();
        sb.append(dstDeviceNumber).append("-").append(srcDeviceNumber);
        D2DAttackPair.putIfAbsent(sb.toString(),"");
    }
    public static void clearD2DAttackPair(){
        D2DAttackPair.clear();
    }
    public static boolean d2DAttackExist(String dstDeviceNumber,String srcDeviceNumber){
        StringBuilder sb = CommonUtil.getGlobalStringBuilder();
        sb.append(dstDeviceNumber).append("-").append(srcDeviceNumber);
        return D2DAttackPair.containsKey(sb.toString());
    }
    public static int getAttackedDeviceCount(){
        return D2DAttackPair.size();
    }

    /*********************************
     *
     *********************************/
    public static void addOrUpdateArtName2ArtGroup(String artName,String artGroup){
        ART_NAME_TO_GROUP.put(artName, artGroup);
    }

    public static String getArtGroupByArtName(String artName){
        String artGroup = ART_NAME_TO_GROUP.get(artName);
        if (artGroup == null)
        {
            return "default";
        }
        return artGroup;
    }

    public static void removeArtGroup(List<String> artNames){
        for (String artName : artNames) {
            ART_NAME_TO_GROUP.put(artName,"default");
        }
    }

    public static void removeArtNameInGroup(String artName){
        ART_NAME_TO_GROUP.remove(artName);
    }
}