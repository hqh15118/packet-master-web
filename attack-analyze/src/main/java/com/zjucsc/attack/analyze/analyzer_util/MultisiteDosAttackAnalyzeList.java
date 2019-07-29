package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.bean.AttackConfigByDevice;
import com.zjucsc.attack.bean.MultisiteFvDimensionAttackWrapper;
import com.zjucsc.attack.util.AttackCacheUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedHashSet;
import java.util.Queue;

public class MultisiteDosAttackAnalyzeList extends TcpAttackAnalyzeList {

    private LinkedHashSet<MultisiteFvDimensionAttackWrapper> hashSet = new LinkedHashSet<>();

    /**
     * @param fvDimensionLayers 新增加的五元组
     * @param newAppendLayer 新插入的五元组
     * @return
     * 到达目标节点的源IP如下：
     * ip1是最旧到达的
     * older << [ip1,ip2,ip1,ip3,ip4,ip1,ip5...ipx...ipn] << newer
     * 1.移除超时的【假设ipx之后的都是超时的】,[ip1,ip2,ip1,ip3...ipx]
     * 2.移除重复的[ip1,ip2,ip3...ipx]
     * 3.统计
     */
    @Override
    protected String analyze(Queue<FvDimensionLayer> fvDimensionLayers,
                                    FvDimensionLayer newAppendLayer) {
        hashSet.clear();
        //如果总数量小于配置数量，那么一定返回false
        AttackConfigByDevice attackConfigByDevice = AttackCacheUtil.getAttackConfigByDevice(newAppendLayer.ip_dst[0]);
        //没有配置，直接返回，没有攻击
        if (attackConfigByDevice == null || attackConfigByDevice.getMulsiteTime() == 0 || attackConfigByDevice.getMulsiteNum() == 0){
            return null;
        }
        long cositeTimeStamp = attackConfigByDevice.getCositeTime();
        int cositeNum = attackConfigByDevice.getCositeNum();
        if (fvDimensionLayers.size() < cositeTimeStamp){
            return null;
        }
        return doAnalyze(fvDimensionLayers,cositeTimeStamp,cositeNum);
    }

    private String doAnalyze(Queue<FvDimensionLayer> fvDimensionLayers, long multiSiteTimeStamp, int multiSiteNum) {
        for (;;){
            FvDimensionLayer layer = fvDimensionLayers.poll();// min timeStampInLong
            if (layer == null){
                break;
            }else{
                if ((tailLayer.timeStampInLong - layer.timeStampInLong) > multiSiteTimeStamp){
                    fvDimensionLayers.remove(layer);
                }else{
                    break;
                }
            }
        }
        for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
            hashSet.add(new MultisiteFvDimensionAttackWrapper(fvDimensionLayer));
        }
        if (hashSet.size()  >= multiSiteNum){
            if (AttackConfig.debug) {
                System.out.println("-------------");
                System.out.println("multi attack");
                System.out.println("-------------");
            }
            return "多址DOS攻击";
        }
        return null;
    }

    public int getSetSize(){
        return hashSet.size();
    }
}
