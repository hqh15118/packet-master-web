package com.zjucsc.tshark.pre_processor2;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TsharkProxyTest {

    @Test
    public void start() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    startTshark(null);
                } catch (BrokenBarrierException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(100000);
    }

    private AtomicInteger packetNumebr = new AtomicInteger(0);
    private void startTshark(CyclicBarrier cyclicBarrier) throws BrokenBarrierException, InterruptedException {
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
                    public void error(String msg,Exception e) {
                        System.out.println("run error " + msg);
                    }
                },
                msg -> System.out.println("decode error " + msg)
        );
        tsharkProxy.registerFvDimensionCallback(data -> {
            packetNumebr.incrementAndGet();
        });
        tsharkProxy.start();
    }

    @Test
    public void tsharkSpeedTest() throws BrokenBarrierException, InterruptedException {
        startTshark(null);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println(packetNumebr);
        },1,1, TimeUnit.SECONDS);
        Thread.sleep(100000);
    }
}