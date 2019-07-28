package com.zjucsc.attack.common;

import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.List;
import java.util.Map;

import static com.zjucsc.attack.common.ArtAttackAnalyzeUtil.attackDecode;

public class ArtAttackAnalyzeTask implements Runnable{

    private List<String> expression;
    private Map<String,Float> techmap;
    private String description;
    private FvDimensionLayer layer;

    public ArtAttackAnalyzeTask(List<String> expression , Map<String,Float> techmap , String description,
                                FvDimensionLayer layer) {
        this.expression = expression;
        this.techmap = techmap;
        this.description = description;
        this.layer = layer;
    }

    @Override
    public void run() {
        String res = attackDecode(expression,techmap,description);
        if (res!=null){
            AttackCommon.appendFvDimensionError(AttackBean.builder()
                    .attackType(AttackTypePro.ART_EXCEPTION)
                    .fvDimension(layer)
                    .attackInfo(res).build(),layer);
        }
        //help GC
        techmap = null;
        expression = null;
        layer = null;
        description = null;
    }
}
