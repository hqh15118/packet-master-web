package com.zjucsc.attack.analyze;

import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

public class TcpAttackAnalyzeList extends FvDimensionList {
    @Override
    public void append(FvDimensionLayer layer) {
        /*
         * 保证是TCP请求，且是连接请求
         */
        if (layer.frame_protocols[0].endsWith("tcp") &&
            layer.tcp_flags_ack[0].equals("0")&&
            layer.tcp_flags_syn[0].equals("1")) {
            super.append(layer);
        }
    }
}
