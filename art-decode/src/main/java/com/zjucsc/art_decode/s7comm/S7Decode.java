package com.zjucsc.art_decode.s7comm;


import com.zjucsc.art_decode.artconfig.S7Config;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.common_util.Bytecut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class S7Decode extends BaseArtDecode<S7Config> {

    private Map<Integer,List<DBclass>> DBmap = new HashMap<>(); ////pduref,DB

    private Map<Integer,byte[]> Datamap = new HashMap<>();  //// seq ,data

    private Map<Integer,Integer> PDUrefmap = new HashMap<>(); ////////seq ,pduref

    public Map<Integer,Map<Integer,Byte>> map = new HashMap<>();

    private GetS7load getS7load = new GetS7load();

    private Map<Integer,Map<Integer,Byte>> putDBdatamap(byte[] load)
    {
        if(load != null)
        {
            putDBmap(load);
            return map;
        }
        return null;
    }

    private void putDBmap(byte[] s7load){
        if(s7load[1]==0x07)              //userdata
        {
            if(ByteUtil.bytesToShort(s7load,6)==8)
            {
                byte[] parameter = Bytecut.Bytecut(s7load,10,8);
                if(parameter!=null && parameter[4]==0x11 && parameter[5]==0x42 && parameter[6]==0x05)/////////////subfunction ,method ,function group,type
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
                        DBmap.put(PDUref,DBlist);
                    }
                }
            }
            else if(ByteUtil.bytesToShort(s7load,6)==12)
            {
                byte[] parameter = Bytecut.Bytecut(s7load,10,-1);
                if(parameter!=null && parameter[4]==(byte)0x12 && parameter[5]==(byte)0x82 && parameter[6]==(byte)0x05)
                {
                    List<DBclass> DBlist = DBmap.get((int)ByteUtil.bytesToShort(s7load,4));
                    if(DBlist != null)
                    {
                        PDUrefmap.put((int)parameter[7],(int)ByteUtil.bytesToShort(s7load,4));
                        byte[] data = Bytecut.Bytecut(parameter,12,-1);
                        if(data!=null && data[0]==(byte)0xff)
                        {
                            Datamap.put((int)ByteUtil.bytesToShort(s7load,4),data);
                            decodeDBlist(DBlist,data);
                        }
                     }
                }
                else if(parameter !=null && parameter[4]==(byte)0x12 && parameter[5]==(byte)0x02 )
                {
                    int sequence_num = (int)parameter[7];/////////////与response seq num 对应
                    byte[] data = Bytecut.Bytecut(parameter,12,-1);//////////与response一样
                    if(data!=null && Datamap!=null && PDUrefmap!=null )
                    {
                        byte[] bytes = Datamap.get(PDUrefmap.get(sequence_num));///////////modify
                        if (bytes!=null && data[0] == (byte) 0xff &&  PDUrefmap.get(sequence_num) != null) {
                            Datamap.put(PDUrefmap.get(sequence_num), data);
                            decodeDBlist(DBmap.get(PDUrefmap.get(sequence_num)), data);
                        }
                    }
                }
                else if(parameter!=null && parameter[4]==(byte)0x12 && parameter[5]==(byte)0x82 && parameter[6]==(byte)0x04)
                {
                    int sequence_num = (int)parameter[7];
                    if(PDUrefmap.get(sequence_num)!=null)
                    {
                        int PDU = PDUrefmap.get(sequence_num);
                        DBmap.remove(PDU);
                        Datamap.remove(sequence_num);
                        PDUrefmap.remove(sequence_num);
                    }
                }
            }
        }
    }


    private void decodeDBlist(List<DBclass> DBlist, byte[] data)
    {
        if(data!=null && DBlist!=null)
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
                            if (map.get(s.getDbnum()) != null) {
                                (map.get(s.getDbnum())).put(i + s.getByteoffset(), iteamdata[4 + i]);
                            } else {
                                Map<Integer, Byte> Bmap = new HashMap<>();
                                Bmap.put(i + s.getByteoffset(), iteamdata[4 + i]);
                                map.put(s.getDbnum(), Bmap);
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
                                        if (map.get(s.getDbnum()) != null) {
                                            (map.get(s.getDbnum())).put(i + s.getByteoffset(), subitemdata[1 + i]);
                                        } else {
                                            Map<Integer, Byte> Bmap = new HashMap<>();
                                            Bmap.put(i + s.getByteoffset(), subitemdata[1 + i]);
                                            map.put(s.getDbnum(), Bmap);
                                        }
                                    }
                                    subitemdata = Bytecut.Bytecut(subitemdata, 1 + s.getLength(), -1);
                                }
                                else if(subitemdata[0]==(byte)0xfe)
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

    private Map<String,Float> decode_tech(S7Config S7tech, Map<String, Float> tech_map, byte[] load, int tcp)
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
                } else if (S7tech.getType().equals("int")) {
                    tech_map.put(S7tech.getTag(), (float) ByteUtil.bytesToInt(bytes, 0));
                }
            } else if (datamap!=null && S7tech.getType().equals("short") && S7tech.getLength() == 2) {
                byte[] bytes = new byte[2];
                for (int s = 0; s < 2; s++) {
                    bytes[s] = datamap.get(S7tech.getByteoffset() + s);
                }
                tech_map.put(S7tech.getTag(), (float) ByteUtil.bytesToShort(bytes, 0));
            } else if (S7tech.getType().equals("bool")) {
                if (datamap!=null && (((int) datamap.get(S7tech.getByteoffset())) & (1 << S7tech.getBitoffset())) == 0) {
                    tech_map.put(S7tech.getTag(), 0f);
                } else {
                    tech_map.put(S7tech.getTag(), 1f);
                }
            }
        }
        return tech_map;
    }

    @Override
    public Map<String, Float> decode(S7Config s7Config, Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return decode_tech(s7Config,globalMap,payload,(int)obj[0]);
    }

    @Override
    public String protocol() {
        return "s7comm";
    }


}













