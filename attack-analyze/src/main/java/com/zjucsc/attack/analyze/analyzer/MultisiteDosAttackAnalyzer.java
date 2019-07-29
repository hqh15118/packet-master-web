package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.MultisiteDosAttackAnalyzeList;

import java.util.Map;

public class MultisiteDosAttackAnalyzer extends TcpDosAttackAnalyzer<MultisiteDosAttackAnalyzeList> {
    public MultisiteDosAttackAnalyzer(Map<String, MultisiteDosAttackAnalyzeList> stringMultisiteDosAttackAnalyzeListMap, Class<MultisiteDosAttackAnalyzeList> clazz) {
        super(stringMultisiteDosAttackAnalyzeListMap, clazz);
    }
}
