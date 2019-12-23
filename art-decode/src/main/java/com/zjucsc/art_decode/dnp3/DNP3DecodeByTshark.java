package com.zjucsc.art_decode.dnp3;

import com.zjucsc.art_decode.artconfig.DNP3ConfigByTshark;
import com.zjucsc.art_decode.base.ElecBaseArtDecode;
import com.zjucsc.art_decode.util.mapper.DNP3Mapper;
import com.zjucsc.tshark.packets.Dnp3_0Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DNP3DecodeByTshark extends ElecBaseArtDecode<DNP3ConfigByTshark> {

    private DNPResultWrapper dnp3ResultValue = new DNPResultWrapper();

    private static DNP3Mapper dnp3Mapper = new DNP3Mapper();

    static {
        setDNP3MapperFile("config/dnp3mapper");
    }

    public static void setDNP3MapperFile(String mapperFilePath){
        dnp3Mapper.setMapperFilePath(mapperFilePath);
        dnp3Mapper.createMapper();
    }

    private static Logger logger = LoggerFactory.getLogger(DNP3DecodeByTshark.class);

    private String[] objAndVars,startIndex,stopIndex,pointIndex;

    private static Map<String,String> objAndVar2Tag = new HashMap<String,String>(){
        {
            put("2562","binary_output");
            put("7682","analog_input");
            put("258","binary_input");
            put("7684","analog_input");
        }
    };

    private int dnpPointIndex;

    private DNP3ValueWrapper binaryOutputPointValue = new DNP3ValueWrapper(),
                             analogValue = new DNP3ValueWrapper(),
                             binaryInputPointValue = new DNP3ValueWrapper();

    private void resetValuesArrIndex(){
        binaryOutputPointValue.reset();
        analogValue.reset();
        binaryInputPointValue.reset();
        dnpPointIndex = 0;
        objAndVars = null;
        startIndex = null;
        stopIndex= null;
        pointIndex = null;
    }

    @Override
    public Map<String, Float> decode(DNP3ConfigByTshark dnp3ConfigByTshark, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer layer, Object... obj) {
        if (layer instanceof Dnp3_0Packet.LayersBean){
            int configIndex = ((Integer) obj[0]);
            if (configIndex != 0){
                String ip = layer.ip_src[0];
                saveResultToArtMap(ip,dnp3ConfigByTshark,globalMap,layer);
            }
            Dnp3_0Packet.LayersBean dnpPacketLayer = ((Dnp3_0Packet.LayersBean) layer);
            if (!storeDNP3Values(dnpPacketLayer)){
                resetValuesArrIndex();
                return globalMap;
            }
            String ip = layer.ip_src[0];
            saveResultToArtMap(ip,dnp3ConfigByTshark,globalMap,layer);
            resetValuesArrIndex();
        }
        return globalMap;
    }

    private void saveResultToArtMap(String ip,DNP3ConfigByTshark dnp3ConfigByTshark,Map<String, Float> globalMap,FvDimensionLayer layer){
        Float config2Value = dnp3ResultValue.getResultValue(ip,dnp3ConfigByTshark.getObjAndVar(),dnp3ConfigByTshark.getPointIndex());
        if (config2Value!=null){
            Float prev = globalMap.put(dnp3ConfigByTshark.getTag(),config2Value);
            if (prev == null || !prev.equals(config2Value)){
                callback(dnp3ConfigByTshark.getTag(),config2Value,layer);
            }
        }
    }

    private DNP3ValueWrapper getDNP3ArrByObj(String objAndVar){
        switch (objAndVar) {
            case "2562" : return binaryOutputPointValue;
            case "7682" : return analogValue;
            case "7684" : return analogValue;
            case "258"  : return binaryInputPointValue;
        }
        return null;
    }

    /**
     * objAndVar1 -- 决定了下面的数据属于哪种类型
     *      -- point index -- 数据索引
     *          [] -- Value(analog)
     *          [] -- dnp3_al_boq_b7(binary)
     * objAndVar2
     * ...
     */
    private boolean storeDNP3Values(Dnp3_0Packet.LayersBean dnpPacketLayer) {
        boolean valueChange = false;
        objAndVars = dnpPacketLayer.objAndVars;
        if (objAndVars == null || objAndVars.length == 0){
            return false;
        }
        pointIndex = dnpPacketLayer.pointIndex;
        if (pointIndex == null || pointIndex.length == 0){
            return false;
        }
        binaryOutputPointValue.setValues(dnpPacketLayer.binaryOutputPointValue);
        analogValue.setValues(dnpPacketLayer.analogValue);
        binaryInputPointValue.setValues(dnpPacketLayer.binaryInputPointValue);
        startIndex = dnpPacketLayer.startIndex;
        stopIndex = dnpPacketLayer.stopIndex;
        int startIndexInt, stopIndexInt;
        String objAndVar;
        DNP3ValueWrapper dnp3ValueWrapper;
        String pointIndexStr;
        String valueType;
        int offset;
        String ip = dnpPacketLayer.ip_src[0];
        for (int i = 0,len = objAndVars.length; i < len; i++) {
            objAndVar = objAndVars[i];  //"2562" "7682" "258"
            valueType = objAndVar2Tag.get(objAndVar);   //binary_input/binary_output/analog_input
            startIndexInt = Integer.parseInt(startIndex[i]);
            stopIndexInt = Integer.parseInt(stopIndex[i]);
            offset = stopIndexInt - startIndexInt + 1;
            if (valueType == null){
                dnpPointIndex += offset;
                logger.error("【DNP3工艺参数解析】未定义[{}] objAndVar",objAndVar);
                continue;
            }
            dnp3ValueWrapper = getDNP3ArrByObj(objAndVar);
            if (dnp3ValueWrapper == null){
                //skip unknown obj&var
                dnpPointIndex += offset;
                continue;
            }
            for (int j = dnpPointIndex; j < dnpPointIndex + offset; j++) {
                pointIndexStr = pointIndex[j];
                int result = Integer.parseInt(dnp3ValueWrapper.getNextValue());
                boolean change = dnp3ResultValue.setResultValue(ip,valueType,pointIndexStr, (float)result);
                if (change){
                    //stored value change and need to update global map
                    if (!valueChange) {
                        valueChange = true;
                    }
                    String id = dnp3Mapper.getIDByIPAndTypeAndPointIndex(dnpPacketLayer.ip_src[0], objAndVar2Tag.get(objAndVar), pointIndexStr);
                    if (id!=null){
                        elecStatusChangeCallback("dnp3",new DNP3Wrapper(id,result,valueType,pointIndexStr,dnpPacketLayer.ip_src[0]));
                    }
                }
            }
            dnpPointIndex += offset;
        }
        return valueChange;
    }

    @Override
    public String protocol() {
        return "dnp3";
    }

    private static class DNP3ValueWrapper{
        private String[] values;
        private int index;

        public void setValues(String[] dnpValues){
            values = dnpValues;
        }

        public String getNextValue(){
            String result = values[index];
            index ++ ;
            return result;
        }

        public void reset(){
            values = null;
            index = 0;
        }
    }

    public DNPResultWrapper getDnp3ResultValue() {
        return dnp3ResultValue;
    }

    public static class DNPResultWrapper {
        //
        private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Float>>> dnp3ResultValue =
                new ConcurrentHashMap<>();

        public boolean setResultValue(String ip, String type, String pointIndex, float value) {
            ConcurrentHashMap<String, ConcurrentHashMap<String, Float>> type2PointIndex2ValueMap = dnp3ResultValue
                    .computeIfAbsent(ip, ip1 -> new ConcurrentHashMap<>());
            ConcurrentHashMap<String, Float> pointIndex2ValueMap = type2PointIndex2ValueMap.computeIfAbsent(type, type1 -> new ConcurrentHashMap<>());
            Float prev = pointIndex2ValueMap.put(pointIndex, value);
            return prev == null || prev != value;
        }

        public Float getResultValue(String ip,String type,String pointIndex){
            ConcurrentHashMap<String, ConcurrentHashMap<String, Float>> type2PointIndex2ValueMap = dnp3ResultValue.get(ip);
            if (type2PointIndex2ValueMap == null){
                return null;
            }
            ConcurrentHashMap<String, Float> pointIndex2ValueMap = type2PointIndex2ValueMap.get(type);
            if (pointIndex2ValueMap == null){
                return null;
            }
            return pointIndex2ValueMap.get(pointIndex);
        }

        public ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Float>>> getDnp3ResultValue(){
            return dnp3ResultValue;
        }
    }

}

