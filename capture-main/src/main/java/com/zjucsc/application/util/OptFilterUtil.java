package com.zjucsc.application.util;

import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OptFilterUtil {

    /**
     * cache6
     * String 设备TAG
     * String -> 协议
     * OperationAnalyzer -> 报文操作分析器
     */
    public static ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>>> OPERATION_FILTER_PRO =
            new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, OperationAnalyzer> getTargetProtocolToOperationAnalyzerByDeviceTag(String dstTag,String srcTag){
        ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> map = OPERATION_FILTER_PRO.get(dstTag);
        if (map == null){
            return null;
        }else{
            return map.get(srcTag);
        }
    }

    public static ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> getSrcAnalyzerByDstTag(String tag){
        return OPERATION_FILTER_PRO.get(tag);
    }
    /**
     *
     * @param deviceTag 设备的TAG，可能是IP也可能是MAC
     * @param optFilterForFront 过滤器集合
     * @param filterName 过滤器名字，随便写一个就好
     * @throws ProtocolIdNotValidException
     */
    @SuppressWarnings("unchecked")
    public static void addOrUpdateAnalyzer(String deviceTag , OptFilterForFront optFilterForFront , String filterName) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String,ConcurrentHashMap<String,OperationAnalyzer>> srcAnalyzeMap = OPERATION_FILTER_PRO.get(deviceTag);
        if (srcAnalyzeMap == null){
            srcAnalyzeMap = new ConcurrentHashMap<>();
            OPERATION_FILTER_PRO.put(deviceTag,srcAnalyzeMap);
        }
        ConcurrentHashMap<String, OperationAnalyzer> analyzerMap;
        String srcTag = !optFilterForFront.getSrcIp().equals("--") ? optFilterForFront.getSrcIp() : optFilterForFront.getSrcMac();
        if ((analyzerMap = OPERATION_FILTER_PRO.get(deviceTag).get(srcTag)) == null){
            //新建设备
            analyzerMap = new ConcurrentHashMap<>();
            srcAnalyzeMap.put(srcTag,analyzerMap);
            OPERATION_FILTER_PRO.put(deviceTag, srcAnalyzeMap);
        }
        String protocolName = CacheUtil.convertIdToName(optFilterForFront.getProtocolId());
        analyzerMap.putIfAbsent(protocolName,new OperationAnalyzer(new OperationPacketFilter<>(filterName)));
        OperationPacketFilter analyzer = analyzerMap.get(protocolName).getAnalyzer();
        analyzer.resetAllRule();
        for (String funCode : optFilterForFront.getFunCodes()) {
//            int filterType = optFilter.getFilterType();
//            if (filterType == 0){
//                //white
//                analyzerMap.get(protocolName).getAnalyzer().addWhiteRule(optFilter.getFunCode(),
//                        CommonConfigUtil.getTargetProtocolFuncodeMeaning(protocolName,optFilter.getFunCode()));
//            }else{
//                analyzerMap.get(protocolName).getAnalyzer().addBlackRule(optFilter.getFunCode(),
//                        CommonConfigUtil.getTargetProtocolFuncodeMeaning(protocolName,optFilter.getFunCode()));
//            }
            //white
            analyzer.addWhiteRule(funCode, ProtocolUtil.getTargetProtocolFuncodeMeaning(protocolName,funCode));
        }
    }

    public static void removeTargetDeviceAnalyzeFuncode(OptFilterForFront optFilterForFront) throws ProtocolIdNotValidException {
        String deviceTag = CacheUtil.getTargetDeviceTagByNumber(optFilterForFront.getDeviceNumber());
        String protocolName = CacheUtil.convertIdToName(optFilterForFront.getProtocolId());
        List<String> funCodes = optFilterForFront.getFunCodes();
        String srcTag = optFilterForFront.getSrcIp().equals("--") ? optFilterForFront.getSrcIp() : optFilterForFront.getSrcMac();
        for (String funCode : funCodes) {
            if (OPERATION_FILTER_PRO.containsKey(deviceTag)) {
                OPERATION_FILTER_PRO.get(deviceTag).get(protocolName).get(srcTag).getAnalyzer().getWhiteMap()
                        .remove(funCode);
            }
        }
    }


    /**
     * 删除某个设备对应的所有分析器
     * @param deviceTag
     */
    public static void disableTargetDeviceAnalyzer(String deviceTag){
        OPERATION_FILTER_PRO.remove(deviceTag);
    }

//    /**
//     * 删除某个设备下某个协议对应的分析器
//     * @param deviceTag
//     * @param protocolId
//     * @throws ProtocolIdNotValidException
//     */
//    public static void removeTargetDeviceAnalyzerProtocol(String deviceTag , int protocolId) throws ProtocolIdNotValidException {
//        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER_PRO.get(deviceTag);
//        if (removedMap != null){
//            removedMap.remove(convertIdToName(protocolId));
//        }
//    }

//    /**
//     * 删除某个设备 -> 某协议 -> 对应的功能码过滤项
//     * @param deviceTag
//     * @param funcode
//     * @param protocolId
//     * @throws ProtocolIdNotValidException
//     */
//    public static void removeTargetDeviceAnalyzerFuncode(String deviceTag , int funcode , int protocolId) throws ProtocolIdNotValidException, ProtocolIdNotValidException {
//        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER_PRO.get(deviceTag);
//        if (removedMap != null) {
//            OperationAnalyzer analyzer = removedMap.get(convertIdToName(protocolId));
//            analyzer.getAnalyzer().getBlackMap().remove(funcode);
//            analyzer.getAnalyzer().getWhiteMap().remove(funcode);
//        }
//    }

    public static void removeAllOptFilter(){
        OPERATION_FILTER_PRO.clear();
    }


    public static OptFilterForFront getOptFilterForFront(Rule rule) {
        OptFilterForFront optFilterForFront = new OptFilterForFront();
        List<String> funCodes = rule.getFunCodes();
        optFilterForFront.setDeviceNumber(rule.getFvDimensionFilter().getDeviceNumber());
        optFilterForFront.setFvId(rule.getFvDimensionFilter().getFvId());
        optFilterForFront.setProtocolId(rule.getFvDimensionFilter().getProtocolId());
        optFilterForFront.setFunCodes(funCodes);
        optFilterForFront.setSrcMac(rule.getFvDimensionFilter().getSrcMac());
        optFilterForFront.setSrcIp(rule.getFvDimensionFilter().getSrcIp());
        return optFilterForFront;
    }
}
