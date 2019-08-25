package com.zjucsc.attack.base;

import com.zjucsc.attack.AttackCommon;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public abstract class AbstractOptCommandAttackEntry implements IOptCommandAttackEntry{
    public void commandCallback(String command, FvDimensionLayer layer , Object...objs){
        AttackCommon.appendCommandFvDimensionPacket(command,layer,objs);
    }
}
