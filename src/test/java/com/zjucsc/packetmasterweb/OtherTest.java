package com.zjucsc.packetmasterweb;

import com.zjucsc.application.tshark.BasePacketHandler;
import com.zjucsc.application.tshark.PacketDecodeHandler;
import com.zjucsc.application.tshark.PacketSendHandler;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 23:48
 */
public class OtherTest {

    @Test
    public void pipeLineBannerTest(){
        DefaultPipeLine pipeLine = new DefaultPipeLine();
        pipeLine.addLast(new BasePacketHandler(Executors.newSingleThreadExecutor()));
        pipeLine.addLast(new PacketDecodeHandler(Executors.newFixedThreadPool(10)));
        PacketSendHandler handler = new PacketSendHandler(Executors.newSingleThreadExecutor());
        pipeLine.addLast(handler);
        DefaultPipeLine pipeLine1 = new DefaultPipeLine();
        handler.addPipeLine(pipeLine1);
        pipeLine1.addLast(new BasePacketHandler(Executors.newSingleThreadExecutor()).setId("base packet handler"));
        pipeLine1.addLast(new PacketDecodeHandler(Executors.newFixedThreadPool(10)));
        pipeLine1.addLast(new PacketSendHandler(Executors.newSingleThreadExecutor()));
        System.out.println(pipeLine);
    }
}
