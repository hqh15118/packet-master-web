package com.zjucsc.attack.iec104;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.BaseOptAnalyzer;
import com.zjucsc.attack.common.ArtAttackAnalyzeTask;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

import static com.zjucsc.attack.common.ArtAttackAnalyzeTask.*;

public class iec104optdecode extends BaseOptAnalyzer<iec104Opconfig> {

    public AttackBean attackdecode(FvDimensionLayer layer, iec104Opconfig iec104Opconfig, Map<String,Float> techmap)
    {
        if(attackDecode(iec104Opconfig.getExpression(),techmap,"1")==null)
        {
            return null;
        }
        else if(attackDecode(iec104Opconfig.getExpression(),techmap,"1").equals("配置错误"))
        {
            return null;
        }
        else if(attackDecode(iec104Opconfig.getExpression(),techmap,"1").equals("1"))
        {
            if(operationdecode(layer,iec104Opconfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(iec104Opconfig.getComment()).build();
            }
        }
        return null;
    }

    private boolean operationdecode(FvDimensionLayer layer, iec104Opconfig iec104opconfig)
    {
        int packetIOA = 0;
        int packetSQ = 0;
        if(layer==null || iec104opconfig==null)
        {
            return false;
        }
        else {
            byte[] payload = layer.tcpPayload;
            if ((iec104opconfig.getCategory() == 0 || iec104opconfig.getCategory() == 1 || iec104opconfig.getCategory() == 2) && payload!=null) {
                packetSQ = (Byte.toUnsignedInt(payload[7]) & 0x80) >> 7;
                if (payload[6] == 0x2d && packetSQ == 0)
                {
                    packetIOA = Byte.toUnsignedInt(payload[12]) + (Byte.toUnsignedInt(payload[13]) << 8) + (Byte.toUnsignedInt(payload[14]) << 16);
                    if(packetIOA==iec104opconfig.getSetIOAAddress())
                    {
                        if (iec104opconfig.isResult() && !iec104opconfig.isOptype() && (payload[15] & 0x81) == 1)
                        {
                            return true;
                        }
                        else if (iec104opconfig.isResult() && iec104opconfig.isOptype() && (payload[15] & 0x81) == 0x81)
                        {
                            return true;
                        }
                        else if (!iec104opconfig.isResult() && !iec104opconfig.isOptype() && (payload[15] & 0x81) == 0)
                        {
                            return true;
                        }
                        else if (!iec104opconfig.isResult() && iec104opconfig.isOptype() && (payload[15] & 0x81) == 0x80)
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
    public AttackBean doAnalyze(FvDimensionLayer layer, Map<String, Float> techmap, iec104Opconfig iec104Opconfig, Object... objs) {
        return attackdecode(layer,iec104Opconfig,techmap);
    }
}
