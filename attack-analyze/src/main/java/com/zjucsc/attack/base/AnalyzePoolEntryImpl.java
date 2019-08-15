package com.zjucsc.attack.base;

import com.zjucsc.attack.analyze.analyzer.*;
import com.zjucsc.attack.analyze.analyzer_util.CositeDosAttackAnalyzeList;
import com.zjucsc.attack.analyze.analyzer_util.MultisiteDosAttackAnalyzeList;
import com.zjucsc.attack.bean.DosConfig;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 每一个设备都定义一个AnalyzePoolEntryImpl
 */
public class AnalyzePoolEntryImpl implements AnalyzePoolEntry {
    private ConcurrentHashMap<String, List<BaseAttackAnalyzer>> analyzerHashMap = new ConcurrentHashMap<>();
    @Override
    public String append(FvDimensionLayer layer) throws InstantiationException, IllegalAccessException {
        List<BaseAttackAnalyzer> analyzers = analyzerHashMap.get(layer.protocol);
        if (analyzers != null){
            String msg = null;
            for (BaseAttackAnalyzer analyzer : analyzers) {
                String temp = analyzer.analyze(layer);
                if (temp!=null){
                    msg = temp;
                    break;
                }
            }
            return msg;
        }
        return null;
    }

    @Override
    public boolean addDosAnalyzer(DosConfig dosConfig){
        List<BaseAttackAnalyzer> baseAttackAnalyzers = getBaseAttackAnalyzer(dosConfig);
        if (baseAttackAnalyzers!=null){
            analyzerHashMap.put(dosConfig.getProtocol(),baseAttackAnalyzers);
            return true;
        }
        return false;
    }

    @Override
    public void removeDosAnalyzer(String protocol) {
        List<BaseAttackAnalyzer> baseAttackAnalyzers = analyzerHashMap.get(protocol);
        for (BaseAttackAnalyzer baseAttackAnalyzer : baseAttackAnalyzers) {
            baseAttackAnalyzer.setDosConfig(null);
        }
    }

    @Override
    public void enableDosAnalyzer(boolean enableConfig,String protocol) {
        List<BaseAttackAnalyzer> baseAttackAnalyzers = analyzerHashMap.get(protocol);
        for (BaseAttackAnalyzer baseAttackAnalyzer : baseAttackAnalyzers) {
            baseAttackAnalyzer.getDosConfig().setEnable(enableConfig);
        }
    }

    private List<BaseAttackAnalyzer> getBaseAttackAnalyzer(DosConfig dosConfig){
        switch (dosConfig.getProtocol()){
            case "tcp" :
                return Arrays.asList(new TcpDosAttackAnalyzer<>(new HashMap<>(), CositeDosAttackAnalyzeList.class).setDosConfig(dosConfig),
                    new TcpDosAttackAnalyzer<>(null, MultisiteDosAttackAnalyzeList.class).setDosConfig(dosConfig));
            case "dnp3" : return Arrays.asList(new Dnp3DosAttackAnalyzer<>(new HashMap<>(), CositeDosAttackAnalyzeList.class).setDosConfig(dosConfig),
                    new Dnp3DosAttackAnalyzer<>(null, MultisiteDosAttackAnalyzeList.class).setDosConfig(dosConfig));
            case "104apci" : return Arrays.asList(new Iec104DosAttackAnalyzer<>(new HashMap<>(), CositeDosAttackAnalyzeList.class).setDosConfig(dosConfig),
                    new Iec104DosAttackAnalyzer<>(null, MultisiteDosAttackAnalyzeList.class).setDosConfig(dosConfig));
            case "modbus" : return Arrays.asList(new ModbusDosAttackAnalyzer<>(new HashMap<>(), CositeDosAttackAnalyzeList.class).setDosConfig(dosConfig),
                    new ModbusDosAttackAnalyzer<>(null, MultisiteDosAttackAnalyzeList.class).setDosConfig(dosConfig));
            case "s7comm" : return Arrays.asList(new S7commDosAttackAnalyzer<>(new HashMap<>(), CositeDosAttackAnalyzeList.class).setDosConfig(dosConfig),
                    new S7commDosAttackAnalyzer<>(null, MultisiteDosAttackAnalyzeList.class).setDosConfig(dosConfig));
        }
        return null;
    }

    @Override
    public String toString() {
        return "AnalyzePoolEntryImpl{" +
                "analyzerHashMap=" + analyzerHashMap +
                '}';
    }
}
