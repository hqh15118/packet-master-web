package com.zjucsc.application.system.service.common_impl;

import com.zjucsc.application.domain.non_hessian.CustomPacket;
import com.zjucsc.application.system.service.common_iservice.IPacketService;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.application.util.TsharkUtil;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class PacketServiceImpl implements IPacketService {

    @Override
    public String getPacketDetail(String rawData) throws PcapNativeException, NotOpenException, IOException {
        byte[] rawDataInByte = PacketDecodeUtil.hexStringToByteArray2(rawData);
        return TsharkUtil.analyzeRawData(rawDataInByte);
    }

}
