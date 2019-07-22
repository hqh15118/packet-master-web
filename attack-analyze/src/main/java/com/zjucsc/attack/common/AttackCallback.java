package com.zjucsc.attack.common;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface AttackCallback {
    void artCallback(AttackBean attackBean, FvDimensionLayer layer);
}
