package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.AbstractDosList;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.Map;

public class ModbusDosAttackAnalyzer<T extends AbstractDosList> extends BaseAttackAnalyzer<T> {
    /**
     * 通过构造函数注入分析结构
     *
     * @param t     key 源地址，value 分析的数据结构
     * @param clazz
     */
    public ModbusDosAttackAnalyzer(Map<String, T> t, Class<T> clazz) {
        super(t, clazz);
    }

    //FIXME
    private Map<String,Short> TImap = new HashMap<>();
    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        /*
         * 保证是modbus请求，且是连接请求
         */
            if(!TImap.containsKey(layer.funCode)
                    || TImap.get(layer.funCode)!= ByteUtil.bytesToShort(layer.getUseTcpPayload(),0))
            {
                TImap.put(layer.funCode, ByteUtil.bytesToShort(layer.getUseTcpPayload(),0));
                return true;
            }
        return false;
    }
}
