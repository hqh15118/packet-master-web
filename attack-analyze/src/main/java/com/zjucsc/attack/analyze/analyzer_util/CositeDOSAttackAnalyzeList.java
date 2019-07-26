package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.bean.AttackConfigByDevice;
import com.zjucsc.attack.util.AttackCacheUtil;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedList;

//同源DOS攻击检测
public class CositeDOSAttackAnalyzeList extends FvDimensionList {

    /**
     *
     * @param index 新插入的五元组所在的索引
     * @param fvDimensionLayers 新增加的五元组
     * @param layer 新插入的五元组
     * @return
     *
     * 到达目标节点的源IP如下：
     * ip1是最新到达的
     * newer >> [ip1,ip2,ip1,ip3,ip4,ip1,ip5...ipx...ipn] >> older
     * 1.移除超时的【假设ipx之后的都是超时的】,[ip1,ip2,ip1,ip3...ipx]
     * 2.统计
     */
    @Override
    protected String analyze(int index,LinkedList<FvDimensionLayer> fvDimensionLayers,
                           FvDimensionLayer layer) {
        //如果总数量小于配置数量，那么一定返回false
        if (fvDimensionLayers.size() < AttackConfig.getCoSiteNum()){
            return null;
        }
        //        if (index == 0){//插入的位置是最大的
        //            //[最新时间[当前五元组对应的时间戳]，第二新时间，...]
        //            long newTime = fvDimensionLayers.get(0).timeStampInLong;    //最新一条报文到达时间戳
        //            long oldTime = fvDimensionLayers.get(1).timeStampInLong;    //上一条报文到达时间戳
        //            if (doAnalyze(newTime,oldTime)){
        //                AttackCommon.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
        //            }
        //        }else{//时间戳发生错位，插入的位置不是最大的
        //            long newerTime = fvDimensionLayers.get(index - 1).timeStampInLong;
        //            long newTime = fvDimensionLayers.get(index).timeStampInLong;
        //            long olderTime = fvDimensionLayers.get(index + 1).timeStampInLong;
        //            if (doAnalyze(newerTime,newTime)){
        //                AttackCommon.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
        //                return true;
        //            }else{
        //                if (doAnalyze(newTime,olderTime)){
        //                    AttackCommon.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
        //                    return true;
        //                }
        //            }
        //        }
        return doAnalyze(fvDimensionLayers, index);
    }

//    private boolean doAnalyze(long newTime , long oldTime){
//        //如果最新一条报文时间 - 上一条报文时间 < 额定时间，满足同源DOS攻击
//        return (newTime - oldTime) < AttackConfig.getCoSiteTimeGap();
//    }

    private String doAnalyze(LinkedList<FvDimensionLayer> fvDimensionLayers,int index){
        long timeStamp;
        for (int i = index - 1; i >= 0 ; i--) {
            FvDimensionLayer targetLayer = fvDimensionLayers.get(i);
            timeStamp = targetLayer.timeStampInLong;
            AttackConfigByDevice attackConfigByDevice = AttackCacheUtil.getAttackConfigByDevice(targetLayer.ip_dst[0]);
            //没有配置，直接返回，没有攻击
            if (attackConfigByDevice == null){
                return null;
            }
            while ((timeStamp - fvDimensionLayers.getLast().timeStampInLong) > attackConfigByDevice.getCositeTime()){
                fvDimensionLayers.removeLast();
            }
            if ((fvDimensionLayers.size() - i) >= attackConfigByDevice.getCositeNum()){
                //debug log
                if (AttackConfig.debug){
                    System.out.println("-------------");
                    System.out.println("co site attack");
                    System.out.println("-------------");
                }
                return "同源DOS攻击";
            }
        }
        return null;
    }
}
