package com.zjucsc.attack.common;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public class CommonAttackAnalyzeTask implements Runnable {

    private FvDimensionLayer layer;
    private Object[] objs;
    private AttackCallback attackCallback;
    public CommonAttackAnalyzeTask(FvDimensionLayer layer,
                                   AttackCallback attackCallback,
                                   Object[] objs){
        this.layer = layer;
        this.objs = objs;
        this.attackCallback = attackCallback;
    }

    @Override
    public void run() {
        //sniff
        //eth:llc:data
        String protocolStack = layer.frame_protocols[0];
        if (protocolStack.length() >= 8 && protocolStack.charAt(4) == 'l' && protocolStack.charAt(5) == 'l'
        && protocolStack.charAt(6) == 'c' && layer.rawData[14]==(byte)0xaa && layer.rawData[15]==(byte)0xaa ){
            attackCallback.artCallback(AttackBean.builder().attackType(AttackTypePro.SNIFF_ATTACK)
            .fvDimension(layer).build());
        }
    }
}
