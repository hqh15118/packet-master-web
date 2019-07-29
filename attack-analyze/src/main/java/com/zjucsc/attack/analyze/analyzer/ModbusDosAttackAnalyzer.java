package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.Map;

public class ModbusDosAttackAnalyzer extends BaseAttackAnalyzer<FvDimensionList> {
    /**
     * 通过构造函数注入分析结构
     *
     * @param t     key 源地址，value 分析的数据结构
     * @param clazz
     */
    public ModbusDosAttackAnalyzer(Map<String, FvDimensionList> t, Class<FvDimensionList> clazz) {
        super(t, clazz);
    }

    //FIXME
    private Map<String,Short> TImap = new HashMap<>();
    @Override
    protected boolean validPacket(FvDimensionLayer layer) {
        /*
         * 保证是modbus请求，且是连接请求
         */
        if(layer.protocol.equals("modbus"))
        {
            if(!TImap.containsKey(layer.funCode)
                    || TImap.get(layer.funCode)!= ByteUtil.bytesToShort(layer.tcpPayload,0))
            {
                TImap.put(layer.funCode, ByteUtil.bytesToShort(layer.tcpPayload,0));
                return true;
            }
        }
        return false;
    }
}
