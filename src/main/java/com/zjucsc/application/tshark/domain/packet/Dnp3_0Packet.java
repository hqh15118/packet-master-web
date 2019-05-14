package com.zjucsc.application.tshark.domain.packet;

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

    public static class LayersBean extends FvDimensionLayer{
        public String[] dnp3_ctl_dir;
        public String[] dnp3_ctl_prm;
        public String[] dnp3_ctl_dfc;
        public String[] dnp3_ctl_secfunc;
    }
}
