package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.attack.bean.DosConfig;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.FvDimensionList;

public class AbstractDosList extends FvDimensionList {
    private BaseAttackAnalyzer baseAttackAnalyzer;

    public DosConfig getDosConfig() {
        return baseAttackAnalyzer.getDosConfig();
    }

    public void setBaseAttackAnalyzer(BaseAttackAnalyzer baseAttackAnalyzer) {
        this.baseAttackAnalyzer = baseAttackAnalyzer;
    }
}
