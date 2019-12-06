package com.zjucsc.art_decode.can;

import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Arrays;
import java.util.Map;

public class CanDecode extends BaseArtDecode<CanConfig> {

    public Map<String,Float> canTech(CanConfig canConfig, Map<String, Float> globalMap, byte[] tcpPayload) {
        if (tcpPayload[0] == 0x12 && tcpPayload[1] == 0x34) {
            String id = idDecode(tcpPayload);
            float data;
            int RTR = tcpPayload[2] & 0x40;//远程发送请求，判断“显性”和“隐性”
            if (RTR == 0) {
                data = Float.parseFloat(dataDecode(tcpPayload));
            } else {
                data = 0f;
            }
            if (id.equals(canConfig.getAddress())){
                globalMap.put(canConfig.getTag(), data);
            }
        }
        return globalMap;
    }

    private  String idDecode(byte[] tcpPayload) {
        StringBuilder id = new StringBuilder();//转成String类型方便筛选
        int format = tcpPayload[2] & 0x80;//帧格式
        byte[] IDbyte;
        if (format == 0){//标准格式
            IDbyte = Arrays.copyOfRange(tcpPayload,6,8);
        }
        else {//拓展格式
            IDbyte = Arrays.copyOfRange(tcpPayload,4,8);
        }
        for (byte b : IDbyte) {
            if ((b & 0xff) < 0x10) {//0~F前面补零
                id.append("0");
            }
            id.append(Integer.toHexString(0xFF & b));
        }
        return id.toString();
    }

    private  String dataDecode(byte[] tcpPayload){
        byte[] dataBytes = Arrays.copyOfRange(tcpPayload,8,16);
        StringBuilder dataString = new StringBuilder();
        for (int i = 0; i < dataBytes.length; i++) {
            if ((dataBytes[i] & 0xff) < 0x10){//0~F前面补零
                dataString.append("0");
            }
            dataString.append(Integer.toHexString(0xFF & dataBytes[i]));
        }
        String data = "" + dataString;
//        System.out.println("Data:" + Data);
        return data;
    }


    @Override
    public Map<String, Float> decode(CanConfig canConfig, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer layer, Object... obj) {
        return canTech(canConfig,globalMap,payload);
    }

    @Override
    public String protocol() {
        return "CAN";
    }

}
