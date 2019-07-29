package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.S7CommPacket;

import java.util.Map;

public class S7commDosAttackAnalyzer extends BaseAttackAnalyzer<FvDimensionList> {
    /**
     * 通过构造函数注入分析结构
     *
     * @param t     key 源地址，value 分析的数据结构
     * @param clazz
     */
    public S7commDosAttackAnalyzer(Map<String, FvDimensionList> t, Class<FvDimensionList> clazz) {
        super(t, clazz);
    }

    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        /*
         * 保证是modbus请求，且是连接请求
         */
        return layer.protocol.equals("s7comm") && layer.funCode.equals("240")
                && ((S7CommPacket.LayersBean) layer).s7comm_header_rosctr[0].equals("1");
    }
}
