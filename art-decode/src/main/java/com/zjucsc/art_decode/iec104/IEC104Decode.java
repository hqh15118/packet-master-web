package com.zjucsc.art_decode.iec104;

import com.zjucsc.art_decode.artconfig.IEC104Config;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.other.AttackType;

import java.util.List;
import java.util.Map;

public class IEC104Decode extends BaseArtDecode<IEC104Config> {

    private int IOA[] = new int[127];
    private float data[] = new float[127];

    private Map<String,Float> decode_tech(IEC104Config IEC104tech, Map<String, Float> tech_map, byte[] load)
    {
        byte[] iec104load = load;
        int packetIOA = 0;
        int packetSQ = 0;
        int data_num = 0;

        int index = 0;

        //I-格式帧
        if (iec104load != null && (iec104load[2] & 0x01) == 0 && (iec104load[4] & 0x01) == 0) {
            //单位遥控
            if (iec104load[6] == 0x2d && (iec104load[8] & 0x3f) == 0x07 && (iec104load[15] & 0x80) == 0){
                packetIOA = Byte.toUnsignedInt(iec104load[12]) + (Byte.toUnsignedInt(iec104load[13]) << 8) + (Byte.toUnsignedInt(iec104load[14]) << 16);
                packetSQ = (Byte.toUnsignedInt(iec104load[7]) & 0x80) >> 7;

                if (IEC104tech.getSetIOAAddress()==packetIOA && packetSQ==0){
                    if ((iec104load[15] & 0x01) == 0) {
                        tech_map.put(IEC104tech.getTag(), (float) 0f);
                    }
                    else {
                        tech_map.put(IEC104tech.getTag(), (float) 1f);
                    }
                }
            }
            //单位遥信
            else if (iec104load[6] == 0x01 && (iec104load[8] & 0x3f) == 3) {
            packetIOA = Byte.toUnsignedInt(iec104load[12]) + (Byte.toUnsignedInt(iec104load[13]) << 8) + (Byte.toUnsignedInt(iec104load[14]) << 16);
            packetSQ = (Byte.toUnsignedInt(iec104load[7]) & 0x80) >> 7;

                if (IEC104tech.getMVIOAAddress()==packetIOA && packetSQ==0){
                    if ((iec104load[15] & 0x01) == 0) {
                        tech_map.put(IEC104tech.getTag(), (float) 0f);
                    }
                    else {
                        tech_map.put(IEC104tech.getTag(), (float) 1f);
                    }
                }
            }
            //归一化遥测
            else if (iec104load[6] == 0x09 && (iec104load[8] & 0x3f) == 3){
                packetSQ = (Byte.toUnsignedInt(iec104load[7]) & 0x80) >> 7;
                if (packetSQ==0){
                    data_num = Byte.toUnsignedInt(iec104load[7]);
                    for (index = 0; index < data_num; index = index + 1) {
                        IOA[index] = Byte.toUnsignedInt(iec104load[12+6*index]) + (Byte.toUnsignedInt(iec104load[13+6*index])<<8) + (Byte.toUnsignedInt(iec104load[14+6*index])<<16);
                        if ((iec104load[16+6*index]&0x0080)==0) {
                            data[index] = (float)(Byte.toUnsignedInt(iec104load[15+6*index]) + (Byte.toUnsignedInt(iec104load[16+6*index])<<8)) * 32768.0f / (float)Math.pow(2,15) ;
                        }
                        if (IEC104tech.getMVIOAAddress()==IOA[index]){
                            tech_map.put(IEC104tech.getTag(), data[index]);
                        }
                    }
                }
            }
        }
        return tech_map;
    }

    @Override
    public Map<String, Float> decode(IEC104Config iec104Config, Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return decode_tech(iec104Config,globalMap,payload);
    }

    @Override
    public String protocol() {
        return "iec104";
    }

    @Override
    public List<AttackType> attackDecode(List<AttackType> globalAttackList, byte[] payload, Object... obj) {
        return null;
    }
}