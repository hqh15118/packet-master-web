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
import static com.zjucsc.attack.s7comm.s7Opdecode.getdBclasses;

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
        return getdBclasses(S7layer, DBlist);
    }

    private boolean OperationDecode(List<DBclass> DBlist, S7OptAttackConfig s7OptAttackConfig)
    {
        if(DBlist==null || s7OptAttackConfig ==null)
        {
            return false;
        }
        if (s7Opdecode.s7m1(DBlist, s7OptAttackConfig.getDBnum(), s7OptAttackConfig.getByteoffset(), s7OptAttackConfig.getBitoffset(), s7OptAttackConfig.isResult()))
            return true;
        return false;
    }

//    private GetS7load getS7load = new GetS7load();

//    private Map<Integer, List<DBclass>> DBmap = new HashMap<>();

    @Override
    public AttackBean doAnalyze(FvDimensionLayer layer, Map<String,Float> techmap, S7OptAttackConfig s7OptAttackConfig, Object... objs) {
        return Attackdecode(layer, s7OptAttackConfig,techmap);
    }
}
