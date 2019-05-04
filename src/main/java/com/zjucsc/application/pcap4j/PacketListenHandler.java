package com.zjucsc.application.pcap4j;

import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PacketListenHandler implements PacketListener {

    private static Logger logger = LoggerFactory.getLogger(PacketListener.class);
    /**
     * queue store packet
     */
    private LinkedBlockingQueue<Packet> packetLinkedBlockingQueue = new LinkedBlockingQueue<>(10000);
    /**
     * 无限长队列存储packet，开启10个线程用于处理packet
     */
    private ThreadPoolExecutor executorService = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    private static long id = 0;
    private static AtomicLong tcpId = new AtomicLong(0);

    {
        //单个reactor线程从Queue中抓取报文
        Thread packetCollectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;){
                    Runnable runnable;
                    try {
                        final Packet packet = packetLinkedBlockingQueue.poll(5, TimeUnit.SECONDS);
                        /*
                         * 超时的packet为null
                         */
                        if (packet!=null){
                            /*
                             * 将获取到的报文封装成任务放入队列中待处理
                             */
                            runnable = () -> decodePacket(packet);
                            executorService.execute(runnable);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        /*
         * 探测线程，每隔一段时间
         */
        Thread  taskAndPacketDetectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("packet queue size : {} , worker queue packet size : {}" +
                            ";;has received {} packets  ; has receive {} tcp packet"  , packetLinkedBlockingQueue.size() , executorService.getQueue().size(),
                            id , tcpId.longValue());
                }
            }
        });

        packetCollectThread.start();
        taskAndPacketDetectThread.start();
    }

    @Override
    public void gotPacket(Packet packet) {
        /*
         * 这里的packet不会是null
         */
        try {
            packetLinkedBlockingQueue.put(packet);
            /*
             * 如果是单线程，这里可以直接用long格式来统计收到的包数
             * 多个reactor线程的话，就需要原子类来进行统计了
             */
            id++;
        } catch (InterruptedException e) {
            logger.error("{} \n packet thread has been interrupted" , e);
        }
    }

    /**
     * 复用byte，不用每次调用decodePacket都需要分配24字节的内存
     */
    private static ThreadLocal<byte[]> threadLocal = ThreadLocal.withInitial(() -> new byte[24]);

    private static void decodePacket(Packet packet){
        Iterator<Packet> packetIterator = packet.iterator();
        Packet end = null;
        while(packetIterator.hasNext()){
            end = packetIterator.next();
        }
        if (end!=null) {
            System.out.println(end.getHeader());
        }else{
            System.out.println("null packet");
        }
        System.out.println("****************");
    }
}
