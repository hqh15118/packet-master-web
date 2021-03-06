package com.zjucsc.attack.analyze;

import com.zjucsc.attack.analyze.analyzer_util.CositeDosAttackAnalyzeList;
import com.zjucsc.attack.analyze.analyzer_util.MultisiteDosAttackAnalyzeList;
import com.zjucsc.attack.bean.AttackConfig;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-31 - 23:43
 */
public class DOSAttackAnalyzeListTest {

    @Test
    public void cositeDOSAttackTest(){
        CositeDosAttackAnalyzeList cositeDOSAttackAnalyzeList = new CositeDosAttackAnalyzeList();
        long time1 = System.currentTimeMillis();
        //[0,5,10,15,20,25,...,495]
        //false true true true
        List<FvDimensionLayer> fvDimensionLayers = createFvFvDimensionList(100);
        AttackConfig.setCoSiteNum(2);
        AttackConfig.setCoSiteTimeGap(10);
        for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
            System.out.println(cositeDOSAttackAnalyzeList.append(fvDimensionLayer));
        }
        System.out.println(System.currentTimeMillis() - time1);
        System.out.println(cositeDOSAttackAnalyzeList.getSize());
    }

    public  List<FvDimensionLayer> createMulDimensionList(int i) {
        List<FvDimensionLayer> fvDimensionLayers = new LinkedList<>();
        String[] srcIps = new String[]{
          "192.168.0.121","192.168.0.122","192.168.0.123"
        };
        for (int j = 0; j < i; j++) {
            FvDimensionLayer layer = new FvDimensionLayer();
            layer.frame_protocols[0] = "tcp";
            layer.tcp_flags_syn[0] = "1";
            layer.tcp_flags_ack[0] = "0";
            layer.timeStampInLong = j * 5;
            layer.ip_src[0] = srcIps[j%3];
            fvDimensionLayers.add(layer);
        }

        return fvDimensionLayers;
    }

    @Test
    public void mulsiteDOSAttackTest(){
        MultisiteDosAttackAnalyzeList multisiteDOSAttackAnalyzeList = new MultisiteDosAttackAnalyzeList();
        AttackConfig.setMultiSiteNum(3);
        AttackConfig.setMultiSiteTimeGap(10);
        List<FvDimensionLayer> fvDimensionLayers = createMulDimensionList(100);
        for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
            System.out.println(fvDimensionLayer.timeStampInLong + "--" + fvDimensionLayer.ip_src[0]);
        }
        for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
            System.out.println(multisiteDOSAttackAnalyzeList.append(fvDimensionLayer));
        }
    }

    private List<FvDimensionLayer> createFvFvDimensionList(int i) {
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


    @Test
    public void test(){
        byte[] bytes = new byte[]{
                0x01, (byte) 0xFF,-1, 0b0111111,
        };
    }

}