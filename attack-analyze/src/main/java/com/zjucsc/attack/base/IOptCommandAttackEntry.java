package com.zjucsc.attack.base;

import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface IOptCommandAttackEntry<T extends BaseOpName> {
    Object analyze(FvDimensionLayer layer, T opNameConfig, Object...objs);
}
