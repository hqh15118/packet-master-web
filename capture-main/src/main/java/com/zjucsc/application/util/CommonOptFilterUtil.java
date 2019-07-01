package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.common.exceptions.OptFilterNotValidException;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

import static com.zjucsc.application.config.Common.OPERATION_FILTER_PRO;
import static com.zjucsc.application.util.CommonCacheUtil.convertIdToName;

@Slf4j
public class CommonOptFilterUtil {

    /**
     *
     * @param deviceTag 设备的TAG，可能是IP也可能是MAC
     * @param optFilterForFront 过滤器集合
     * @param filterName 过滤器名字，随便写一个就好
     * @throws ProtocolIdNotValidException
     */
    public static void addOrUpdateAnalyzer(String deviceTag , OptFilterForFront optFilterForFront , String filterName) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String, OperationAnalyzer> analyzerMap;
        int type = 0;
        if ((analyzerMap = Common.OPERATION_FILTER_PRO.get(deviceTag)) == null){
            //新建设备
            analyzerMap = new ConcurrentHashMap<>();
            Common.OPERATION_FILTER_PRO.put(deviceTag, analyzerMap);
        }else{
            type = 1;
        }

        for (Integer funCode : optFilterForFront.getFunCodes()) {
            String protocolName = CommonCacheUtil.convertIdToName(optFilterForFront.getProtocolId());
            analyzerMap.putIfAbsent(protocolName,new OperationAnalyzer(new OperationPacketFilter<>(filterName)));
//            int filterType = optFilter.getFilterType();
//            if (filterType == 0){
//                //white
//                analyzerMap.get(protocolName).getAnalyzer().addWhiteRule(optFilter.getFunCode(),
//                        CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocolName,optFilter.getFunCode()));
//            }else{
//                analyzerMap.get(protocolName).getAnalyzer().addBlackRule(optFilter.getFunCode(),
//                        CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocolName,optFilter.getFunCode()));
//            }
            //white
            analyzerMap.get(protocolName).getAnalyzer().addWhiteRule(funCode,
                    CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocolName,funCode));
        }

        if (type == 0){
            //new analyze map
            log.info("新建设备：【{}】 的【功能码】过滤规则MAP , 新的规律规则为 \n 【{}】 ",deviceTag , analyzerMap);
        }else{
            log.info("更新设备：【{}】 【功能码】过滤规则MAP, 新的规律规则为 \n 【{}】 " , deviceTag, analyzerMap);
        }
    }


    public static void addOrUpdateAnalyzer(String deviceIp, String protocolName, OperationAnalyzer analyzer) throws OptFilterNotValidException {
        if (analyzer == null || analyzer.getAnalyzer() == null){
            throw new OptFilterNotValidException("OperationAnalyzer为空");
        }
        if (OPERATION_FILTER_PRO.get(deviceIp) == null){
            //不存在该设备ID对应的报文分析器
            ConcurrentHashMap<String, OperationAnalyzer> map = new ConcurrentHashMap<>();
            map.put(protocolName,analyzer);
            OPERATION_FILTER_PRO.put(deviceIp,map);
            log.info("[ANALYZER] ADD new operation filter [device_ip] {} [analyzer] {} " , deviceIp , map);
        }else{
            //存在该设备对应的报文分析器，就加入新的分析器，覆盖旧的
            OPERATION_FILTER_PRO.get(deviceIp).put(protocolName,analyzer);
            log.info("[ANALYZER] UPDATE operation filter [device_ip] {} [analyzer] {} " , deviceIp , analyzer);
        }
    }

    /**
     * 删除某个设备对应的所有分析器
     * @param deviceIp
     */
    public static void removeTargetDeviceAnalyzer(String deviceIp){
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER_PRO.remove(deviceIp);
        if (removedMap != null){
            log.info("[ANALYZER] REMOVE SUCCESSFULLY ==> device_ip {} 's operation analyzer :  {} \n " , deviceIp , removedMap);
        }else{
            log.info("[ANALYZER] **************REMOVE WARNING ! ==> device_ip {} 's operation analyzer \n cause device_id is not exist in the map " +
                    "OPERATION_FILTER : {}" , deviceIp , OPERATION_FILTER_PRO.get(deviceIp));
        }
    }

    /**
     * 删除某个设备下某个协议对应的分析器
     * @param deviceIp
     * @param protocolId
     * @throws ProtocolIdNotValidException
     */
    public static void removeTargetDeviceAnalyzerProtocol(String deviceIp , int protocolId) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER_PRO.get(deviceIp);
        if (removedMap == null){
            log.info("[ANALYZER] **************remove WARNING BY PROTOCOL ==>device id : [ {} ] protocol id : [ {} ] , protocol name : [ {} ] , cause can not find the target device id in OPERATION_FILTER MAP : {} " ,
                    deviceIp , protocolId , convertIdToName(protocolId) , OPERATION_FILTER_PRO.get(deviceIp));
        }else{
            removedMap.remove(convertIdToName(protocolId));
            log.info("[ANALYZER] **************remove SUCCESSFULLY BY DEVICE ID ==> device id : [ {} ] protocol id : [ {} ] , protocol name : [ {} ] by protocol id , and now  OPERATION_FILTER MAP is : {} " ,
                    deviceIp , protocolId , convertIdToName(protocolId) , OPERATION_FILTER_PRO.get(deviceIp));
        }
    }

    /**
     * 删除某个设备 -> 某协议 -> 对应的功能码过滤项
     * @param deviceIp
     * @param funcode
     * @param protocolId
     * @throws ProtocolIdNotValidException
     */
    public static void removeTargetDeviceAnalyzerFuncode(String deviceTag , int funcode , int protocolId) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER_PRO.get(deviceTag);
        if (removedMap == null){
            log.error("[ANALYZER]\n**************\nremove WARNING BY FUNCODE ==> device id: [ {} ] protocol id : [ {} ] , protocol name : [ {} ] , cause can not find the target device id in OPERATION_FILTER MAP : {} \n" +
                            "**************" ,
                    deviceTag , protocolId , convertIdToName(protocolId) , OPERATION_FILTER_PRO);
        }else{
            OperationAnalyzer analyzer = removedMap.get(convertIdToName(protocolId));
            analyzer.getAnalyzer().getBlackMap().remove(funcode);
            analyzer.getAnalyzer().getWhiteMap().remove(funcode);
            log.info("**************\nremove device id : [ {} ] protocol id : [ {} ] , protocol name : [ {} ]  , funcode : [ {} ], and now  OPERATION_FILTER MAP is : {} \n**************" ,
                    deviceTag , protocolId , convertIdToName(protocolId) , funcode , OPERATION_FILTER_PRO.get(deviceTag));
        }
    }

    /**
     * 获取指定设备、协议下对应的报文分析器
     * @param deviceIp
     * @param protocol
     * @return
     */
    public static OperationAnalyzer getTargetDeviceProtocolOptAnalyzer(String deviceIp , String protocol){
        return OPERATION_FILTER_PRO.get(deviceIp).get(protocol);
    }

    public static void removeAllOptFilter(){
        Common.OPERATION_FILTER_PRO.clear();
        log.info("change gplot so -> clear all opt dimension filter of gplot_id : [{}] " , Common.GPLOT_ID);
    }
}
