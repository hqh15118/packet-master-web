package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

public class OpcDaPacket {
    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "dcerpc_pkt_type")
        public String[] dcerpc_pkt_type;
        @JSONField(name = "dcerpc_datype")
        public String[] dcerpc_datype;
        @JSONField(name = "dcerpc_stub_data")
        public String[] dcerpc_stub_data;
        @JSONField(name = "dcerpc_cn_call_id")
        public String[] dcerpc_cn_call_id;
    }
}
