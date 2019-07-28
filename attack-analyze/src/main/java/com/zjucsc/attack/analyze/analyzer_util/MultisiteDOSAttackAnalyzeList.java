package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.MultisiteFvDimensionAttackWrapper;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedHashSet;
import java.util.Queue;

public class MultisiteDOSAttackAnalyzeList extends TcpAttackAnalyzeList {

    private LinkedHashSet<MultisiteFvDimensionAttackWrapper> hashSet = new LinkedHashSet<>();

    /**
     * @param fvDimensionLayers 新增加的五元组
     * @param layer 新插入的五元组
     * @return
     * 到达目标节点的源IP如下：
     * ip1是最新到达的
     * newer >> [ip1,ip2,ip1,ip3,ip4,ip1,ip5...ipx...ipn] >> older
     * 1.移除超时的【假设ipx之后的都是超时的】,[ip1,ip2,ip1,ip3...ipx]
     * 2.移除重复的[ip1,ip2,ip3...ipx]
     * 3.统计
     */
    @Override
    protected String analyze(Queue<FvDimensionLayer> fvDimensionLayers,
                                    FvDimensionLayer layer) {
//        hashSet.clear();
//        long timeStamp;
//        for (int i = index - 1; i >= 0 ; i--) {
//            FvDimensionLayer targetLayer = fvDimensionLayers.get(i);
//            timeStamp = targetLayer.timeStampInLong;
//            AttackConfigByDevice attackConfigByDevice = AttackCacheUtil.getAttackConfigByDevice(targetLayer.ip_dst[0]);
//            if (attackConfigByDevice == null){
//                return null;
//            }
//            while ((timeStamp - fvDimensionLayers.getLast().timeStampInLong) > attackConfigByDevice.getMulsiteTime()){
//                fvDimensionLayers.removeLast();
//            }
//            for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
//                hashSet.add(new MultisiteFvDimensionAttackWrapper(fvDimensionLayer));
//            }
//            if ((hashSet.size() - i) >= attackConfigByDevice.getMulsiteNum()){
//                if (AttackConfig.debug) {
//                    System.out.println("-------------");
//                    System.out.println("multi attack");
//                    System.out.println("-------------");
//                }
//                return "多址DOS攻击";
//            }
//        }
        return null;
    }

    public int getSetSize(){
        return hashSet.size();
    }
}
