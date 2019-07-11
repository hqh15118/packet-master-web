package com.zjucsc.attack.pn_io;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.ArtAttackAnalyzeTask;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.common.common_util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.List;
import java.util.Map;

public class pniooptdecode {

    private static final int paddinglength = 24;

    public AttackBean attackdecode(FvDimensionLayer layer, pnioOpconfig pnioopconfig, List<String> expression, Map<String,Float> techmap)
    {
        if(ArtAttackAnalyzeTask.attackDecode(expression,techmap,"1")==null)
        {
            return null;
        }
        else if(ArtAttackAnalyzeTask.attackDecode(expression,techmap,"1").equals("配置错误"))
        {
            return new AttackBean.Builder().attackType("配置错误").fvDimension(layer).attackInfo("").build();
        }
        else if(ArtAttackAnalyzeTask.attackDecode(expression,techmap,"1").equals("1"))
        {
            if(operationdecode(layer,pnioopconfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(pnioopconfig.getComment()).build();
            }
        }
        return null;
    }

    private boolean operationdecode(FvDimensionLayer layer,pnioOpconfig pnioopconfig)
    {
        if(layer==null || pnioopconfig==null)
        {
            return false;
        }
        if(layer.frame_protocols[0].equals("pn_io") && ByteUtil.byteArrayToHex(pnioopconfig.getMacaddress()).equals(layer.eth_src[0]))
        {
            byte[] data = Bytecut.Bytecut(layer.rawData,16, layer.rawData.length-paddinglength-20);
            if(data == null)
            {
                return false;
            }
            else if((data[pnioopconfig.getByteoffset()]>>pnioopconfig.getBitoffset() & 1)==1 && pnioopconfig.isResult())
            {
                return true;
            }
            else if((data[pnioopconfig.getByteoffset()]>>pnioopconfig.getBitoffset() & 1)==0 && !pnioopconfig.isResult())
            {
                return true;
            }
        }
        return false;
    }
}
