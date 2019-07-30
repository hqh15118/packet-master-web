package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.bean.MultisiteFvDimensionAttackWrapper;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedHashSet;
import java.util.Queue;

public class MultisiteDosAttackAnalyzeList extends AbstractDosList {

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
        //没有配置，直接返回，没有攻击
        if ( getDosConfig() == null || getDosConfig().getMulSiteNum() == 0 || getDosConfig().getMulSiteTime() == 0){
            return null;
        }
        long mulSiteTime = getDosConfig().getMulSiteTime();
        int mulSiteNum =  getDosConfig().getMulSiteNum();
        if (fvDimensionLayers.size() < mulSiteNum){
            return null;
        }
        return doAnalyze(fvDimensionLayers,mulSiteTime,mulSiteNum);
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
