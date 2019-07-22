package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

public class MmsPacket {
    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "mms_confirmedServiceRequest")
        public String[] mmsFuncode = {"-1"};
    }
}
