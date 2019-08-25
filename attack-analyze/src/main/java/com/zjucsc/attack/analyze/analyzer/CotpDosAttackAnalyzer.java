package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.AbstractDosList;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public class CotpDosAttackAnalyzer<T extends AbstractDosList> extends BaseAttackAnalyzer<T> {

    public CotpDosAttackAnalyzer (Map<String, T> t, Class<T> clazz) {
        super(t, clazz);
    }

    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        return false;/////
    }

}
