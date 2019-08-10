package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.DosConfig;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class CositeDosAttackAnalyzeListTest {


    @Test
    public void cositeDosTest(){
        CositeDosAttackAnalyzeList cositeDosAttackAnalyzeList = new CositeDosAttackAnalyzeList();
        BaseAttackAnalyzer<CositeDosAttackAnalyzeList> baseAttackAnalyzer = new BaseAttackAnalyzer<CositeDosAttackAnalyzeList>(
                new HashMap<>(),CositeDosAttackAnalyzeList.class
        ) {
            @Override
            protected boolean validPacket(FvDimensionLayer layer) {
                return true;
            }
        };
        cositeDosAttackAnalyzeList.setBaseAttackAnalyzer(baseAttackAnalyzer);
        DosConfig dosConfig = new DosConfig();
        dosConfig.setEnable(true);
        dosConfig.setCoSiteNum(9999);
        dosConfig.setCoSiteTime(100000);
        dosConfig.setProtocol("tcp");
        baseAttackAnalyzer.setDosConfig(dosConfig);
        int timeStart = 0;
        List<FvDimensionLayer> fvDimensionLayers = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            FvDimensionLayer layer = new FvDimensionLayer();
            layer.setTcp_flags_ack(new String[]{"0"});
            layer.setTcp_flags_syn(new String[]{"1"});
            timeStart += 10 ;
            layer.setTimeStampInLong(timeStart);
            fvDimensionLayers.add(layer);
        }
        String res = null;
        for (FvDimensionLayer layer : fvDimensionLayers) {
            res = cositeDosAttackAnalyzeList.append(layer);
            if (res!=null){
                System.out.println(res);
            }
        }
    }
}