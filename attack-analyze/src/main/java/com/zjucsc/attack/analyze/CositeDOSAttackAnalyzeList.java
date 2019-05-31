package com.zjucsc.attack.analyze;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.attack.common.Common;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedList;

//同源DOS攻击检测
public class CositeDOSAttackAnalyzeList extends TcpAttackAnalyzeList {

    @Override
    protected void analyze(int index,LinkedList<FvDimensionLayer> fvDimensionLayers,
                           FvDimensionLayer layer) {
        if (index == 0){//插入的位置是最大的
            long newTime = fvDimensionLayers.get(0).timeStampInLong;    //最新一条报文到达时间戳
            long oldTime = fvDimensionLayers.get(1).timeStampInLong;    //上一条报文到达时间戳
            if (doAnalyze(newTime,oldTime)){
                Common.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
            }
        }else{//时间戳发生错位，插入的位置不是最大的
            long newerTime = fvDimensionLayers.get(index - 1).timeStampInLong;
            long newTime = fvDimensionLayers.get(index).timeStampInLong;
            long olderTime = fvDimensionLayers.get(index + 1).timeStampInLong;
            if (doAnalyze(newerTime,newTime)){
                Common.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
            }else{
                if (doAnalyze(newTime,olderTime)){
                    Common.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
                }
            }
        }
    }

    private boolean doAnalyze(long newTime , long oldTime){
        //如果最新一条报文时间 - 上一条报文时间 < 额定时间，满足同源DOS攻击
        return (newTime - oldTime) < AttackConfig.getCoSiteTimeGap();
    }
}
