package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public class Dnp3DosAttackAnalyzer extends BaseAttackAnalyzer<FvDimensionList> {
    /**
     * 通过构造函数注入分析结构
     *
     * @param t     key 源地址，value 分析的数据结构
     * @param clazz
     */
    public Dnp3DosAttackAnalyzer(Map<String, FvDimensionList> t, Class<FvDimensionList> clazz) {
        super(t, clazz);
    }

    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        return layer.protocol.equals("dnp3") && layer.funCode.equals("0");
    }
}
