package com.zjucsc.tshark.pre_processor2;

import com.zjucsc.tshark.packets.PacketDetail;
import org.junit.Test;

public class TsharkProxyTest {

    @Test
    public void start() throws InterruptedException {
        TsharkProxy tsharkProxy = new TsharkProxy();
        tsharkProxy.setTsharkPreProcessorInfos(
                "28:D2:44:5F:69:E1",
                "en0",
                "E:\\wireshark\\tshark",
                10000,
                new TsharkListener() {
                    @Override
                    public void success(String tsharkCommand, Process process) {
                        System.out.println("success run tshark command + " + tsharkCommand);
                    }

                    @Override
                    public void error(String msg) {
                        System.out.println("run error " + msg);
                    }
                },
                msg -> System.out.println("decode error " + msg)
        );
        tsharkProxy.registerFvDimensionCallback(data -> System.out.println(data.getLayers().getFrame().getFrame_frame_protocols()));
        tsharkProxy.start();
        Thread.sleep(100000);
    }
}