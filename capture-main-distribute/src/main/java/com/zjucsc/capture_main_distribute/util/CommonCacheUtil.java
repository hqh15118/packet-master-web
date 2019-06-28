package com.zjucsc.capture_main_distribute.util;

import com.zjucsc.capture_main_distribute.bean.StatisticInfoSaveBean;
import com.zjucsc.capture_main_distribute.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.capture_main_distribute.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import static com.zjucsc.capture_main_distribute.CaptureCommon.*;

@Slf4j
public class CommonCacheUtil {

    /*********************************
     *
     *  CONFIGURATION_MAP
     *
     **********************************/

    public static void deleteCachedProtocolByName(String protocolName){
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
        HashMap<Integer,String> funcodeMeaningMap;
        if ((funcodeMeaningMap = CONFIGURATION_MAP.get(protocolName)) == null){
            throw new ProtocolIdNotValidException("deleteCachedFuncodeByName " + protocolName + " protocol name not exist and the CONFIGURATION_MAP is : \n" + CONFIGURATION_MAP);
        }else{
            funcodeMeaningMap.remove(funcode);
        }
        log.info("delete funcode [{}] of protocol [{}] " , funcode , protocolName);
    }

    public static void deleteCachedFuncodeById(int protocolId,
                                                 int funcode) throws ProtocolIdNotValidException {
        String name = convertIdToName(protocolId);
        deleteCachedFuncodeByName(name,funcode);
        log.info("【接上】delete funcode [{}] of protocol [{}]" , funcode , name);
    }

    /**
     * 添加新的协议，不增加该协议的功能码
     * @param protocol
     * @param protocolId
     */
    public static void addNewProtocolToCache(String protocol,
                                          int protocolId) throws ProtocolIdNotValidException {
        doAddNewProtocolToCache(protocol,protocolId,null);
    }

    private static void doAddNewProtocolToCache(String protocol,int protocolId,HashMap<Integer,String> funCodeMeaningMap) throws ProtocolIdNotValidException {
        if (CONFIGURATION_MAP.get(protocol) != null || PROTOCOL_STR_TO_INT.get(protocolId)!=null){
            throw new ProtocolIdNotValidException("protocol " + protocol + " is not valid , cause it exists in CONFIGURATION_MAP or PROTOCOL_STR_TO_INT \n CONFIGURATION_MAP is" +
                    CONFIGURATION_MAP + " PROTOCOL_STR_TO_INT" + PROTOCOL_STR_TO_INT);
        }else{
            if (funCodeMeaningMap == null) {
                CONFIGURATION_MAP.put(protocol, new HashMap<>());
            }else {
                CONFIGURATION_MAP.put(protocol, funCodeMeaningMap);
            }
            addNewProtocolToId(protocol,protocolId);
        }
    }

    /**
     * 添加新有协议，同时添加该协议对应的功能码
     * @param protocol
     * @param protocolId
     * @param funcodeMeaningMap
     */
    public static void addNewProtocolAndFuncodeMapToCache(String protocol,int protocolId,HashMap<Integer,String> funcodeMeaningMap) throws ProtocolIdNotValidException {
        doAddNewProtocolToCache(protocol,protocolId , funcodeMeaningMap);
    }

    /**
     * 增加已有协议对应的功能码-操作
     * @param protocol
     * @param funcode
     * @param opt
     * @throws ProtocolIdNotValidException
     */
    public static void updateOldProtocolCacheByName(String protocol,
                                              int funcode,
                                              String opt) throws ProtocolIdNotValidException {
        HashMap<Integer,String> funcodeMeaningMap;

        if ((funcodeMeaningMap = CONFIGURATION_MAP.get(protocol)) == null){
            throw new ProtocolIdNotValidException("updateOldProtocolCacheByName " + protocol + " protocol name not exist and the CONFIGURATION_MAP is : \n" + CONFIGURATION_MAP);
        }else{
            funcodeMeaningMap.put(funcode,opt);
        }
        log.info("update CONFIGURATION_MAP protocol : {} funcode : {}  opt : {}" , protocol , funcode , opt);
    }

    /**
     *
     * @param protocolId
     * @param funcode
     * @param opt
     * @throws ProtocolIdNotValidException
     */
    public static void updateOldProtocolCacheById(int protocolId,
                                                  int funcode,
                                                  String opt) throws ProtocolIdNotValidException {
        String protocolName;
        if ((protocolName = PROTOCOL_STR_TO_INT.get(protocolId)) == null){
            throw new ProtocolIdNotValidException(protocolId + " protocol id not exist and the PROTOCOL_STR_TO_INT is : \n" + PROTOCOL_STR_TO_INT);
        }else{
            HashMap<Integer,String> funcodeMeaningMap;
            if ((funcodeMeaningMap = CONFIGURATION_MAP.get(protocolName)) == null){
                throw new ProtocolIdNotValidException(protocolName + "protocol name not exist and the CONFIGURATION_MAP is : \n" + CONFIGURATION_MAP);
            }else{
                funcodeMeaningMap.put(funcode,opt);
            }
        }
        log.info("update PROTOCOL_STR_TO_INT protocol : {} funcode : {}  opt : {}" , protocolId , funcode , opt);
    }


    /*********************************
     *
     *  PROTOCOL_STR_TO_INT
     *
     **********************************/

    /**
     *
     * @param protocolId
     * @return
     * @throws ProtocolIdNotValidException
     */

    public static String convertIdToName(int protocolId) throws ProtocolIdNotValidException {
        if (PROTOCOL_STR_TO_INT.get(protocolId) == null){
            throw new ProtocolIdNotValidException(protocolId + " << PROTOCOL ID not exist and the PROTOCOL_STR_TO_INT is : \n" + PROTOCOL_STR_TO_INT);
        }
        return PROTOCOL_STR_TO_INT.get(protocolId);
    }

    public static int convertNameToId(String protocolName) throws ProtocolIdNotValidException {
        if (PROTOCOL_STR_TO_INT.inverse().get(protocolName) == null){
            throw new ProtocolIdNotValidException(protocolName + " << PROTOCOL NAME not exist and the PROTOCOL_STR_TO_INT is : \n" + PROTOCOL_STR_TO_INT);
        }
        return PROTOCOL_STR_TO_INT.inverse().get(protocolName);
    }


    public static boolean protocolExist(String protocolName){
        try {
            convertNameToId(protocolName);
        } catch (ProtocolIdNotValidException e) {
            return false;
        }
        return true;
    }

    public static void addNewProtocolToId(String protocolName , int protocolId) throws ProtocolIdNotValidException {
        if (PROTOCOL_STR_TO_INT.get(protocolId)!=null){
            throw new ProtocolIdNotValidException(protocolId + " << is existed ,can not update PROTOCOL_STR_TO_INT");
        }
        PROTOCOL_STR_TO_INT.put(protocolId,protocolName);
        log.info("add new protocol into PROTOCOL_STR_TO_INT ; protocolId : {} ; protocolName : {} " , protocolId , protocolName);
    }

    /*********************************
     *
     *  DEVICE_IP_TO_NAME
     *
     **********************************/
    /**
     *
     * @param deviceNumber
     * @param deviceTag device 的 ip地址或者mac地址
     */
    public static void addOrUpdateDeviceNumberAndTAG(String deviceNumber , String deviceTag){
        DEVICE_TAG_TO_NAME.put(deviceTag , deviceNumber);
        STATISTICS_INFO_BEAN.put(deviceNumber,new StatisticInfoSaveBean());
        log.info("add device number : {} [tag : {} ] and DEVICE_TAG_TO_NAME {}" , deviceNumber , deviceTag , DEVICE_TAG_TO_NAME);
    }

    public static void removeDeviceNumer(String deviceNumber){
        DEVICE_TAG_TO_NAME.remove(deviceNumber);
        STATISTICS_INFO_BEAN.remove(deviceNumber);
        log.info("remove device number : {} [TAG : {} ]" , deviceNumber , DEVICE_TAG_TO_NAME.inverse().get(deviceNumber));
    }

    public static String getTargetDeviceNumberByTag(String tag){
        return DEVICE_TAG_TO_NAME.get(tag);
    }

    public static String getTargetDeviceNumberByTag(FvDimensionLayer layer) {
        //System.out.println("get " + deviceIp + "xxxxxx" + AttackDEVICE_IP_TO_NAME.get(deviceIp));
        String tag = DEVICE_TAG_TO_NAME.get(layer.ip_dst[0]);
        if (tag!=null){
            return tag;
        }
        tag = DEVICE_TAG_TO_NAME.get(layer.eth_dst[0]);
        return tag;
    }

    public static String getTargetDeviceTagByNumber(String deviceNumber){
        return DEVICE_TAG_TO_NAME.inverse().get(deviceNumber);
    }

    public static void removeAllCachedDeviceNumber(){
        DEVICE_TAG_TO_NAME.clear();
        STATISTICS_INFO_BEAN.clear();
    }

    /*********************************
     *
     *  CACHED SHOW GRAPH ARGS
     *
     **********************************/
    public static void addShowGraphArg(int protocolId,String artArg){
        SHOW_GRAPH_SET.add(artArg);
        log.info("添加图表展示：协议 {} 下，工艺参数 {} ",protocolId,artArg);
    }

    public static boolean removeShowGraph(int protocolId,String artArg){
        if(SHOW_GRAPH_SET.remove(artArg)){
            log.info("取消协议 {} 下，工艺参数 {} 图表展示",protocolId,artArg);
            return true;
        }else{
            log.info("取消协议 {} 下，工艺参数 {} 图表展示失败，set中不存在名为{}的key值",protocolId,artArg,artArg);
            return false;
        }
    }

    /*********************************
     *
     *  CACHED ART DECODE INSTANCE
     *
     **********************************/
    
    /**********************************
     *
     * AttackSTATISTICS_INFO_BEAN 发送到数据库的统计信息Map
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
    public static void resetSaveBean(){
        STATISTICS_INFO_BEAN.forEach(biConsumer);
    }

    /**********************************
     *
     * 设置 SCHEDULE SERVICE 运行状态
     *
     *********************************/
    public static void setScheduleServiceRunningState(boolean running){
        SCHEDULE_RUNNING = running;
    }

    public static boolean getScheduleServiceRunningState(){
        return SCHEDULE_RUNNING;
    }

    /**********************************
     *
     * 获取五元组过滤器
     *
     *********************************/
    public static FiveDimensionAnalyzer getFvDimensionFilter(FvDimensionLayer layer){
        FiveDimensionAnalyzer fiveDimensionAnalyzer = FV_DIMENSION_FILTER_PRO.get(layer.ip_dst[0]);
        if (fiveDimensionAnalyzer!=null){
            return fiveDimensionAnalyzer;
        }
        return FV_DIMENSION_FILTER_PRO.get(layer.eth_dst[0]);
    }

    /**********************************
     *
     * 获取功能码过滤器
     *
     *********************************/
    public static ConcurrentHashMap<String, OperationAnalyzer> getOptAnalyzer(FvDimensionLayer layer){
        ConcurrentHashMap<String, OperationAnalyzer> map = OPERATION_FILTER_PRO.get(layer.ip_dst[0]);
        if (map != null){
            return map;
        }
        return OPERATION_FILTER_PRO.get(layer.eth_dst[0]);
    }
}
