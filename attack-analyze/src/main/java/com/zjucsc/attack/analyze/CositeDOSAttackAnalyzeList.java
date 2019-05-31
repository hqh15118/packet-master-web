package com.zjucsc.attack.analyze;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedList;

//同源DOS攻击检测
public class CositeDOSAttackAnalyzeList extends TcpAttackAnalyzeList {

    @Override
    protected boolean analyze(int index,LinkedList<FvDimensionLayer> fvDimensionLayers,
                           FvDimensionLayer layer) {
        //如果总数量小于配置数量，那么一定返回false
        if (fvDimensionLayers.size() < AttackConfig.getCoSiteNum()){
            return false;
        }
        //        if (index == 0){//插入的位置是最大的
        //            //[最新时间[当前五元组对应的时间戳]，第二新时间，...]
        //            long newTime = fvDimensionLayers.get(0).timeStampInLong;    //最新一条报文到达时间戳
        //            long oldTime = fvDimensionLayers.get(1).timeStampInLong;    //上一条报文到达时间戳
        //            if (doAnalyze(newTime,oldTime)){
        //                Common.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
        //            }
        //        }else{//时间戳发生错位，插入的位置不是最大的
        //            long newerTime = fvDimensionLayers.get(index - 1).timeStampInLong;
        //            long newTime = fvDimensionLayers.get(index).timeStampInLong;
        //            long olderTime = fvDimensionLayers.get(index + 1).timeStampInLong;
        //            if (doAnalyze(newerTime,newTime)){
        //                Common.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
        //                return true;
        //            }else{
        //                if (doAnalyze(newTime,olderTime)){
        //                    Common.updateAll(new AttackBean(layer,"同源TCP-DOS统计"));
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

    private boolean doAnalyze(LinkedList<FvDimensionLayer> fvDimensionLayers,int index){
        long timeStamp;
        for (int i = index; i >= 0 ; i--) {
            timeStamp = fvDimensionLayers.get(i).timeStampInLong;
            while ((timeStamp - fvDimensionLayers.getLast().timeStampInLong) > AttackConfig.getCoSiteTimeGap()){
                fvDimensionLayers.removeLast();
            }
            //debug log
            if (AttackConfig.debug){
                for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
                    System.out.println(fvDimensionLayer.timeStampInLong);
                }
            }
            if ((fvDimensionLayers.size() - i) >= AttackConfig.getCoSiteNum()){
                //debug log
                if (AttackConfig.debug){
                    System.out.println("true");
                    System.out.println("-------------");
                }
                return true;
            }
            //debug log
            if (AttackConfig.debug){
                System.out.println("false");
                System.out.println("-------------");
            }
        }
        return false;
    }

}
