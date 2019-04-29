package com.zjucsc.packetmasterweb;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.tshark.*;
import com.zjucsc.application.tshark.capture.PacketMain;
import com.zjucsc.application.tshark.packet.layers.ModbusPacket;
import com.zjucsc.application.tshark.packet.layers.S7Packet;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DecodeTest {
    public String modbus_json = "{\"timestamp\" : \"946695632201\", \"layers\" : {\"frame_protocols\": [\"eth:ethertype:ip:tcp:mbtcp:modbus\"],\"eth_dst\": [\"00:0c:29:9e:7a:44\"],\"eth_src\": [\"00:0c:29:75:b2:38\"],\"ip_src\": [\"192.168.254.134\"],\"ip_addr\": [\"192.168.254.134\",\"192.168.254.143\"],\"tcp_srcport\": [\"1075\"],\"tcp_dstport\": [\"502\"],\"modbus_func_code\": [\"2\"],\"tcp_payload\": [\"d9:04:00:00:00:06:01:02:00:00:00:0b\"]}}";
    public String s7_json = "{\"timestamp\" : \"946695632244\", \"layers\" : {\"frame_protocols\": [\"eth:ethertype:ip:tcp:tpkt:cotp:s7comm\"],\"eth_dst\": [\"00:0c:29:49:7e:9f\"],\"eth_src\": [\"00:0c:29:75:b2:38\"],\"ip_src\": [\"192.168.254.134\"],\"ip_addr\": [\"192.168.254.134\",\"192.168.254.34\"],\"tcp_srcport\": [\"1073\"],\"tcp_dstport\": [\"102\"],\"s7comm_param_func\": [\"0x00000004\"],\"tcp_payload\": [\"03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20\"]}}";
    public String coreCommand = "-l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -T fields -c 1 -r";

    @Test
    public void split_string(){
        String command = "/Applications/Wireshark.app/Contents/MacOS/tshark -l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -T ek -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap" ;
        Process process = PacketMain.runTargetCommand(command);
        System.out.println(System.currentTimeMillis());
        try (InputStream is = process.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            for (; ; ) {
                String str = "";
                if ((str = reader.readLine()) != null) {
                    if (str.length() > 100)
                    {

                    }
                } else {
                    break;
                }
            }
            System.out.println("quit");
            System.out.println(System.currentTimeMillis());
        } catch (RuntimeException | IOException e) {
            throw new OpenCaptureServiceException("can not get pipe input of tshark");
        } finally {
            process.destroy();
            System.out.println("process exit value : {} " +  process.exitValue());
        }
    }


    @Test
    public void json_Test(){
        ModbusPacket packet = JSON.parseObject(modbus_json,ModbusPacket.class );
        System.out.println(packet.layers.modbus_func_code[0]);
        S7Packet packet1 = JSON.parseObject(s7_json,S7Packet.class );
        System.out.println(packet1.layers.s7comm_param_func[0]);
    }

    @Test
    public void base_json_test(){
        BasePacket packet = JSON.parseObject(modbus_json,BasePacket.class );
        System.out.println(packet.layers.frame_protocols[0]);
    }




}
