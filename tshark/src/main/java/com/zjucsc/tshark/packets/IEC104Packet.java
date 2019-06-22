package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:16
 */
public class IEC104Packet {

    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "104asdu_typeid")
        public String[] iec104_funcode;
    }
}
