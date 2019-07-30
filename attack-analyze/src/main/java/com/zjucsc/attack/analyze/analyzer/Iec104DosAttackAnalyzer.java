package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public class Iec104DosAttackAnalyzer<T extends FvDimensionList> extends BaseAttackAnalyzer<T> {

    /**
     * 通过构造函数注入分析结构
     * @param t key 源地址，value 分析的数据结构
     * @param clazz
     */
    public Iec104DosAttackAnalyzer(Map<String, T> t, Class<T> clazz) {
        super(t, clazz);
    }

    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        return layer.protocol.equals("104asdu") && layer.funCode.equals("1");
    }

}
