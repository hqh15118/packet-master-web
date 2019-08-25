package com.zjucsc.attack.base;

import com.zjucsc.attack.s7comm.S7OptName;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface IOptCommandAttackEntry {
    Object analyze(FvDimensionLayer layer, S7OptName s7OptName,Object...objs);
}
