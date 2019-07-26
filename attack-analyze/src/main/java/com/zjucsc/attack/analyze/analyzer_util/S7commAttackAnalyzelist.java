package com.zjucsc.attack.analyze.analyzer_util;

import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.S7CommPacket;

public class S7commAttackAnalyzelist extends FvDimensionList {
    @Override
    public String append(FvDimensionLayer layer) {
        /*
         * 保证是modbus请求，且是连接请求
         */
        if(layer.protocol.equals("s7comm") && layer.funCode.equals("240")
            && ((S7CommPacket.LayersBean)layer).s7comm_header_rosctr[0].equals("1"))
        {
                return super.append(layer);
        }
        return null;
    }
}
