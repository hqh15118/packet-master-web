package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Queue;

//同源DOS攻击检测
public class CositeDosAttackAnalyzeList extends AbstractDosList {

    /**
     * @param fvDimensionLayers 新增加的五元组
     * @param newAppendLayer 新插入的五元组，时间戳可能不是最新的
     * @return
     *
     * 到达目标节点的源IP如下：
     * ip1是最先到达的
     * older << [ip1,ip2,ip1,ip3,ip4,ip1,ip5...ipx...ipn] << newer
     * 1.移除超时的【假设ipx之后的都是超时的】,[ip1,ip2,ip1,ip3...ipx]
     * 2.统计
     */
    @Override
    protected String analyze(Queue<FvDimensionLayer> fvDimensionLayers,
                             FvDimensionLayer newAppendLayer) {
        //没有配置，直接返回，没有攻击
        if (getDosConfig() == null || !getDosConfig().isEnable() || getDosConfig().getCoSiteNum() == 0 || getDosConfig().getCoSiteTime() == 0){
            return null;
        }
        long cositeTimeStamp = getDosConfig().getCoSiteTime();
        int cositeNum = getDosConfig().getCoSiteNum();
        if (fvDimensionLayers.size() < cositeTimeStamp){
            return null;
        }
        return doAnalyze(fvDimensionLayers,cositeTimeStamp,cositeNum);
    }


    private String doAnalyze(Queue<FvDimensionLayer> fvDimensionLayers,
                             long cositeTimeStamp,
                             int cositeNum){
        for (;;){
            FvDimensionLayer layer = fvDimensionLayers.poll();// min timeStampInLong
            if (layer == null){
                break;
            }else{
                if ((tailLayer.timeStampInLong - layer.timeStampInLong) > cositeTimeStamp){
                    fvDimensionLayers.remove(layer);
                }else{
                    break;
                }
            }
        }
        if (fvDimensionLayers.size() >= cositeNum){
            //debug log
            if (AttackConfig.debug){
                System.out.println("-------------");
                System.out.println("co site attack");
                System.out.println("-------------");
            }
            return "同源DOS攻击";
        }
        return null;
    }
}
