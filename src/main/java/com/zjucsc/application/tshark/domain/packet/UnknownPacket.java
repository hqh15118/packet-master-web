package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 20:43
 */
public class UnknownPacket {
    @JSONField(name = "layers")
    public LayersBean layers;


    public static class LayersBean extends FvDimensionLayer {
        @Override
        public String toString() {
            return super.toString();
        }
    }
}
