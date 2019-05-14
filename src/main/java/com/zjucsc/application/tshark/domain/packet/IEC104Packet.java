package com.zjucsc.application.tshark.domain.packet;

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

    public static class LayersBean extends FvDimensionLayer{
        public String[] iec60870_asdu_start;
        public String[] iec60870_104_apdulen;
        public String[] iec60870_104_type;
        public String[] iec60870_104_utype;
    }
}
