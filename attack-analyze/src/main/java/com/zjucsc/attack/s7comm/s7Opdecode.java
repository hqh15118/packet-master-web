package com.zjucsc.attack.s7comm;

import com.zjucsc.attack.base.AbstractOptCommandAttackEntry;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.common.common_util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.ArrayList;
import java.util.List;

public class s7Opdecode extends AbstractOptCommandAttackEntry {

    private List<DBclass> DBlist = new ArrayList<>();

    private List<DBclass> Writejobdecode(FvDimensionLayer S7layer )
    {
        byte[] S7data;
        if(S7layer.tcpPayload!=null)
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
            DBlist.clear();
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
                    DBlist.add(DBifo);
                    if(data[0]==0x00)
                    {
                        DBifo.setData(Bytecut.Bytecut(data,4,ByteUtil.bytesToShort(data,2)));
                        DBlist.add(DBifo);
                        itemparadata = Bytecut.Bytecut(itemparadata,len +2,-1);
                        data = Bytecut.Bytecut(data,4+ByteUtil.bytesToShort(data,2),-1);
                    }
                }
            }
            return DBlist;////////decode
        }
        return null;
    }

    private boolean OperationDecode(List<DBclass> DBlist, S7OptName s7OptName)
    {
        if(DBlist==null || s7OptName ==null)
        {
            return false;
        }
        for(DBclass db:DBlist) {
            if (s7OptName.getDBnum()==db.getDbnum())
            {
                if(s7OptName.getByteoffset()==db.getByteoffset() && db.getTransportsize()==1 )////开关量操作
                {
                    if(db.getBitoffset()<= s7OptName.getBitoffset() && (db.getBitoffset()+db.getLength()> s7OptName.getBitoffset()))
                    {
                        if((((int)db.getData()[0]>>(s7OptName.getBitoffset()-db.getBitoffset())) & 1) == 1 && s7OptName.isResult())
                        {
                            return true;
                        }
                        else if((((int)db.getData()[0]>>(s7OptName.getBitoffset()-db.getBitoffset())) & 1) == 0 && !s7OptName.isResult())
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
            return s7OptName.getOpname();
        }
        return null;
    }
}
