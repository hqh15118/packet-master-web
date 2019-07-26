package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public class Iec104AttackAnalyzeList extends FvDimensionList {
    @Override
    public String append(FvDimensionLayer layer)
    {
        if(layer.protocol.equals("104asdu") && layer.funCode.equals("1"))
        {
            return super.append(layer);
        }
        return null;
    }
}
