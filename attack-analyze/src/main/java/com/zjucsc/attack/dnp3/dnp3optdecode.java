package com.zjucsc.attack.dnp3;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.BaseOptAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

import static com.zjucsc.attack.common.ArtAttackAnalyzeTask.attackDecode;

public class dnp3optdecode extends BaseOptAnalyzer<dnp3Opconfig> {

    public AttackBean attackdecode(FvDimensionLayer layer, dnp3Opconfig dnp3Opconfig, Map<String,Float> techmap)
    {
        if(attackDecode(dnp3Opconfig.getExpression(),techmap,"1")==null)
        {
            return null;
        }
        else if(attackDecode(dnp3Opconfig.getExpression(),techmap,"1").equals("配置错误"))
        {
            return null;
        }
        else if(attackDecode(dnp3Opconfig.getExpression(),techmap,"1").equals("1"))
        {
            if(operationdecode(layer,dnp3Opconfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(dnp3Opconfig.getComment()).build();
            }
        }
        return null;
    }

    private boolean operationdecode(FvDimensionLayer layer, dnp3Opconfig dnp3opconfig)
    {
        int packetobjGroup = 0;
        int packetindex = 0;
        if(layer==null || dnp3opconfig==null)
        {
            return false;
        }
        else {
            byte[] payload = layer.tcpPayload;
            if ((dnp3opconfig.getCategory() == 0 || dnp3opconfig.getCategory() == 1 || dnp3opconfig.getCategory() == 2) && payload!=null) {

                if (payload[12] == 0x05)
                {
                    packetobjGroup = payload[13];
                    packetindex = Byte.toUnsignedInt(payload[17]);
                    if(packetindex==dnp3opconfig.getSetindex() && packetobjGroup==dnp3opconfig.getSetobjGroup())
                    {
                        if (dnp3opconfig.isResult() && payload[18] == (byte)0x81)
                        {
                            return true;
                        }
                        else if (!dnp3opconfig.isResult() && payload[18] == 0x03)
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
    public AttackBean doAnalyze(FvDimensionLayer layer, Map<String, Float> techmap, dnp3Opconfig dnp3Opconfig, Object... objs) {
        return attackdecode(layer,dnp3Opconfig,techmap);
    }
}