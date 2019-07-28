package com.zjucsc.attack.s7comm;


import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.BaseOptAnalyzer;
import com.zjucsc.attack.config.S7OptAttackConfig;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.common.common_util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zjucsc.attack.common.ArtAttackAnalyzeUtil.attackDecode;

public class S7OptAnalyzer extends BaseOptAnalyzer<S7OptAttackConfig>{

    private List<DBclass> DBlist = new ArrayList<>();


    private AttackBean Attackdecode(FvDimensionLayer layer, S7OptAttackConfig s7OptAttackConfig, Map<String,Float> techmap)
    {
        if(attackDecode(s7OptAttackConfig.getExpression(),techmap,"1")==null || Writejobdecode(layer)==null)
        {
            return null;
        }
        if(attackDecode(s7OptAttackConfig.getExpression(),techmap,"1").equals("配置错误"))
        {
            return null;
        }
        else if(attackDecode(s7OptAttackConfig.getExpression(),techmap,"1").equals("1"))
        {
            if(OperationDecode(Writejobdecode(layer), s7OptAttackConfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(s7OptAttackConfig.getComment()).build();
            }
        }
        return null;
    }

    private List<DBclass> Writejobdecode(FvDimensionLayer S7layer )
    {
        byte[] S7data;
        if(!S7layer.tcp_payload[0].equals(""))
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

    private boolean OperationDecode(List<DBclass> DBlist, S7OptAttackConfig s7OptAttackConfig)
    {
        if(DBlist==null || s7OptAttackConfig ==null)
        {
            return false;
        }
        for(DBclass db:DBlist) {
            if (s7OptAttackConfig.getDBnum()==db.getDbnum())
            {
                if(s7OptAttackConfig.getByteoffset()==db.getByteoffset() && db.getTransportsize()==1 )////开关量操作
                {
                    if(db.getBitoffset()<= s7OptAttackConfig.getBitoffset() && (db.getBitoffset()+db.getLength()> s7OptAttackConfig.getBitoffset()))
                    {
                        if((((int)db.getData()[0]>>(s7OptAttackConfig.getBitoffset()-db.getBitoffset())) & 1) == 1 && s7OptAttackConfig.isResult())
                        {
                            return true;
                        }
                        else if((((int)db.getData()[0]>>(s7OptAttackConfig.getBitoffset()-db.getBitoffset())) & 1) == 0 && !s7OptAttackConfig.isResult())
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

    @Override
    public AttackBean doAnalyze(FvDimensionLayer layer, Map<String,Float> techmap, S7OptAttackConfig s7OptAttackConfig, Object... objs) {
        return Attackdecode(layer, s7OptAttackConfig,techmap);
    }
}
