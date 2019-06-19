package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.tshark.TsharkCommon;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.handler.DefaultPipeLine;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import org.junit.Test;

import java.util.concurrent.Executors;

public class OpcuaPreProcessorTest {


    @Test
    public void opcuaTest() throws InterruptedException {
        BasePreProcessor preProcessor = new OpcuaPreProcessor();
        //改成网卡的MAC地址
        //String macAddress = "28:D2:44:5F:69:E1";
        //改成网卡的名字
        //String interfaceName = "en0";
        //BasePreProcessor.setCaptureDeviceNameAndMacAddress(macAddress,interfaceName);
        TsharkCommon.setErrorCallback(System.out::println);
        DefaultPipeLine pipeLine = new DefaultPipeLine("zzz");
        preProcessor.setPipeLine(pipeLine);
        pipeLine.addLast(handler);
        preProcessor.execCommand(0,-1);
        Thread.sleep(1000000000);
    }

    private static AbstractAsyncHandler<FvDimensionLayer> handler =
            new AbstractAsyncHandler<FvDimensionLayer>(Executors.newSingleThreadExecutor()) {
                @Override
                public FvDimensionLayer handle(Object o) {
                    FvDimensionLayer fvDimensionLayer = ((FvDimensionLayer) o);
                    byte[] rawData = PacketDecodeUtil.hexStringToByteArray2(fvDimensionLayer.custom_ext_raw_data[0]);
                    byte[] tcppayload = PacketDecodeUtil.hexStringToByteArray(fvDimensionLayer.tcp_payload[0]);
                    System.out.println(fvDimensionLayer);
                    return fvDimensionLayer;
                }
            };
}