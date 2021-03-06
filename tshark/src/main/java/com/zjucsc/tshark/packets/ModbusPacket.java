package com.zjucsc.tshark.packets;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 22:37
 */
public class ModbusPacket {

    /**
     * timestamp : 1557129889029
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:mbtcp:modbus"],"eth_dst":["00:0c:29:9e:7a:44"],"eth_src":["00:0c:29:75:b2:38"],"frame_cap_len":["90"],"ip_dst":["192.168.254.143"],"ip_src":["192.168.254.134"],"tcp_srcport":["1075"],"tcp_dstport":["502"],"modbus_func_code":["2"]}
     */

    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        public String[] modbus_func_code;
    }

}
