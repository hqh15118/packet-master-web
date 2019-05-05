package com.zjucsc.application.pcap4j;

import com.zjucsc.application.tshark.capture.PacketMain;
import com.zjucsc.application.tshark.domain.beans.PacketInfo;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacket;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.util.PacketDecodeUtil;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.zjucsc.application.config.PACKET_PROTOCOL.*;

@Component
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
        Thread taskAndPacketDetectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;){
                    try {
                        Thread.sleep(100000);
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

    private  ThreadLocal<StringBuilder> stringBuilderThreadLocal0 =
            new ThreadLocal<StringBuilder>(){
                @Override
                protected StringBuilder initialValue() {
                    return new StringBuilder(100);
                }
            };

    private void decodePacket(Packet packet){
        Iterator<Packet> packetIterator = packet.iterator();
        Packet end = null;
        /*
         * 这里需要把协议封装为标准格式
         */
        String protocol = "";
        String dst_mac = "";
        String src_mac = "";
        String src_ip = "";
        String dst_ip = "";
        String src_port = "";
        String dst_port = "";
        StringBuilder sb = stringBuilderThreadLocal0.get();
        while(packetIterator.hasNext()){
            end = packetIterator.next();
            if (end == null) {
                continue;
            }
            if (end instanceof EthernetPacket){
                protocol = ETHERNET;
                dst_mac = ((EthernetPacket) end).getHeader().getDstAddr().toString();
                src_mac = ((EthernetPacket) end).getHeader().getSrcAddr().toString();
            }
            else if (end instanceof ArpPacket){
                protocol = ARP;
                //dst_mac = ((ArpPacket) end).getHeader().getDstHardwareAddr().toString();
                //src_mac = ((ArpPacket) end).getHeader().getSrcHardwareAddr().toString();
            }else if(end instanceof IpV4Packet) {
                protocol = IPV4;
                sb.delete(0,sb.length());
                src_ip = toStandardIPString(((IpV4Packet) end).getHeader().getSrcAddr().getAddress(),sb , 4);
                sb.delete(0,sb.length());
                dst_ip = toStandardIPString(((IpV4Packet) end).getHeader().getDstAddr().getAddress(),sb , 4);
            }else if(end instanceof IpV6Packet){
                protocol = IPV6;
                sb.delete(0,sb.length());
                src_ip = toStandardIPString(((IpV6Packet) end).getHeader().getSrcAddr().getAddress(),sb , 6);
                sb.delete(0,sb.length());
                dst_ip = toStandardIPString(((IpV6Packet) end).getHeader().getDstAddr().getAddress(),sb , 6);
            }else if (end instanceof UdpPacket){
                protocol = UDP;
                sb.delete(0,sb.length());
                dst_port = ((UdpPacket) end).getHeader().getDstPort().valueAsString();
                sb.delete(0,sb.length());
                src_port = ((UdpPacket) end).getHeader().getSrcPort().valueAsString();
            }else if (end instanceof DnsPacket){
                protocol = DNS;
            }else{
                //protocol = OTHER;
            }
        }
        if (end != null){
            FiveDimensionPacketWrapper fiveDimensionPacketWrapper = new FiveDimensionPacketWrapper.Builder()
                    .protocol(protocol)
                    .srcEthAndIp(sb.delete(0,sb.length()).append(src_mac).append(":").append(src_ip).toString())
                    .dstEthAndIp(sb.delete(0,sb.length()).append(dst_mac).append(":").append(dst_ip).toString())
                    .tcpPayload(end.getRawData())
                    .timeStamp(PacketDecodeUtil.decodeTimeStamp(end.getRawData(), 20))
                    .packetLength(String.valueOf(end.getRawData().length))
                    .src_Ip(src_ip)
                    .dst_Ip(dst_ip)
                    .src_port(src_port)
                    .dis_port(dst_port)
                    .fun_code("")
                    .build();
            PacketMain.pcapPipeLine.pushDataAtHead(new PacketInfo.PacketWrapper().onFiveDimensionPacketWrapper(fiveDimensionPacketWrapper));
        }
    }

    private String toStandardIPString(byte[] ipAddress , StringBuilder sb , int version){
        String append = version == 4 ? "." : ":";
        int i = 0;
        for (int len = ipAddress.length - 1; i < len; i++) {
            sb.append(Byte.toUnsignedInt(ipAddress[i])).append(append);
        }
        sb.append(Byte.toUnsignedInt(ipAddress[i]));
        return sb.toString();
    }
}
