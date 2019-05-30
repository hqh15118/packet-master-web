package com.zjucsc.packetmasterweb.tshark_test.tests;

import com.zjucsc.packetmasterweb.tshark_test.demos.pre_processor.ModbusPreProcessor;
import com.zjucsc.packetmasterweb.tshark_test.demos.pre_processor.S7CommPreProcessor;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.handler.DefaultPipeLine;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 20:52
 */
public class PacketDecodeTest {


    private long time1;
    private int num = 0;

    @Test
    public void S7AndModbusDecodeTest() {
        S7CommPreProcessor preProcessor = new S7CommPreProcessor();
        ModbusPreProcessor modbusPreProcessor = new ModbusPreProcessor();
        DefaultPipeLine pipeLine = new DefaultPipeLine("s7 pipe line");
        AbstractAsyncHandler<Void> sendHandler =
                new AbstractAsyncHandler<Void>(Executors.newSingleThreadExecutor(
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return new Thread(r);
                            }
                        }
                )) {
                    @Override
                    public Void handle(Object t) {
//                        FvDimensionLayer layer = null;
//                        if (t instanceof S7CommPacket){
//                            layer = ((S7CommPacket) t).layersX;
//                        }
//                        if (t instanceof ModbusPacket){
//                            layer = ((ModbusPacket) t).layers;
//                        }
                        System.out.println(((String) t));
                        return null;
                    }
                };
        pipeLine.addLast(sendHandler);
        preProcessor.setPipeLine(pipeLine);
        modbusPreProcessor.setPipeLine(pipeLine);
        time1 = System.currentTimeMillis();
        //preProcessor.execCommand();
        modbusPreProcessor.execCommand();
    }
}
