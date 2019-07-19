package com.zjucsc.attack.common;

import com.zjucsc.tshark.packets.FvDimensionLayer;

import static com.zjucsc.attack.AttackCommon.doAnalyzeFvDimension;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:45
 */
public class CommonAnalyzeTask implements Runnable {

    private FvDimensionLayer layer;

    public CommonAnalyzeTask(FvDimensionLayer layer) {
        this.layer = layer;
    }

    @Override
    public void run() {
        doAnalyzeFvDimension(layer);
    }
}
