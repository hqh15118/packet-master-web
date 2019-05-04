package com.zjucsc.application.tshark.domain.packet.layers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.util.PacketDecodeUtil;

public class ModbusPacket{
    /**
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:mbtcp:modbus"],"eth_dst":["00:0c:29:9e:7a:44"],"eth_src":["00:0c:29:75:b2:38"],"ip_src":["192.168.254.134"],"ip_addr":["192.168.254.134","192.168.254.143"],"tcp_srcport":["1075"],"tcp_dstport":["502"],"modbus_func_code":["2"],"tcp_payload":["d9:04:00:00:00:06:01:02:00:00:00:0b"]}
     */

    public LayersBean layers;

    public static class LayersBean {
        public String[] frame_protocols;
        public String[] eth_dst;
        public String[] eth_src;
        public String[] ip_src;
        @JSONField(name ="ip_dst")
        public String[] ip_addr;
        public String[] tcp_srcport;
        public String[] tcp_dstport;
        public String[] modbus_func_code;
        public String[] tcp_payload;
    }
}
