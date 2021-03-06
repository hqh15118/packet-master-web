package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.AbstractDosList;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public class TcpDosAttackAnalyzer<T extends AbstractDosList> extends BaseAttackAnalyzer<T> {

    public TcpDosAttackAnalyzer(Map<String, T> stringTMap, Class<T> clazz) {
        super(stringTMap, clazz);
    }

    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        /*
         * 保证是TCP请求，且是连接请求
         */
        return layer.tcp_flags_ack[0].equals("0") &&
                layer.tcp_flags_syn[0].equals("1");
    }
}
