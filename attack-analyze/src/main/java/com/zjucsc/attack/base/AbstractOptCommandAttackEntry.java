package com.zjucsc.attack.base;

import com.zjucsc.attack.AttackCommon;
import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public abstract class AbstractOptCommandAttackEntry<T extends BaseOpName> implements IOptCommandAttackEntry<T>{
    protected void commandCallback(String command, FvDimensionLayer layer , Object...objs){
        AttackCommon.appendCommandFvDimensionPacket(command,layer,objs);
    }

    public void afterRoundDecode(){

    }
}
