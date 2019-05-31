package com.zjucsc.attack.analyze;

import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedList;

public class MultisiteDOSAttackAnalyzeList extends TcpAttackAnalyzeList {


    @Override
    protected boolean analyze(int index, LinkedList<FvDimensionLayer> fvDimensionLayers, FvDimensionLayer layer) {
        return false;
    }
}
