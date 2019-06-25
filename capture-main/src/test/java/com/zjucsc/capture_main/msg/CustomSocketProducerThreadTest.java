package com.zjucsc.capture_main.msg;

import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.junit.Test;

public class CustomSocketProducerThreadTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testProducer(){
        CustomSocketProducerThread customSocketProducerThread = new CustomSocketProducerThread();
        customSocketProducerThread.start();
        FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
        customSocketProducerThread.appendData(fvDimensionLayer);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testConsumer() throws InterruptedException {
        SocketServerThread socketServerThread = new SocketServerThread(e -> {

        });
        socketServerThread.start();
        Thread.sleep(1000000);
    }
}