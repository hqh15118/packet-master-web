package com.zjucsc.art_decode.iec104;

import com.google.common.collect.HashBiMap;
import com.zjucsc.art_decode.artconfig.IEC104ConfigTshark;
import com.zjucsc.art_decode.base.ElecBaseArtDecode;
import com.zjucsc.art_decode.util.mapper.IEC104Mapper;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.IEC104Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * not thread safe
 * must single thread...
 */
public class IEC104DecodeByTshark extends ElecBaseArtDecode<IEC104ConfigTshark> {
    private static Logger logger = LoggerFactory.getLogger(IEC104DecodeByTshark.class);

    private IECResultMapWrapper iecResultMapWrapper = new IECResultMapWrapper();

    private static IEC104Mapper iec104Mapper = new IEC104Mapper();

    //read only

    static {
        setIEC104MapperFile("config/104mapper");
    }
    public static void setIEC104MapperFile(String iec104MapperFilePath){
        iec104Mapper.setMapperFilePath(iec104MapperFilePath);
        iec104Mapper.createMapper();
    }

    private IEC104ValuesWrapper /*controlPacketStatus = new IEC104ValuesWrapper()*/
                                scaleValues = new IEC104ValuesWrapper()
                                ,normalValues = new IEC104ValuesWrapper()
                                ,measureFloatValues = new IEC104ValuesWrapper()
                                ,singlePointValue = new IEC104ValuesWrapper();
    private String[] typeIds, numIxes, ioaAddresses;
    private int addressIndex = 0;

    private static final String SINGLE_POINT = "1",SHORT_FLOAT = "13",INTER_COMMAND = "100",
                    SCALE_VALUE = "11",NORMAL_VALUE = "9",CONTROL = "30";

    {
        singlePointValue.setSiq();
    }

    private static class IEC104ValuesWrapper{
        private String[] values;
        private int index;
        private boolean isSiq;
        public void setValues(String[] values){
            this.values = values;
        }
        void setSiq(){
            this.isSiq = true;
        }
        String getNextValue(){
            String result = values[index];
            index ++ ;
            return result;
        }
        public void reset(){
            index = 0;
            values = null;
        }
    }
    /**
     * "1" : single-point information
     * "13":short float
     * "100":interrogation command
     * "11": scale-value
     * "9" : normal value
     * @param typeId
     * @return
     */
    private IEC104ValuesWrapper getStringArrByTypeId(String typeId){
        switch (typeId){
            case SINGLE_POINT : return singlePointValue;
            case SHORT_FLOAT : return measureFloatValues;
            case SCALE_VALUE : return scaleValues;
            case NORMAL_VALUE : return normalValues;
            case CONTROL      : return singlePointValue;
            default:
                throw new IEC104DecodeException("无法通过typeId{" + typeId + "}获取到对应的数组值");
        }
    }

    private void resetValuesArrIndex(){
        singlePointValue.reset();
        measureFloatValues.reset();
        scaleValues.reset();
        normalValues.reset();
        addressIndex = 0;
    }

    @Override
    public Map<String, Float> decode(IEC104ConfigTshark iec104ConfigTshark, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer layer, Object... obj) {
        if (layer instanceof IEC104Packet.LayersBean){
            int configIndex = ((Integer) obj[0]);
            if (configIndex != 0){
                saveValueToArtMap(iec104ConfigTshark, layer, globalMap);
            }
            IEC104Packet.LayersBean layersBean = ((IEC104Packet.LayersBean) layer);
            ioaAddresses = layersBean.ioaAddress;
            measureFloatValues.setValues(layersBean.measureFloatValue);
            singlePointValue.setValues(layersBean.singlePointValue);
//            controlPacketStatus.setValues(layersBean.controlPacketStatus);
            scaleValues.setValues(layersBean.scaleValue);
            normalValues.setValues(layersBean.normalValues);
            typeIds = layersBean.iec104asdu_typeid;
            numIxes = layersBean.asduNumIx;
            if (!(storeValidValueIntoMap(layersBean))){
                resetValuesArrIndex();
                return globalMap;
            }else{
                //存储数据的map发生变化
                saveValueToArtMap(iec104ConfigTshark, layer, globalMap);
            }
            resetValuesArrIndex();
        }
        return globalMap;
    }

    private void saveValueToArtMap(IEC104ConfigTshark iec104ConfigTshark,FvDimensionLayer layer,Map<String, Float> globalMap){
        Float value = iecResultMapWrapper.getResultValue(iec104ConfigTshark.getIpAddress(),iec104ConfigTshark.getIoaAddress());
        if (value!=null){
            Float prevValue = globalMap.put(iec104ConfigTshark.getTag(),value);
            if (prevValue == null || !prevValue.equals(value)){
                callback(iec104ConfigTshark.getTag(), value, layer);
            }
        }
    }

    /**
     *
     * @param layersBean
     * @return 当检测到新的值加入或者旧的值变化时候返回true
     */
    private boolean storeValidValueIntoMap(IEC104Packet.LayersBean layersBean) {
        if (typeIds == null || typeIds.length == 0){
            //not update global map return false
            return false;
        }
        boolean valueChange = false;
        String rtuIp = layersBean.ip_src[0];
        String typeId;
        IEC104ValuesWrapper valueArr;
        String ioaAddress;
        int asduNumIx;
        for (int i = 0, len = typeIds.length; i < len; i++) {
            typeId = typeIds[i];
            if (typeId.equals(INTER_COMMAND)){
                asduNumIx = Integer.parseInt(numIxes[i]);
                addressIndex += asduNumIx;
                continue;
            }
            try {
                valueArr = getStringArrByTypeId(typeId);
            }catch (IEC104DecodeException e){
                //if typeId = 45 (control packet)
                return false;
            }
            asduNumIx = Integer.parseInt(numIxes[i]);
            for (int j = addressIndex, end = addressIndex + asduNumIx; j < end; j++) {
                ioaAddress = ioaAddresses[j];   //"28681"
                if (valueArr.isSiq){
                    //0x00000001
                    int decodeRes = Integer.decode(valueArr.getNextValue());
                    boolean change = iecResultMapWrapper.setResultValue(rtuIp,ioaAddress,(float)decodeRes);
                    if (change){
                        //new value
                        newValue(layersBean.ip_src[0],ioaAddress,decodeRes);
                        if (!valueChange){
                            valueChange = true;
                        }
                    }
                }else{
                    //"0.0209961"
                    float decodeRes = Float.parseFloat(valueArr.getNextValue());
                    boolean change = iecResultMapWrapper.setResultValue(rtuIp,ioaAddress,decodeRes);
                    if (change){
                        newValue(layersBean.ip_src[0],ioaAddress,decodeRes);
                        if (!valueChange){
                            valueChange = true;
                        }
                    }
                }
            }
            addressIndex += asduNumIx;
        }
        return valueChange;
    }

    private void newValue(String ip,String ioa,float value){
        String id = iec104Mapper.getIDByIPAndIOA(ip, ioa);
        if (id!=null) {
            elecStatusChangeCallback("iec104", new IEC104Wrapper(id, value, ip, ioa));
        }
    }

    @Override
    public String protocol() {
        return "104apci";
    }

    public IECResultMapWrapper getIec104ResultValueMap() {
        return iecResultMapWrapper;
    }

    private static class IEC104DecodeException extends RuntimeException{
        IEC104DecodeException(String msg){
            super(msg);
        }
    }

    public static Map<String, HashBiMap<String,String>> readIOA2IDMapper(){
        return iec104Mapper.getIpAndIoa2IDMap();
    }

    public static String getIOAByIpAndId(String ip,String id){
        return iec104Mapper.getIOAByIPAndID(ip, id);
    }

    /**
     * [ip -- {ioa-value}]
     */
    public static class IECResultMapWrapper{
        private ConcurrentHashMap<String,ConcurrentHashMap<String,Float>> iec104ResultValueMap =
                new ConcurrentHashMap<>();

        /**
         * 设置值，
         * @param ip
         * @param ioa
         * @param value
         * @return rtu对应的值是否发生变化，如果发生变化返回true，否则返回false
         */
        public boolean setResultValue(String ip,String ioa,float value){
            ConcurrentHashMap<String,Float> ioa2ValueMap = iec104ResultValueMap.computeIfAbsent(ip, ip1 -> new ConcurrentHashMap<>());
            Float prev = ioa2ValueMap.put(ioa,value);
            if (prev == null || prev != value){
                return true;
            }
            return false;
        }

        public Float getResultValue(String ip,String ioa){
            ConcurrentHashMap<String,Float> ioa2ValueMap = iec104ResultValueMap.get(ip);
            if (ioa2ValueMap == null){
                return null;
            }
            return ioa2ValueMap.get(ioa);
        }

        public ConcurrentHashMap<String,ConcurrentHashMap<String,Float>> getIec104ResultValueMap(){
            return iec104ResultValueMap;
        }
    }
}
