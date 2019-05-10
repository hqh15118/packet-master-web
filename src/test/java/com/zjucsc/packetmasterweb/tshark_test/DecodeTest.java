package com.zjucsc.packetmasterweb.tshark_test;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.tshark.capture.PacketMain;
import com.zjucsc.application.tshark.domain.packet.InitPacket;
import com.zjucsc.application.tshark.domain.packet.layers.ModbusPacket;
import com.zjucsc.application.tshark.domain.packet.layers.S7Packet;
import com.zjucsc.application.util.ByteUtils;
import com.zjucsc.application.util.PacketDecodeUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class DecodeTest {
    public String modbus_json = "{\"timestamp\" : \"946695632201\", \"layers\" : {\"frame_protocols\": [\"eth:ethertype:ip:tcp:mbtcp:modbus\"],\"eth_dst\": [\"00:0c:29:9e:7a:44\"],\"eth_src\": [\"00:0c:29:75:b2:38\"],\"ip_src\": [\"192.168.254.134\"],\"ip_addr\": [\"192.168.254.134\",\"192.168.254.143\"],\"tcp_srcport\": [\"1075\"],\"tcp_dstport\": [\"502\"],\"modbus_func_code\": [\"2\"],\"tcp_payload\": [\"d9:04:00:00:00:06:01:02:00:00:00:0b\"]}}";
    public String s7_json = "{\"timestamp\" : \"946695632244\", \"layers\" : {\"frame_protocols\": [\"eth:ethertype:ip:tcp:tpkt:cotp:s7comm\"],\"eth_dst\": [\"00:0c:29:49:7e:9f\"],\"eth_src\": [\"00:0c:29:75:b2:38\"],\"ip_src\": [\"192.168.254.134\"],\"ip_addr\": [\"192.168.254.134\",\"192.168.254.34\"],\"tcp_srcport\": [\"1073\"],\"tcp_dstport\": [\"102\"],\"s7comm_param_func\": [\"0x00000004\"],\"tcp_payload\": [\"03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20\"]}}";
    public String coreCommand = "-l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -T fields -c 1 -r";

    @Test
    public void split_string(){
        String command = "/Applications/Wireshark.app/Contents/MacOS/tshark -l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -e tcp.payload -T ek -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap4j/question_1531953261_01.pcap4j" ;
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
        InitPacket packet = JSON.parseObject(modbus_json, InitPacket.class );
        System.out.println(packet.layers.frame_protocols[0]);
    }

    //1 年 3 天 5 时 3 分 15 秒 30 毫秒 7 微秒 1400 纳秒
    private String hexString = "080CA19E0F00FC00";
    //17 年 2 天 7 时 5 分 12 秒 26 毫秒 15 微秒 600 纳秒
   private String hexString1 = "8808E2980D01EC00";


    //40000 -> 47 ms
    @Test
    public void timeStampTest() throws InterruptedException {
        byte[] bytes = ByteUtils.hexStringToByteArray(hexString);
        byte[] bytes1 = ByteUtils.hexStringToByteArray(hexString1);
        long time1 = System.currentTimeMillis();
        int byteLength = bytes.length;
        for (int i = 0; i < 400000; i++) {
            byte[] bytes_1 = ByteUtils.hexStringToByteArray(hexString);
            byte[] bytes1_1 = ByteUtils.hexStringToByteArray(hexString1);
            PacketDecodeUtil.decodeTimeStamp(bytes_1 , byteLength);
            PacketDecodeUtil.decodeTimeStamp(bytes1_1 , byteLength);
        }
        System.out.println(System.currentTimeMillis() - time1);
    }

}
