package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

public class CipPacket {
    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "cip_sc")
        public String[] cip_funcode = {"--"};
    }
}
