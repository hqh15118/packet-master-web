package com.zjucsc.attack.common;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.util.AttackAnalyzeUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import redis.clients.jedis.Jedis;

import java.time.temporal.TemporalUnit;
import java.util.PriorityQueue;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:45
 */
public class AnalyzeTask implements Runnable {

    private FvDimensionLayer layer;

    public AnalyzeTask(FvDimensionLayer layer) {
        this.layer = layer;
    }

    @Override
    public void run() {
        String srcIp = layer.ip_src[0];
        //D_DOS1

    }
}