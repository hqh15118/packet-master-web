package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.bean.MultisiteFvDimensionAttackWrapper;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class MultisiteDOSAttackAnalyzeList extends TcpAttackAnalyzeList {

    private LinkedHashSet<MultisiteFvDimensionAttackWrapper> hashSet = new LinkedHashSet<>();

    /**
     *
     * @param index 新插入的五元组所在的索引
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
    protected boolean analyze(int index, LinkedList<FvDimensionLayer> fvDimensionLayers, FvDimensionLayer layer) {
        hashSet.clear();
        long timeStamp;
        for (int i = index; i >= 0 ; i--) {
            timeStamp = fvDimensionLayers.get(i).timeStampInLong;
            while ((timeStamp - fvDimensionLayers.getLast().timeStampInLong) > AttackConfig.getMultiSiteTimeGap()){
                fvDimensionLayers.removeLast();
            }
            for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
                hashSet.add(new MultisiteFvDimensionAttackWrapper(fvDimensionLayer));
            }
            if ((hashSet.size() - i) >= AttackConfig.getMultiSiteNum()){
                return true;
            }
        }
        return false;
    }

    public int getSetSize(){
        return hashSet.size();
    }
}
