package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Arrays;

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

    }
}
