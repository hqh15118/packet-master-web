package com.zjucsc.attack.s7comm;

import com.zjucsc.attack.base.AbstractOptCommandAttackEntry;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.common.util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.ArrayList;
import java.util.List;

public class s7Opdecode extends AbstractOptCommandAttackEntry {

    private List<DBclass> DBlist = new ArrayList<>();

    private List<DBclass> Writejobdecode(FvDimensionLayer S7layer )
    {
        return getdBclasses(S7layer, DBlist);
    }

    public static List<DBclass> getdBclasses(FvDimensionLayer S7layer, List<DBclass> dBlist) {
        byte[] S7data;
        if(S7layer.tcpPayload.length != 0)
        {
            S7data = GetS7load.S7load(S7layer.tcpPayload,1);
        }
        else
        {
            S7data = GetS7load.S7load(S7layer.rawData,0);
        }
        int PDUref = ByteUtil.bytesToShort(S7data,4);
        int ParaLen = ByteUtil.bytesToShort(S7data,6);
        int DataLen = ByteUtil.bytesToShort(S7data,8);
        byte[] paradata = Bytecut.Bytecut(S7data,10,ParaLen);
        byte[] data = Bytecut.Bytecut(S7data,10+ParaLen,DataLen);
        if(paradata!=null && S7data[1]==0x01 && paradata[0]==0x05)/////////job write
        {
            int itemcnt = paradata[1];
            byte[] itemparadata = Bytecut.Bytecut(paradata,2,-1);
            dBlist.clear();
            DBclass DBifo = new DBclass();
            for(int i=0;i<itemcnt && itemparadata!=null && data!=null;i++)
            {
                if(itemparadata[8]==(byte)0x84) {
                    int len = itemparadata[1];
                    DBifo.setDbnum(ByteUtil.bytesToShort(itemparadata, 6));
                    DBifo.setTransportsize(itemparadata[3]);
                    DBifo.setLength(ByteUtil.bytesToShort(itemparadata, 4));
                    byte[] DBaddr = new byte[] {0x00,itemparadata[len-1],itemparadata[len],itemparadata[len+1]};
                    DBifo.setBitoffset((int) DBaddr[3] & 7);
                    DBifo.setByteoffset((ByteUtil.bytesToInt(DBaddr,0)>>3) & 0xffff);
                    dBlist.add(DBifo);
                    if(data[0]==0x00)
                    {
                        DBifo.setData(Bytecut.Bytecut(data,4,ByteUtil.bytesToShort(data,2)));
                        dBlist.add(DBifo);
                        itemparadata = Bytecut.Bytecut(itemparadata,len +2,-1);
                        data = Bytecut.Bytecut(data,4+ByteUtil.bytesToShort(data,2),-1);
                    }
                }
            }
            return dBlist;////////decode
        }
        return null;
    }

    private boolean OperationDecode(List<DBclass> DBlist, S7OptName s7OptName)
    {
        if(DBlist==null || s7OptName ==null)
        {
            return false;
        }
        if (s7m1(DBlist, s7OptName.getDbNum(), s7OptName.getByteOffset(), s7OptName.getBitOffset(), s7OptName.isResult()))
            return true;
        return false;
    }

    public static boolean s7m1(List<DBclass> DBlist, int dBnum, int byteoffset, int bitoffset, boolean result) {
        for(DBclass db:DBlist) {
            if (dBnum ==db.getDbnum())
            {
                if(byteoffset ==db.getByteoffset() && db.getTransportsize()==1 )////开关量操作
                {
                    if(db.getBitoffset()<= bitoffset && (db.getBitoffset()+db.getLength()> bitoffset))
                    {
                        if((((int)db.getData()[0]>>(bitoffset -db.getBitoffset())) & 1) == 1 && result)
                        {
                            return true;
                        }
                        else if((((int)db.getData()[0]>>(bitoffset -db.getBitoffset())) & 1) == 0 && !result)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Object analyze(FvDimensionLayer layer, S7OptName s7OptName, Object... objs) {
        if(!layer.eth_dst[0].equals(s7OptName.getDeviceMac()))
        {
            return null;
        }
        if(OperationDecode(Writejobdecode(layer), s7OptName))
        {
            //fix
            commandCallback(s7OptName.getOpName(),layer);
//            return s7OptName.getOpname();
        }
        return null;
    }
}
