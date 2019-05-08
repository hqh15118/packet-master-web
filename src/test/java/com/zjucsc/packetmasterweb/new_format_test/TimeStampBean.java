package com.zjucsc.packetmasterweb.new_format_test;

import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-08 - 23:41
 */
public class TimeStampBean {


    /**
     * timestamp : 1557129889030
     * layers : {"eth_trailer":["00020d040000369b8027a8000000369b8027c400"],"eth_fcs":["0x00000061"]}
     */

    //public String timestamp;
    public LayersBean layers;

    public static class LayersBean {
        public String[] eth_trailer;
        public String[] eth_fcs;
    }
}
