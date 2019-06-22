package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.tshark.packets.FvDimensionLayer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 20:43
 */
public class UndefinedPacket {
    @JSONField(name = "layers")
    public LayersBean layers;


    public static class LayersBean extends FvDimensionLayer {

    }
}
