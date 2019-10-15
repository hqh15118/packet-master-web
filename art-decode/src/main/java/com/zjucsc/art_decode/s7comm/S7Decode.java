package com.zjucsc.art_decode.s7comm;


import com.zjucsc.art_decode.artconfig.S7Config;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.common.util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class S7Decode extends BaseArtDecode<S7Config> {

    private static class S7Inner{
        private Map<Integer,List<DBclass>> DBmap = new HashMap<>(); ////pduref,DB

        private Map<Integer,byte[]> Datamap = new HashMap<>();  //// seq ,data

        private Map<Integer,Integer> PDUrefmap = new HashMap<>(); ////////seq ,pduref

        public Map<Integer,Map<Integer,Byte>> map = new HashMap<>();
    }

    private Map<String,S7Inner> innerMap = new ConcurrentHashMap<>();

    private GetS7load getS7load = new GetS7load();

    private Map<Integer,Map<Integer,Byte>> putDBdatamap(byte[] load)
    {
        if(load != null)
        {
            putDBmap(load);
            return s7Inner.map;
        }
        return null;
    }

    private void putDBmap(byte[] s7load){
        if(s7load[1]==0x07)              //userdata
        {
            if(ByteUtil.bytesToShort(s7load,6)==8)
            {
                byte[] parameter = Bytecut.Bytecut(s7load,10,8);
                if(parameter!=null && parameter[4]==0x11 && parameter[5]==0x42 && (parameter[6]==0x05||parameter[6]==0x07))/////////////subfunction ,method ,function group,type
                {
                    int PDUref = ByteUtil.bytesToShort(s7load,4);
                    byte[] data = Bytecut.Bytecut(s7load,18,-1);
                    if(data!=null && data[0]==(byte)0xff)
                    {
                        int itemcount = ByteUtil.bytesToShort(data,4);
                        byte[] itemdata = Bytecut.Bytecut(data,8,-1);
                        List<DBclass> DBlist = new ArrayList<>();
                        for(int i=0;i<itemcount;i++)
                        {
                            if (itemdata == null){
                                break;
                            }
                            int itemlength = (int)itemdata[1]+ 2;
                            if (itemdata[2]==(byte)0x10 && itemdata[8]==(byte)0x84) {
                                int dbnum = ByteUtil.bytesToShort(itemdata,6);
                                int address = ByteUtil.bytesToInt(new byte[]{0x00,itemdata[7],itemdata[8],itemdata[9]},0);
                                int byteoffset = address>>3 & 0xffff;
                                int bitoffset = address & 0x7;
                                int length = ByteUtil.bytesToShort(itemdata,4);
                                DBlist.add(new DBclass(){
                                    {
                                        setBitoffset(bitoffset);
                                        setByteoffset(byteoffset);
                                        setDbnum(dbnum);
                                        setLength(length);
                                    }
                                });
                                itemdata = Bytecut.Bytecut(itemdata,itemlength,-1);
                            }
                            if(itemdata!=null && itemdata[2]==(byte)0xb0)
                            {
                                int subitemcount = (int)itemdata[3];
                                byte[] subitemdata =Bytecut.Bytecut(itemdata,4,-1);
                                for(int j=0;j<subitemcount && subitemdata!=null;j++)
                                {
                                    DBclass DB = new DBclass();
                                    DB.setDbnum(ByteUtil.bytesToShort(subitemdata,1));
                                    DB.setLength((int)subitemdata[0]);
                                    DB.setByteoffset(ByteUtil.bytesToShort(subitemdata,3));
                                    DB.setBitoffset(0);
                                    DBlist.add(DB);
                                    subitemdata = Bytecut.Bytecut(subitemdata,5,-1);
                                }
                            }
                        }
                        s7Inner.DBmap.put(PDUref,DBlist);
                    }
                }
            }
            else if(ByteUtil.bytesToShort(s7load,6)==12)
            {
                byte[] parameter = Bytecut.Bytecut(s7load,10,-1);
                if(parameter!=null && parameter[4]==(byte)0x12 && parameter[5]==(byte)0x82 && (parameter[6]==0x05||parameter[6]==0x07))
                {
                    List<DBclass> DBlist = s7Inner.DBmap.get((int)ByteUtil.bytesToShort(s7load,4));
                    if(DBlist != null)
                    {
                        s7Inner.PDUrefmap.put((int)parameter[7],(int)ByteUtil.bytesToShort(s7load,4));
                        byte[] data = Bytecut.Bytecut(parameter,12,-1);
                        if(data!=null && data[0]==(byte)0xff)
                        {
                            s7Inner.Datamap.put((int)ByteUtil.bytesToShort(s7load,4),data);
                            decodeDBlist(DBlist,data);
                        }
                     }
                }
                else if(parameter !=null && parameter[4]==(byte)0x12 && parameter[5]==(byte)0x02 )
                {
                    int sequence_num = (int)parameter[7];/////////////与response seq num 对应
                    byte[] data = Bytecut.Bytecut(parameter,12,-1);//////////与response一样
                    if(data!=null && s7Inner.Datamap!=null && s7Inner.PDUrefmap!=null )
                    {
                        byte[] bytes = s7Inner.Datamap.get(s7Inner.PDUrefmap.get(sequence_num));///////////modify
                        if (bytes!=null && data[0] == (byte) 0xff &&  s7Inner.PDUrefmap.get(sequence_num) != null) {
                            s7Inner.Datamap.put(s7Inner.PDUrefmap.get(sequence_num), data);
                            decodeDBlist(s7Inner.DBmap.get(s7Inner.PDUrefmap.get(sequence_num)), data);
                        }
                    }
                }
                else if(parameter!=null && parameter[4]==(byte)0x12 && parameter[5]==(byte)0x82 && parameter[6]==(byte)0x04)
                {
                    int sequence_num = (int)parameter[7];
                    if(s7Inner.PDUrefmap.get(sequence_num)!=null)
                    {
                        int PDU = s7Inner.PDUrefmap.get(sequence_num);
                        s7Inner.DBmap.remove(PDU);
                        s7Inner.Datamap.remove(sequence_num);
                        s7Inner.PDUrefmap.remove(sequence_num);
                    }
                }
            }
        }
    }


    private void decodeDBlist(List<DBclass> DBlist, byte[] data)
    {
        if(data!=null && DBlist!=null && DBlist.size() > 0)
        {
            int item_cnt = ByteUtil.bytesToShort(data,4);
            byte[] iteamdata = Bytecut.Bytecut(data,6,-1);
            for(int j=0;j<item_cnt;j++) {
                if (iteamdata == null)
                    break;
                if (iteamdata[0] == (byte) 0xff) {
                    int len = ByteUtil.bytesToShort(iteamdata, 2);
                    if (len == DBlist.get(j).getLength()) {
                        DBclass s = DBlist.get(j);
                        for (int i = 0; i < s.getLength(); i++) {
                            if (s7Inner.map.get(s.getDbnum()) != null) {
                                (s7Inner.map.get(s.getDbnum())).put(i + s.getByteoffset(), iteamdata[4 + i]);
                            } else {
                                Map<Integer, Byte> Bmap = new HashMap<>();
                                Bmap.put(i + s.getByteoffset(), iteamdata[4 + i]);
                                s7Inner.map.put(s.getDbnum(), Bmap);
                            }
                        }
                        iteamdata = Bytecut.Bytecut(iteamdata, 4 + len, -1);
                    } else if (len > DBlist.get(j).getLength()) {
                        int subitem_cnt = DBlist.size() / item_cnt;
                        byte[] subitemdata = Bytecut.Bytecut(iteamdata, 4, -1);
                        if (subitemdata != null){
                            for (int k = 0; k < subitem_cnt && subitemdata!=null; k++) {
                                if (subitemdata[0] == (byte) 0xff) {
                                    DBclass s = DBlist.get(subitem_cnt * j + k);
                                    for (int i = 0; i < s.getLength(); i++) {
                                        if (s7Inner.map.get(s.getDbnum()) != null) {
                                            (s7Inner.map.get(s.getDbnum())).put(i + s.getByteoffset(), subitemdata[1 + i]);
                                        } else {
                                            Map<Integer, Byte> Bmap = new HashMap<>();
                                            Bmap.put(i + s.getByteoffset(), subitemdata[1 + i]);
                                            s7Inner.map.put(s.getDbnum(), Bmap);
                                        }
                                    }
                                    subitemdata = Bytecut.Bytecut(subitemdata, 1 + s.getLength(), -1);
                                }
                                else if(subitemdata[0]==(byte)0xfe || subitemdata[0]==(byte)0x0a)
                                {
                                    subitemdata =Bytecut.Bytecut(subitemdata,1,-1);
                                }
                            }
                    }
                        iteamdata = subitemdata;
                    }
                }
            }
        }
    }

    private Map<String,Float> decode_tech(S7Config S7tech, Map<String, Float> tech_map, byte[] load, int tcp, FvDimensionLayer layer)
    {
        byte[] S7load = getS7load.S7load(load,tcp);
        if(S7load !=null && S7load[0]==(byte)0x32 && S7load[1]==(byte)0x07) {
            Map<Integer, Map<Integer, Byte>> s7map = putDBdatamap(S7load);
            Map<Integer, Byte> datamap = s7map.get(S7tech.getDatabase());
            if (datamap != null && S7tech.getLength() == 4) {
                byte[] bytes = new byte[4];
                for (int s = 0; s < 4; s++) {
                    if (datamap.get(S7tech.getByteoffset() + s) != null) {
                        bytes[s] = datamap.get(S7tech.getByteoffset() + s);
                    } else {
                        break;
                    }
                }
                if (S7tech.getType().equals("float")) {
                    tech_map.put(S7tech.getTag(), Bytecut.BytesTofloat(bytes, 0));
                    callback(S7tech.getTag(),tech_map.get(S7tech.getTag()), layer);
                } else if (S7tech.getType().equals("int")) {
                    tech_map.put(S7tech.getTag(), (float) ByteUtil.bytesToInt(bytes, 0)/(2^32-1) *S7tech.getRange()[1]);
                    callback(S7tech.getTag(),tech_map.get(S7tech.getTag()), layer);
                }
            } else if (datamap!=null && S7tech.getType().equals("short") && S7tech.getLength() == 2) {
                byte[] bytes = new byte[2];
                for (int s = 0; s < 2; s++) {
                    bytes[s] = datamap.get(S7tech.getByteoffset() + s);
                }
                tech_map.put(S7tech.getTag(), (float) ByteUtil.bytesToShort(bytes, 0)/ 65535 * S7tech.getRange()[1]);
                callback(S7tech.getTag(),tech_map.get(S7tech.getTag()), layer);
            } else if (S7tech.getType().equals("bool")) {
                if(datamap==null || datamap.get(S7tech.getByteoffset())==null)
                {
                    return tech_map;
                }
                else if (((int) datamap.get(S7tech.getByteoffset()) & 1 << S7tech.getBitoffset()) == 0) {
                    tech_map.put(S7tech.getTag(), 0f);
                    callback(S7tech.getTag(),tech_map.get(S7tech.getTag()), layer);
                } else {
                    tech_map.put(S7tech.getTag(), 1f);
                    callback(S7tech.getTag(),tech_map.get(S7tech.getTag()), layer);
                }
            }
            else if(S7tech.getLength() == 1 && S7tech.getType().equals("byte"))
            {
                if(datamap==null || datamap.get(S7tech.getByteoffset())==null)
                {
                    return tech_map;
                }
                int data = Byte.toUnsignedInt(datamap.get(S7tech.getByteoffset()));//
                tech_map.put(S7tech.getTag(),(float) data /255 * S7tech.getRange()[1]);
                callback(S7tech.getTag(),tech_map.get(S7tech.getTag()), layer);
            }
        }
        return tech_map;
    }

    private S7Inner s7Inner = null;

    @Override
    public Map<String, Float> decode(S7Config s7Config, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer layer, Object... obj) {
        s7Inner = innerMap.get(s7Config.getDeviceMac());
        if (s7Inner == null){
            s7Inner = new S7Inner();
            innerMap.put(s7Config.getDeviceMac(),s7Inner);
        }
        return decode_tech(s7Config,globalMap,payload,(int)obj[0],layer);
    }

    @Override
    public String protocol() {
        return "s7comm";
    }

}













