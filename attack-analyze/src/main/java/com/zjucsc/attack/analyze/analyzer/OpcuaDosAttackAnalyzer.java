package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.AbstractDosList;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public class OpcuaDosAttackAnalyzer <T extends AbstractDosList> extends BaseAttackAnalyzer<T> {

    public OpcuaDosAttackAnalyzer(Map<String, T> t, Class<T> clazz) {
        super(t, clazz);
    }

    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        /*
         * 保证是opcua请求，且是连接请求
         */
        return layer.funCode.equals("461");
    }
}
