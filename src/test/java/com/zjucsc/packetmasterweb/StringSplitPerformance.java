package com.zjucsc.packetmasterweb;

import com.google.common.base.Splitter;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.tshark.BasePacketHandler;
import com.zjucsc.application.tshark.PacketDecodeHandler;
import com.zjucsc.application.tshark.PacketMain;
import com.zjucsc.application.tshark.PacketSendHandler;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class StringSplitPerformance {

    @Test
    public void split_string(){
        String command = "C:\\Users\\Administrator\\Desktop\\tshark_min\\tshark.exe -l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_cod e -e tcp.payload -T fields -c 1 -r C:\\Users\\Administrator\\Desktop\\pcap_files\\question_1531953285_02.pcap";
        Process process = PacketMain.runTargetCommand(command);
        String json = "eth:ethertype:ip:tcp:tpkt:cotp:s7comm 00:0c:29:49:7e:9f 00:0c:29:75:b2:38 192.168.254.134 192.168.254.134,192.168.254.34 4620 102 0x00000004  03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20";
        Splitter splitter = Splitter.on(" ");
        System.out.println(splitter.split(json));
    }
}
