package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-17 - 23:10
 */
public class SingleProtocol {
    @JSONField(name = "layers")
    public LayersBean layers;
    public static class LayersBean{
        public String[] frame_protocols = {""};
    }
}
