package com.zjucsc.application.tshark.packet.layers;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.packet.FiveDimensionPacket;

import java.util.List;

public class ModbusPacket {


    /**
     * timestamp : 946739510772
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:tpkt:cotp:s7comm"],"eth_dst":["00:0c:29:49:7e:9f"],"eth_src":["00:0c:29:75:b2:38"],"ip_src":["192.168.254.134"],"ip_addr":["192.168.254.134","192.168.254.34"],"tcp_srcport":["4620"],"tcp_dstport":["102"],"s7comm_param_func":["0x00000004"],"tcp_payload":["03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20"]}
     */

    private String timestamp;
    //private LayersBean layers;

    public static String decode(String modbusJson){
//        ModbusPacket modbusPacket = JSON.parseObject(modbusJson, ModbusPacket.class);
//        LayersBean layers = modbusPacket.getLayers();
//        return JSON.toJSONString(new FiveDimensionPacket(
//                layers.getIp_addr()
//                layers.getIp().getIp_ip_dst(),
//                layers.getTcp().getTcp_tcp_srcport(),
//                layers.getTcp().getTcp_tcp_dstport(),
//                layers.getModbus().getModbus_modbus_func_code()
//        ));
        return null;
    }


}
