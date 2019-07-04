package com.zjucsc.art_decode.iec104;

import com.zjucsc.art_decode.artconfig.IEC104Config;
import com.zjucsc.art_decode.base.BaseArtDecode;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class IEC104Decode extends BaseArtDecode<IEC104Config> {
    int IOA[] = new int[127];
    float data[] = new float[127];
    byte[] thisAPDU = new byte[255];

    public  Map<String,Float> decode_tech(IEC104Config IEC104tech , Map<String,Float> tech_map , byte[] load)
    {
        byte[] iec104load = load;
        int packetIOA = 0;
        int packetSQ = 0;
        int data_num = 0;
        int index = 0;

        //一条报文中只有一个APDU的情况
        if (iec104load != null && iec104load.length == Byte.toUnsignedInt(iec104load[1]) + 2) {
            //I-格式帧
            if ((iec104load[2] & 0x01) == 0 && (iec104load[4] & 0x01) == 0) {

                //单位遥控
                if (iec104load[6] == 0x2d && (iec104load[8] & 0x3f) == 0x07 && (iec104load[15] & 0x80) == 0) {
                    packetIOA = Byte.toUnsignedInt(iec104load[12]) + (Byte.toUnsignedInt(iec104load[13]) << 8) + (Byte.toUnsignedInt(iec104load[14]) << 16);
                    packetSQ = (Byte.toUnsignedInt(iec104load[7]) & 0x80) >> 7;
                    if (IEC104tech.getSetIOAAddress() == packetIOA && packetSQ == 0) {
                        if ((iec104load[15] & 0x01) == 0) {
                            tech_map.put(IEC104tech.getTag(), (float) 0f);
                        }
                        else if ((iec104load[15] & 0x01) == 1) {
                            tech_map.put(IEC104tech.getTag(), (float) 1f);
                        }
                    }
                }

                //单位遥信
                else if (iec104load[6] == 0x01) {
                    packetSQ = (Byte.toUnsignedInt(iec104load[7]) & 0x80) >> 7;
                    //传送原因：突发
                    if ((iec104load[8] & 0x3f) == 3  && packetSQ == 0) {
                        data_num = Byte.toUnsignedInt(iec104load[7]);
                        for (index = 0; index < data_num; index++){
                            packetIOA = Byte.toUnsignedInt(iec104load[12 + 4 * index]) + (Byte.toUnsignedInt(iec104load[13 + 4 * index]) << 8) + (Byte.toUnsignedInt(iec104load[14 + 4 * index]) << 16);
                            if (IEC104tech.getMVIOAAddress() == packetIOA) {
                                if ((iec104load[15 + 4 * index] & 0x01) == 0) {
                                    tech_map.put(IEC104tech.getTag(), (float) 0f);
                                }
                                else if ((iec104load[15 + 4 * index] & 0x01) == 1) {
                                    tech_map.put(IEC104tech.getTag(), (float) 1f);
                                }
                            }
                        }
                    }
                    //传送原因：站召唤响应或突发
                    else if ((iec104load[8] & 0x3f) == 20  || (iec104load[8] & 0x3f) == 3 && packetSQ == 1) {
                        packetIOA = Byte.toUnsignedInt(iec104load[12]) + (Byte.toUnsignedInt(iec104load[13]) << 8) + (Byte.toUnsignedInt(iec104load[14]) << 16);
                        packetIOA = packetIOA - 1;
                        data_num = Byte.toUnsignedInt(iec104load[7]) - 128;
                        for (index = 0; index < data_num; index++) {
                            packetIOA = packetIOA + 1;
                            if (IEC104tech.getMVIOAAddress()==packetIOA){
                                if ((iec104load[index+15] & 0x01) == 0) {
                                    tech_map.put(IEC104tech.getTag(), (float) 0f);
                                }
                                else if ((iec104load[index+15] & 0x01) == 1) {
                                    tech_map.put(IEC104tech.getTag(), (float) 1f);
                                }
                            }
                        }
                    }
                }

                //归一化遥测
                else if (iec104load[6] == 0x09) {
                    packetSQ = (Byte.toUnsignedInt(iec104load[7]) & 0x80) >> 7;
                    //传送原因：突发
                    if ((iec104load[8] & 0x3f) == 3 && packetSQ == 0) {
                        data_num = Byte.toUnsignedInt(iec104load[7]);
                        for (index = 0; index < data_num; index++) {
                            IOA[index] = Byte.toUnsignedInt(iec104load[12 + 6 * index]) + (Byte.toUnsignedInt(iec104load[13 + 6 * index]) << 8) + (Byte.toUnsignedInt(iec104load[14 + 6 * index]) << 16);
                            if ((iec104load[16 + 6 * index] & 0x0080) == 0) {
                                data[index] = (float) (Byte.toUnsignedInt(iec104load[15 + 6 * index]) + (Byte.toUnsignedInt(iec104load[16 + 6 * index]) << 8)) * 32768.0f / (float) Math.pow(2, 15);
                            }
                            if (IEC104tech.getMVIOAAddress() == IOA[index]) {
                                tech_map.put(IEC104tech.getTag(), data[index]);
                            }
                        }
                    }
                    //传送原因：站召唤响应或突发
                    else if ((iec104load[8] & 0x3f) == 20 || (iec104load[8] & 0x3f) == 3 && packetSQ == 1) {
                        data_num = Byte.toUnsignedInt(iec104load[7]) - 128;
                        packetIOA = Byte.toUnsignedInt(iec104load[12]) + (Byte.toUnsignedInt(iec104load[13]) << 8) + (Byte.toUnsignedInt(iec104load[14]) << 16);
                        packetIOA = packetIOA - 1;
                        for (index = 0; index < data_num; index++) {
                            packetIOA = packetIOA + 1;
                            if ((iec104load[16+3*index]&0x0080)==0 && IEC104tech.getMVIOAAddress()==packetIOA) {
                                data[index] = (float)(Byte.toUnsignedInt(iec104load[15+3*index]) + (Byte.toUnsignedInt(iec104load[16+3*index])<<8)) * 32768.0f / (float)Math.pow(2,15) ;
                                tech_map.put(IEC104tech.getTag(), data[index]);
                            }
                        }
                    }
                }

                //短浮点遥测
                else if (iec104load[6] == 0x0d) {
                    packetSQ = (Byte.toUnsignedInt(iec104load[7]) & 0x80) >> 7;
                    //传送原因：突发
                    if ((iec104load[8] & 0x3f) == 3 && packetSQ == 0) {
                        data_num = Byte.toUnsignedInt(iec104load[7]);
                        for (index = 0; index < data_num; index++) {
                            IOA[index] = Byte.toUnsignedInt(iec104load[12 + 8 * index]) + (Byte.toUnsignedInt(iec104load[13 + 8 * index]) << 8) + (Byte.toUnsignedInt(iec104load[14 + 8 * index]) << 16);
                            data[index] = BytesDeal.BytesToSingle(iec104load[18 + 8 * index],iec104load[17 + 8 * index],iec104load[16 + 8 * index],iec104load[15 + 8 * index]);
                            if (IEC104tech.getMVIOAAddress() == IOA[index]) {
                                tech_map.put(IEC104tech.getTag(), data[index]);
                            }
                        }
                    }
                    //传送原因：站召唤响应或突发
                    else if ((iec104load[8] & 0x3f) == 20 || (iec104load[8] & 0x3f) == 3 && packetSQ == 1) {
                        data_num = Byte.toUnsignedInt(iec104load[7]) - 128;
                        packetIOA = Byte.toUnsignedInt(iec104load[12]) + (Byte.toUnsignedInt(iec104load[13]) << 8) + (Byte.toUnsignedInt(iec104load[14]) << 16);
                        packetIOA = packetIOA - 1;
                        for (index = 0; index < data_num; index++) {
                            packetIOA = packetIOA + 1;
                            if (IEC104tech.getMVIOAAddress()==packetIOA) {
                                data[index] = BytesDeal.BytesToSingle(iec104load[18 + 5 * index],iec104load[17 + 5 * index],iec104load[16 + 5 * index],iec104load[15 + 5 * index]);
                                tech_map.put(IEC104tech.getTag(), data[index]);
                            }
                        }
                    }
                }
            }
        }

        //一条报文中含有多个APDU的情况
        else if (iec104load != null && iec104load.length > Byte.toUnsignedInt(iec104load[1]) + 2) {
            int APDUstart = 0;
            int thisAPDULength = Byte.toUnsignedInt(iec104load[APDUstart+1]);
            System.arraycopy(iec104load, APDUstart, thisAPDU, 0, thisAPDULength+2);
            do{
                //I-格式帧
                if (thisAPDU != null && (thisAPDU[2] & 0x01) == 0 && (thisAPDU[4] & 0x01) == 0) {
                    packetSQ = (Byte.toUnsignedInt(thisAPDU[7]) & 0x80) >> 7;
                    if (packetSQ == 1){
                        data_num = Byte.toUnsignedInt(thisAPDU[7]) - 128;
                        packetIOA = Byte.toUnsignedInt(thisAPDU[12]) + (Byte.toUnsignedInt(thisAPDU[13]) << 8) + (Byte.toUnsignedInt(thisAPDU[14]) << 16);
                        packetIOA = packetIOA - 1;

                        //单位遥信
                        //传送原因：站召唤响应
                        if (thisAPDU[6] == 0x01 && (thisAPDU[8] & 0x3f) == 20) {
                            for (index = 0; index < data_num; index++) {
                                packetIOA = packetIOA + 1;
                                if (IEC104tech.getMVIOAAddress()==packetIOA){
                                    if ((thisAPDU[index+15] & 0x01) == 0) {
                                        tech_map.put(IEC104tech.getTag(), (float) 0f);
                                    }
                                    else if ((thisAPDU[index+15] & 0x01) == 1) {
                                        tech_map.put(IEC104tech.getTag(), (float) 1f);
                                    }
                                }
                            }
                        }

                        //归一化遥测
                        //传送原因：站召唤响应
                        else if (thisAPDU[6] == 0x09 && (thisAPDU[8] & 0x3f) == 20) {
                            for (index = 0; index < data_num; index++) {
                                packetIOA = packetIOA + 1;
                                if ((thisAPDU[16+3*index]&0x0080)==0 && IEC104tech.getMVIOAAddress()==packetIOA) {
                                    data[index] = (float)(Byte.toUnsignedInt(thisAPDU[15+3*index]) + (Byte.toUnsignedInt(thisAPDU[16+3*index])<<8)) * 32768.0f / (float)Math.pow(2,15) ;
                                    tech_map.put(IEC104tech.getTag(), data[index]);
                                }
                            }
                        }

                        //短浮点遥测
                        //传送原因：站召唤响应
                        else if (thisAPDU[6] == 0x0d && (thisAPDU[8] & 0x3f) == 20) {
                            for (index = 0; index < data_num; index++) {
                                packetIOA = packetIOA + 1;
                                if (IEC104tech.getMVIOAAddress()==packetIOA) {
                                    data[index] = BytesDeal.BytesToSingle(iec104load[18 + 5 * index],iec104load[17 + 5 * index],iec104load[16 + 5 * index],iec104load[15 + 5 * index]);
                                    tech_map.put(IEC104tech.getTag(), data[index]);
                                }
                            }
                        }
                    }
                }

                APDUstart = APDUstart + thisAPDULength + 2;
                if (APDUstart >= iec104load.length) break;
                thisAPDULength = Byte.toUnsignedInt(iec104load[APDUstart+1]);
                System.arraycopy(iec104load, APDUstart, thisAPDU, 0, thisAPDULength+2);
            }
            while(thisAPDU != null);
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
}