package com.zjucsc.application.system.service.common_iservice;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import java.io.IOException;

public interface IPacketService {

    String getPacketDetail(String rawData) throws PcapNativeException, NotOpenException, IOException;
}
