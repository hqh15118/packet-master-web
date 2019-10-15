package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.Map;

public class ModbusAttackAnalyzelist extends FvDimensionList {
    private static Map<String,Short> TImap = new HashMap<>();
    @Override
    public String append(FvDimensionLayer layer) {
        /*
         * 保证是modbus请求，且是连接请求
         */
         if(layer.protocol.equals("modbus"))
         {
             if(!TImap.containsKey(layer.funCode)
                || TImap.get(layer.funCode)!=ByteUtil.bytesToShort(layer.tcpPayload,0))
             {
                 TImap.put(layer.funCode, ByteUtil.bytesToShort(layer.tcpPayload,0));
                 return super.append(layer);
             }
         }
         return null;
    }
}
