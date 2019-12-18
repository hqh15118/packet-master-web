package com.zjucsc.art_decode.iec104;

import com.zjucsc.art_decode.artconfig.IEC104ConfigTshark;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.IEC104Packet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * not thread safe
 * must single thread...
 */
public class IEC104DecodeByTshark extends BaseArtDecode<IEC104ConfigTshark> {

    private ConcurrentHashMap<String,Float> iec104ResultValueMap =
            new ConcurrentHashMap<>();
    private IEC104ValuesWrapper /*controlPacketStatus = new IEC104ValuesWrapper()*/
                                scaleValues = new IEC104ValuesWrapper()
                                ,normalValues = new IEC104ValuesWrapper()
                                ,measureFloatValues = new IEC104ValuesWrapper()
                                ,singlePointValue = new IEC104ValuesWrapper();
    private String[] typeIds, numIxes, ioaAddresses;
    private int addressIndex = 0;

    private static final String SINGLE_POINT = "1",SHORT_FLOAT = "13",INTER_COMMAND = "100",
                    SCALE_VALUE = "11",NORMAL_VALUE = "9";

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
            default:
                throw new IEC104DecodeException("无法通过typeId{" + typeId + "}获取到对应的数组");
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
            IEC104Packet.LayersBean layersBean = ((IEC104Packet.LayersBean) layer);
            ioaAddresses = layersBean.ioaAddress;
            measureFloatValues.setValues(layersBean.measureFloatValue);
            singlePointValue.setValues(layersBean.singlePointValue);
//            controlPacketStatus.setValues(layersBean.controlPacketStatus);
            scaleValues.setValues(layersBean.scaleValue);
            normalValues.setValues(layersBean.normalValues);
            typeIds = layersBean.iec104asdu_typeid;
            numIxes = layersBean.asduNumIx;
            if (!(storeValidValueIntoMap())){
                return globalMap;
            }else{
                Float value = iec104ResultValueMap.get(iec104ConfigTshark.getIoaAddress());
                if (value!=null){
                    globalMap.put(iec104ConfigTshark.getTag(),value);
                }
            }
        }
        resetValuesArrIndex();
        return globalMap;
    }

    private boolean storeValidValueIntoMap() {
        if (typeIds == null || typeIds.length == 0){
            //not update global map return false
            return false;
        }
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
            valueArr = getStringArrByTypeId(typeId);
            asduNumIx = Integer.parseInt(numIxes[i]);
            for (int j = addressIndex, end = addressIndex + asduNumIx; j < end; j++) {
                ioaAddress = ioaAddresses[j];   //"28681"
                if (valueArr.isSiq){
                    //0x00000001
                    int decodeRes = Integer.decode(valueArr.getNextValue());
                    iec104ResultValueMap.put(ioaAddress,(float)decodeRes);
                }else{
                    //"0.0209961"
                    float decodeRes = Float.parseFloat(valueArr.getNextValue());
                    iec104ResultValueMap.put(ioaAddress,decodeRes);
                }
            }
            addressIndex += asduNumIx;
        }
        return true;
    }

    @Override
    public String protocol() {
        return "104apci";
    }

    ConcurrentHashMap<String, Float> getIec104ResultValueMap() {
        return iec104ResultValueMap;
    }

    private static class IEC104DecodeException extends RuntimeException{
        IEC104DecodeException(String msg){
            super(msg);
        }
    }
}
