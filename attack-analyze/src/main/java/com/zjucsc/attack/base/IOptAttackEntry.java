package com.zjucsc.attack.base;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public interface IOptAttackEntry {
    /**
     * 操作码分析
     * @param layer 五元组
     * @param objs 额外信息
     * @return 攻击信息，null表示没有攻击
     */
    AttackBean analyze(FvDimensionLayer layer, Map<String , Float> techmap, Object...objs);
}
