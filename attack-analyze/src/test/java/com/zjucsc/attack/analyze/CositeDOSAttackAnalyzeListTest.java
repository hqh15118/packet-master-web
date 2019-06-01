package com.zjucsc.attack.analyze;

import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-31 - 23:43
 */
public class CositeDOSAttackAnalyzeListTest {

    @Test
    public void cositeDOSAttackTest(){
        CositeDOSAttackAnalyzeList cositeDOSAttackAnalyzeList = new CositeDOSAttackAnalyzeList();
        long time1 = System.currentTimeMillis();
        //[0,5,10,15,20,25,...,495]
        //false true true true
        List<FvDimensionLayer> fvDimensionLayers = createFvDimensionList(100);
        AttackConfig.setCoSiteNum(2);
        AttackConfig.setCoSiteTimeGap(10);
        for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
            System.out.println(cositeDOSAttackAnalyzeList.append(fvDimensionLayer));
        }
        System.out.println(System.currentTimeMillis() - time1);
        System.out.println(cositeDOSAttackAnalyzeList.getSize());
    }

    private List<FvDimensionLayer> createFvDimensionList(int i) {
        List<FvDimensionLayer> fvDimensionLayers = new LinkedList<>();
        for (int j = 0; j < i; j++) {
            FvDimensionLayer layer = new FvDimensionLayer();
            layer.frame_protocols[0] = "tcp";
            layer.tcp_flags_syn[0] = "1";
            layer.tcp_flags_ack[0] = "0";
            layer.timeStampInLong = j * 5;
            fvDimensionLayers.add(layer);
        }
        return fvDimensionLayers;
    }

}