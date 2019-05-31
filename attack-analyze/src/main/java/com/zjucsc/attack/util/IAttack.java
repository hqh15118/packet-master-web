package com.zjucsc.attack.util;

import com.zjucsc.tshark.packets.FvDimensionLayer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-31 - 22:27
 */
public interface IAttack {
    /**
     * 输入五元组，返回是否被攻击
     * @param layer
     * @return
     */
    boolean analyze(FvDimensionLayer layer);

}
