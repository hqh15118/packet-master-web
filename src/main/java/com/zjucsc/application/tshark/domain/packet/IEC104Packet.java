package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Arrays;

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
        @JSONField(name = "104asdu_start")
        public String[] iec60870_asdu_start;
        @JSONField(name = "104apci_apdulen")
        public String[] iec60870_104_apdulen;
        @JSONField(name = "104apci_type")
        public String[] iec60870_104_type;
        @JSONField(name = "104apci_utype")
        public String[] iec60870_104_utype;
    }
}
