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

    /**
     * objAndVar1 -- 决定了下面的数据属于哪种类型
     *      -- point index -- 数据索引
     *          [] -- Value(analog)
     *          [] -- dnp3_al_boq_b7(binary)
     * objAndVar2
     * ...
     */
    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "dnp3_ctl_prifunc")
        public String[] dnp3_ctl_prifunc = {""};
        @JSONField(name = "dnp3_ctl_secfunc")
        public String[] dnp3_ctl_secfunc = {""};
        @JSONField(name = "dnp3_ctl_prm")
        public String[] dnp3_ctl_prm={"-1"};
        public String dnpProtocol;
        @JSONField(name = "dnp3_al_boq_b7")
        // binary output value
        public String[] binaryOutputPointValue;
        @JSONField(name = "dnp3_al_boq.b0")
        public String[] online;
        @JSONField(name = "dnp3_al_ana_int")
        public String[] analogValue;
        @JSONField(name = "dnp3_al_point_index")
        public String[] pointIndex;
        @JSONField(name = "dnp3_al_range_start")
        public String[] startIndex;
        @JSONField(name = "dnp3_al_range_stop")
        public String[] stopIndex;
        @JSONField(name = "dnp3_al_obj")
        public String[] objAndVars;
        @JSONField(name = "dnp3_al_biq_b7")
        public String[] binaryInputPointValue;
    }
}
