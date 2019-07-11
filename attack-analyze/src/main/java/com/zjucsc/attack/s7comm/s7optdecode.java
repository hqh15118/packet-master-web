package com.zjucsc.attack.s7comm;


import com.zjucsc.attack.base.BaseOptAnalyzer;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.ArtAttackAnalyzeTask;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.common.common_util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class s7optdecode {


    private AttackBean Attackdecode(FvDimensionLayer layer, Operationconfig operationconfig, List<String> expression, Map<String,Float> techmap)
    {
        if(ArtAttackAnalyzeTask.attackDecode(expression,techmap,"1")==null || Writejobdecode(layer)==null)
        {
            return null;
        }
        else if(ArtAttackAnalyzeTask.attackDecode(expression,techmap,"1").equals("配置错误"))
        {
            return new AttackBean.Builder().attackType("配置错误").fvDimension(layer).attackInfo("").build();
        }
        else if(ArtAttackAnalyzeTask.attackDecode(expression,techmap,"1").equals("1"))
        {
            if(OperationDecode(Writejobdecode(layer),operationconfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(operationconfig.getComment()).build();
            }
        }
        return null;
    }

    private List<DBclass> Writejobdecode(FvDimensionLayer S7layer)
    {
        if(!S7layer.frame_protocols[0].equals("s7comm"))
        {
            return null;
        }
        byte[] S7data;
        if(!S7layer.tcp_payload[0].equals(""))
        {
            S7data = GetS7load.S7load(ByteUtil.hexStringToByteArray(S7layer.tcp_payload[0]),1);
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
                    byte[] DBaddr = new byte[] {0x00,itemparadata[len-3],itemparadata[len-2],itemparadata[len-1]};
                    DBifo.setBitoffset((int) DBaddr[3] & 7);
                    DBifo.setByteoffset((ByteUtil.bytesToInt(DBaddr,0)>>3) & 0xffff);
                    DBlist.add(DBifo);
                    if(data[0]==0x00 && data[1]==itemparadata[3])
                    {
                        DBifo.setData(Bytecut.Bytecut(data,3,(int)data[2]));
                        DBlist.add(DBifo);
                        itemparadata = Bytecut.Bytecut(itemparadata,len +2,-1);
                        data = Bytecut.Bytecut(data,3+data[2],-1);
                    }
                }
            }
            return DBlist;////////decode
        }
        return null;
    }

    private boolean OperationDecode(List<DBclass> DBlist, Operationconfig operationconfig)
    {
        if(DBlist==null || operationconfig==null)
        {
            return false;
        }
        for(DBclass db:DBlist) {
            if (operationconfig.getDBnum()==db.getDbnum())
            {
                if(operationconfig.getByteoffset()==db.getByteoffset() && db.getTransportsize()==3 )////开关量操作
                {
                    if(db.getBitoffset()<=operationconfig.getBitoffset() && (db.getBitoffset()+db.getLength()>operationconfig.getBitoffset()))
                    {
                        if((((int)db.getData()[0]>>(operationconfig.getBitoffset()-db.getBitoffset())) & 1) == 1 && operationconfig.isResult())
                        {
                            return true;
                        }
                        else if((((int)db.getData()[0]>>(operationconfig.getBitoffset()-db.getBitoffset())) & 1) == 0 && !operationconfig.isResult())
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

//    private GetS7load getS7load = new GetS7load();

//    private Map<Integer, List<DBclass>> DBmap = new HashMap<>();

    private List<DBclass> DBlist = new ArrayList<>();
}
