package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.MultisiteDOSAttackAnalyzeList;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-31 - 22:36
 */
public class MultisiteDOSAttackAnalyzer extends BaseAttackAnalyzer<MultisiteDOSAttackAnalyzeList> {


    public MultisiteDOSAttackAnalyzer(MultisiteDOSAttackAnalyzeList multisiteDOSAttackAnalyzeList) {
        super(multisiteDOSAttackAnalyzeList);
    }

    @Override
    public boolean analyze(FvDimensionLayer layer) {
        return getAnalyzer().append(layer);
    }
}
