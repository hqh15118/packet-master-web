package com.zjucsc.art_decode.dnp3;

import com.zjucsc.art_decode.artconfig.DNP3ConfigByTshark;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.tshark.packets.Dnp3_0Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DNP3DecodeByTshark extends BaseArtDecode<DNP3ConfigByTshark> {
    private ConcurrentHashMap<String,ConcurrentHashMap<String,Float>> dnp3ResultValue =
            new ConcurrentHashMap<String,ConcurrentHashMap<String,Float>>(){
                {
                    put("binary_output",new ConcurrentHashMap<>());
                    put("binary_input",new ConcurrentHashMap<>());
                    put("analog_input",new ConcurrentHashMap<>());
                }
            };

    private static final String BINARY_OUTPUT = "binary_output",
            BINARY_INPUT = "binary_input",
            ANALOG_INPUT = "analog_input";

    private static HashMap<String,HashMap<String,String>> objVarAndPointIndex2ID = new HashMap<>(3);

    static {
        objVarAndPointIndex2ID.put(BINARY_OUTPUT,new HashMap<>(0));
        objVarAndPointIndex2ID.put(BINARY_INPUT,new HashMap<>(0));
        objVarAndPointIndex2ID.put(ANALOG_INPUT,new HashMap<>(0));
        setDNP3MapperFile();
    }

    private StringBuilder sb = new StringBuilder();

    public static void setDNP3MapperFile(){
        HashMap<String,HashMap<String,String>> map = new HashMap<>(3);
        map.put(BINARY_OUTPUT,new HashMap<>());
        map.put(BINARY_INPUT,new HashMap<>());
        map.put(ANALOG_INPUT,new HashMap<>());
        File file = new File("config/dnp3mapper");
        if (!file.exists()){
            throw new DNP3MapperFileNotFoundException("路径{" + file.getAbsolutePath() + "}不存在dnp3mapper");
        }
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                StandardCharsets.UTF_8))){
            String data;
            String type;
            Map<String,String> typeMap = null;
            while (true){
                data = bf.readLine();
                if (data == null){
                    break;
                }
                data = data.trim();
                if (data .equals("") ){
                    continue;
                }
                if (data.startsWith("#") || data.equals("\n") ){
                    continue;
                }
                if (data.startsWith(">")){
                    type = data.replace(">","");
                    typeMap = map.get(type.trim());
                    assert typeMap!=null;
                    continue;
                }
                if (typeMap == null){
                    continue;
                }
                String[] pointIndex2ID = data.split(":");
                assert pointIndex2ID.length == 2;
                typeMap.put(pointIndex2ID[0].trim(),pointIndex2ID[1].trim());
            }
            objVarAndPointIndex2ID = map;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("fail to init [dnp3 mapper]");
        }
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
            Dnp3_0Packet.LayersBean dnpPacketLayer = ((Dnp3_0Packet.LayersBean) layer);
            if (!storeDNP3Values(dnpPacketLayer)){
                resetValuesArrIndex();
                return globalMap;
            }
            System.out.println(dnp3ResultValue);
            Float config2Value = dnp3ResultValue.get(dnp3ConfigByTshark.getObjAndVar()).get(dnp3ConfigByTshark.getPointIndex());
            if (config2Value!=null){
                globalMap.put(dnp3ConfigByTshark.getTag(),config2Value);
                String id = objVarAndPointIndex2ID.get(dnp3ConfigByTshark.getObjAndVar()).get(dnp3ConfigByTshark.getPointIndex());
                if (id == null){
                    sb.delete(0,sb.length());
                    id = sb.append(dnp3ConfigByTshark.getObjAndVar()).append(dnp3ConfigByTshark.getPointIndex()).toString();
                }
                callback(dnp3ConfigByTshark.getTag(),config2Value,layer,"dnp3",
                        new DNP3Wrapper(id,config2Value));
            }
            resetValuesArrIndex();
        }
        return globalMap;
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
        Map<String,Float> typeMap;
        int offset;
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
            typeMap = dnp3ResultValue.get(valueType);   //binary_input/binary_output/analog_input map
            dnp3ValueWrapper = getDNP3ArrByObj(objAndVar);
            if (dnp3ValueWrapper == null){
                //skip unknown obj&var
                dnpPointIndex += offset;
                continue;
            }
            for (int j = dnpPointIndex; j < dnpPointIndex + offset; j++) {
                pointIndexStr = pointIndex[j];
                int result = Integer.parseInt(dnp3ValueWrapper.getNextValue());
                Float prevData = typeMap.put(pointIndexStr, (float) result);
                if (!valueChange && (prevData == null || prevData.intValue() != result)){
                    //stored value change and need to update global map
                    valueChange = true;
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

    private static class DNP3MapperFileNotFoundException extends RuntimeException{
        DNP3MapperFileNotFoundException(String msg){
            super(msg);
        }
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Float>> getDnp3ResultValue() {
        return dnp3ResultValue;
    }
}
