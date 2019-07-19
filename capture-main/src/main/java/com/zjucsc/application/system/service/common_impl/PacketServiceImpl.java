package com.zjucsc.application.system.service.common_impl;

import com.zjucsc.application.domain.non_hessian.CustomPacket;
import com.zjucsc.application.system.service.common_iservice.IPacketService;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.application.util.TsharkUtil;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class PacketServiceImpl implements IPacketService {
    private CustomPacket customPacket = new CustomPacket();

    private LinkedBlockingQueue<String> analyzeResultQueue = new LinkedBlockingQueue<>();
    @Override
    public String getPacketDetail(String rawData) {
        byte[] rawDataInByte = PacketDecodeUtil.hexStringToByteArray2(rawData);
        customPacket.setRawData(rawDataInByte);
        try {
            TsharkUtil.analyzeHistoryPacket(customPacket, (rawData1, content) -> {
                analyzeResultQueue.add(content);
            });
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return analyzeResultQueue.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

}
