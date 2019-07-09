package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:16
 */
public class Dnp3_0Packet {

    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "dnp3_ctl_prifunc")
        public String[] dnp3_ctl_prifunc = {""};
        @JSONField(name = "dnp3_ctl_secfunc")
        public String[] dnp3_ctl_secfunc = {""};
        @JSONField(name = "dnp3_ctl_prm")
        public String[] dnp3_ctl_prm={"-1"};
    }
}
