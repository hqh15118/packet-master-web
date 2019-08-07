package com.zjucsc.attack.base;

import com.zjucsc.attack.bean.DosConfig;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface AnalyzePoolEntry {
    String append(FvDimensionLayer layer) throws InstantiationException, IllegalAccessException;
    boolean addDosAnalyzer(DosConfig dosConfig);
    void removeDosAnalyzer(String protocol);
    void enableDosAnalyzer(boolean enableConfig,String protocol);
}
