package com.zjucsc.attack.modbus;

import com.zjucsc.attack.base.BaseOptAnalyzer;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.ArtAttackAnalyzeTask;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.common.common_util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.List;
import java.util.Map;

public class modbusoptdecode extends BaseOptAnalyzer<modbusOpconfig> {

    public AttackBean attackdecode(FvDimensionLayer layer, Map<String,Float> techmap, modbusOpconfig modbusOpconfig)
    {
        if(ArtAttackAnalyzeTask.attackDecode(modbusOpconfig.getExpression(),techmap,"1")==null)
        {
            return null;
        }
        else if(ArtAttackAnalyzeTask.attackDecode(modbusOpconfig.getExpression(),techmap,"1").equals("配置错误"))
        {
            return new AttackBean.Builder().attackType("配置错误").fvDimension(layer).attackInfo("").build();
        }
        else if(ArtAttackAnalyzeTask.attackDecode(modbusOpconfig.getExpression(),techmap,"1").equals("1"))
        {
            if(operationdecode(layer,modbusOpconfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(modbusOpconfig.getComment()).build();
            }
        }
        return null;
    }

    private boolean operationdecode(FvDimensionLayer layer, modbusOpconfig modbusopconfig)
    {
        if(layer==null || modbusopconfig==null)
        {
            return false;
        }
        else if(layer.frame_protocols[0].equals("modbus")) {
            byte[] payload = ByteUtil.hexStringToByteArray(layer.tcp_payload[0]);
            int len = ByteUtil.bytesToShort(payload,4);
            byte[] modbusload = Bytecut.Bytecut(payload,5,len-1);
            if (modbusopconfig.getReg() == 0 && modbusload!=null) {
                if (modbusload[0]==5)                                     ////写单个线圈
                {
                    if(ByteUtil.bytesToShort(modbusload,1)==modbusopconfig.getAddress())
                    {
                        if(modbusopconfig.isResult() && modbusload[3]==(byte)0xff && modbusload[4]==(byte)0x00)
                        {
                            return true;
                        }
                        else if(!modbusopconfig.isResult() && modbusload[3]==(byte)0x00 && modbusload[4]==(byte)0x00)
                        {
                            return true;
                        }
                    }
                }
                else if(modbusload[0]==15)              ///////////////写多个线圈
                {
                    int addresshead = ByteUtil.bytesToShort(modbusload,1);
                    if(addresshead<=modbusopconfig.getAddress() &&
                            (addresshead+ByteUtil.bytesToShort(modbusload,3)>modbusopconfig.getAddress()))
                    {
                        byte[] data = Bytecut.Bytecut(modbusload,6,modbusload[5]);
                        int i = (modbusopconfig.getAddress()-addresshead) / 8;
                        int j = (modbusopconfig.getAddress()-addresshead) % 8;
                        if(data!=null && data.length>i)
                        {
                            if(((data[i]>>j) & 1)==1 && modbusopconfig.isResult())
                            {
                                return true;
                            }
                            else if(((data[i]>>j) & 1)==0 && !modbusopconfig.isResult())
                            {
                                return true;
                            }
                        }
                    }
                }
            }
            else if(modbusopconfig.getReg()==1 && modbusload!=null)
            {
                if(modbusload[0]==6 && ByteUtil.bytesToShort(modbusload,1)==modbusopconfig.getAddress())  ///////////写单个寄存器
                {
                    if(modbusopconfig.getBitoffset()<8)
                    {
                        if(((modbusload[4]>>modbusopconfig.getBitoffset()) & 1)==1 && modbusopconfig.isResult())
                        {
                            return true;
                        }
                        else if(((modbusload[4]>>modbusopconfig.getBitoffset()) & 1)==0 && !modbusopconfig.isResult())
                        {
                            return true;
                        }
                    }
                    else if(modbusopconfig.getBitoffset()>=8 && modbusopconfig.getBitoffset()<16)
                    {
                        if(((modbusload[3]>>(modbusopconfig.getBitoffset()-8)) & 1)==1 && modbusopconfig.isResult())
                        {
                            return true;
                        }
                        else if(((modbusload[3]>>(modbusopconfig.getBitoffset()-8)) & 1)==0 && !modbusopconfig.isResult())
                        {
                            return true;
                        }
                    }
                }
                else if(modbusload[0]==16 || modbusload[0]==23)
                {
                    if(modbusload[0]==23)
                    {
                        modbusload = Bytecut.Bytecut(modbusload,4,-1);
                    }
                    if(modbusload==null)
                    {
                        return false;
                    }
                    int addresshead = ByteUtil.bytesToShort(modbusload,1);
                    int length = ByteUtil.bytesToShort(modbusload,3);
                    if(addresshead<=modbusopconfig.getAddress() && (addresshead + length)>modbusopconfig.getAddress())
                    {
                        int i = 6 + (modbusopconfig.getAddress()-addresshead)*2 + (1-modbusopconfig.getBitoffset()/8);
                        if((modbusload[i]>>(modbusopconfig.getBitoffset()%8) & 1)==1 && modbusopconfig.isResult())
                        {
                            return true;
                        }
                        else if((modbusload[i]>>(modbusopconfig.getBitoffset()%8) & 1)==0 && !modbusopconfig.isResult())
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
    public AttackBean doAnalyze(FvDimensionLayer layer, Map<String,Float> techmap,modbusOpconfig modbusOpconfig, Object... objs) {
        return attackdecode(layer,techmap,modbusOpconfig);
    }
}
