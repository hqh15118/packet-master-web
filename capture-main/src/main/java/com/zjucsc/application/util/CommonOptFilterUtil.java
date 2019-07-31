package com.zjucsc.application.util;

import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.common.exceptions.OptFilterNotValidException;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.zjucsc.application.config.Common.GPLOT_ID;
import static com.zjucsc.application.util.CommonCacheUtil.convertIdToName;

@Slf4j
public class CommonOptFilterUtil {

    /**
     * cache6
     * String 设备TAG
     * String -> 协议
     * OperationAnalyzer -> 报文操作分析器
     */
    public static ConcurrentHashMap<String,ConcurrentHashMap<String, OperationAnalyzer>> OPERATION_FILTER_PRO =
            new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, OperationAnalyzer> getTargetProtocolToOperationAnalyzerByDeviceTag(String tag){
        return OPERATION_FILTER_PRO.get(tag);
    }
    /**
     *
     * @param deviceTag 设备的TAG，可能是IP也可能是MAC
     * @param optFilterForFront 过滤器集合
     * @param filterName 过滤器名字，随便写一个就好
     * @throws ProtocolIdNotValidException
     */
    public static void addOrUpdateAnalyzer(String deviceTag , OptFilterForFront optFilterForFront , String filterName) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String, OperationAnalyzer> analyzerMap;
        if ((analyzerMap = OPERATION_FILTER_PRO.get(deviceTag)) == null){
            //新建设备
            analyzerMap = new ConcurrentHashMap<>();
            OPERATION_FILTER_PRO.put(deviceTag, analyzerMap);
        }
        for (Integer funCode : optFilterForFront.getFunCodes()) {
            String protocolName = CommonCacheUtil.convertIdToName(optFilterForFront.getProtocolId());
            analyzerMap.putIfAbsent(protocolName,new OperationAnalyzer(new OperationPacketFilter<>(filterName)));
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
            analyzerMap.get(protocolName).getAnalyzer().addWhiteRule(funCode,
                    CommonConfigUtil.getTargetProtocolFuncodeMeaning(protocolName,funCode));
        }

    }

    public static void removeTargetDeviceAnalyzeFuncode(OptFilterForFront optFilterForFront) throws ProtocolIdNotValidException {
        String deviceTag = CommonCacheUtil.getTargetDeviceTagByNumber(optFilterForFront.getDeviceNumber());
        String protocolName = CommonCacheUtil.convertIdToName(optFilterForFront.getProtocolId());
        List<Integer> funCodes = optFilterForFront.getFunCodes();
        for (Integer funCode : funCodes) {
            OPERATION_FILTER_PRO.get(deviceTag).get(protocolName).getAnalyzer().getWhiteMap()
                    .remove(funCode);
        }
    }


    public static void addOrUpdateAnalyzer(String deviceTag, String protocolName, OperationAnalyzer analyzer) throws OptFilterNotValidException {
        if (analyzer == null || analyzer.getAnalyzer() == null){
            throw new OptFilterNotValidException("OperationAnalyzer为空");
        }
        if (OPERATION_FILTER_PRO.get(deviceTag) == null){
            //不存在该设备ID对应的报文分析器
            ConcurrentHashMap<String, OperationAnalyzer> map = new ConcurrentHashMap<>();
            map.put(protocolName,analyzer);
            OPERATION_FILTER_PRO.put(deviceTag,map);
        }else{
            //存在该设备对应的报文分析器，就加入新的分析器，覆盖旧的
            OPERATION_FILTER_PRO.get(deviceTag).put(protocolName,analyzer);
        }
    }

    /**
     * 删除某个设备对应的所有分析器
     * @param deviceTag
     */
    public static void disableTargetDeviceAnalyzer(String deviceTag){
        OPERATION_FILTER_PRO.remove(deviceTag);
    }

    /**
     * 删除某个设备下某个协议对应的分析器
     * @param deviceTag
     * @param protocolId
     * @throws ProtocolIdNotValidException
     */
    public static void removeTargetDeviceAnalyzerProtocol(String deviceTag , int protocolId) throws ProtocolIdNotValidException {
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER_PRO.get(deviceTag);
        if (removedMap != null){
            removedMap.remove(convertIdToName(protocolId));
        }
    }

    /**
     * 删除某个设备 -> 某协议 -> 对应的功能码过滤项
     * @param deviceTag
     * @param funcode
     * @param protocolId
     * @throws ProtocolIdNotValidException
     */
    public static void removeTargetDeviceAnalyzerFuncode(String deviceTag , int funcode , int protocolId) throws ProtocolIdNotValidException, ProtocolIdNotValidException {
        ConcurrentHashMap<String,OperationAnalyzer> removedMap = OPERATION_FILTER_PRO.get(deviceTag);
        if (removedMap != null) {
            OperationAnalyzer analyzer = removedMap.get(convertIdToName(protocolId));
            analyzer.getAnalyzer().getBlackMap().remove(funcode);
            analyzer.getAnalyzer().getWhiteMap().remove(funcode);
        }
    }

    public static void removeAllOptFilter(){
        OPERATION_FILTER_PRO.clear();
    }
}
