package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public class Dnp3AttackAnalyzeList extends FvDimensionList {
    @Override
    public String append(FvDimensionLayer layer)
    {
        if(layer.protocol.equals("dnp3") && layer.funCode.equals("0"))
        {
            return super.append(layer);
        }
        return null;
    }
}
