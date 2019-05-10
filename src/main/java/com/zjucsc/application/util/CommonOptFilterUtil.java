package com.zjucsc.application.util;

import com.zjucsc.application.domain.analyzer.OperationAnalyzer;
import com.zjucsc.application.domain.exceptions.OptFilterNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

import static com.zjucsc.application.config.Common.OPERATION_FILTER;
import static com.zjucsc.application.util.CommonCacheUtil.convertIdToName;

@Slf4j
public class CommonOptFilterUtil {

    public static void addOrUpdateAnalyzer(int deviceId, String protocolName, OperationAnalyzer analyzer) throws OptFilterNotValidException {
        if (analyzer == null || analyzer.getAnalyzer() == null){
            throw new OptFilterNotValidException("OperationAnalyzer为空");
        }
        if (OPERATION_FILTER.get(deviceId) == null){
            //不存在该设备ID对应的报文分析器
            ConcurrentHashMap<String,OperationAnalyzer> map = new ConcurrentHashMap<>();
            map.put(protocolName,analyzer);
            OPERATION_FILTER.put(deviceId,map);
            log.info("[ANALYZER] ADD new operation filter [device_id] {} [analyzer] {} " , deviceId , map);
        }else{
            //存在该设备对应的报文分析器，就加入新的分析器，覆盖旧的
            OPERATION_FILTER.get(deviceId).put(protocolName,analyzer);
            log.info("[ANALYZER] UPDATE operation filter [device_id] {} [analyzer] {} " , deviceId , analyzer);
        }
    }

    /**
     * 删除某个设备对应的所有分析器
     * @param deviceId
     */
    public static void removeTargetDeviceAnalyzer(int deviceId){
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER.remove(deviceId);
        if (removedMap != null){
            log.info("[ANALYZER] REMOVE SUCCESSFULLY ==> device_id {} 's operation analyzer :  {} \n " , deviceId , removedMap);
        }else{
            log.info("[ANALYZER] **************REMOVE WARNING ! ==> device_id {} 's operation analyzer \n cause device_id is not exist in the map " +
                    "OPERATION_FILTER : {}" , deviceId , OPERATION_FILTER.get(deviceId));
        }
    }

    /**
     * 删除某个设备下某个协议对应的分析器
     * @param deviceId
     * @param protocolId
     * @throws ProtocolIdNotValidException
     */
    public static void removeTargetDeviceAnalyzerProtocol(int deviceId , int protocolId) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER.get(deviceId);
        if (removedMap == null){
            log.info("[ANALYZER] **************remove WARNING BY PROTOCOL ==>device id : [ {} ] protocol id : [ {} ] , protocol name : [ {} ] , cause can not find the target device id in OPERATION_FILTER MAP : {} " ,
                    deviceId , protocolId , convertIdToName(protocolId) , OPERATION_FILTER.get(deviceId));
        }else{
            removedMap.remove(convertIdToName(protocolId));
            log.info("[ANALYZER] **************remove SUCCESSFULLY BY DEVICE ID ==> device id : [ {} ] protocol id : [ {} ] , protocol name : [ {} ] by protocol id , and now  OPERATION_FILTER MAP is : {} " ,
                    deviceId , protocolId , convertIdToName(protocolId) , OPERATION_FILTER.get(deviceId));
        }
    }

    /**
     * 删除某个设备 -> 某协议 -> 对应的功能码过滤项
     * @param deviceId
     * @param funcode
     * @param protocolId
     * @throws ProtocolIdNotValidException
     */
    public static void removeTargetDeviceAnalyzerFuncode(int deviceId , int funcode , int protocolId) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER.get(deviceId);
        if (removedMap == null){
            log.info("[ANALYZER]**************remove WARNING BY FUNCODE ==> device id: [ {} ] protocol id : [ {} ] , protocol name : [ {} ] , cause can not find the target device id in OPERATION_FILTER MAP : {} " ,
                    deviceId , protocolId , convertIdToName(protocolId) , OPERATION_FILTER);
        }else{
            OperationAnalyzer analyzer = removedMap.get(convertIdToName(protocolId));
            analyzer.getAnalyzer().getBlackMap().remove(funcode);
            analyzer.getAnalyzer().getWhiteMap().remove(funcode);
            log.info("remove device id : [ {} ] protocol id : [ {} ] , protocol name : [ {} ]  , funcode : [ {} ], and now  OPERATION_FILTER MAP is : {} " ,
                    deviceId , protocolId , convertIdToName(protocolId) , funcode , OPERATION_FILTER.get(deviceId));
        }
    }

    /**
     * 获取指定设备、协议下对应的报文分析器
     * @param deviceId
     * @param protocol
     * @return
     */
    public static OperationAnalyzer getTargetDeviceProtocolOptAnalyzer(int deviceId , String protocol){
        return OPERATION_FILTER.get(deviceId).get(protocol);
    }
}
