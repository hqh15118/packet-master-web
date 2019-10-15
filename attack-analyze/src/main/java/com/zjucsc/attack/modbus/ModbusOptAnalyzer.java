package com.zjucsc.attack.modbus;

import com.zjucsc.attack.bean.BaseOptAnalyzer;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.ArtAttackAnalyzeUtil;
import com.zjucsc.attack.config.ModbusOptConfig;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.common.util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public class ModbusOptAnalyzer extends BaseOptAnalyzer<ModbusOptConfig> {

    public AttackBean attackdecode(FvDimensionLayer layer, Map<String,Float> techmap, ModbusOptConfig ModbusOptConfig)
    {
        if(ArtAttackAnalyzeUtil.attackDecode(ModbusOptConfig.getExpression(),techmap,"1")==null)
        {
            return null;
        }
        else if(ArtAttackAnalyzeUtil.attackDecode(ModbusOptConfig.getExpression(),techmap,"1").equals("配置错误"))
        {
            return null;
        }
        else if(ArtAttackAnalyzeUtil.attackDecode(ModbusOptConfig.getExpression(),techmap,"1").equals("1"))
        {
            if(operationdecode(layer, ModbusOptConfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(ModbusOptConfig.getComment()).build();
            }
        }
        return null;
    }

    private boolean operationdecode(FvDimensionLayer layer, ModbusOptConfig modbusopconfig)
    {
        if(layer==null || modbusopconfig==null)
        {
            return false;
        }
        else {
            byte[] payload =layer.getUseTcpPayload();
            int len = ByteUtil.bytesToShort(payload,4);
            byte[] modbusload = Bytecut.Bytecut(payload,7,len-1);
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
                        modbusload = Bytecut.Bytecut(modbusload,5,-1);
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
    public AttackBean doAnalyze(FvDimensionLayer layer, Map<String,Float> techmap, ModbusOptConfig ModbusOptConfig, Object... objs) {
        return attackdecode(layer,techmap, ModbusOptConfig);
    }
}
