package com.zjucsc.application.tshark.decode;

import com.zjucsc.application.domain.bean.BadPacket;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 23:00
 */
public interface IPacketAnalyzer {
    /**
     *
     * @param tcpPayload rawData
     * @return if bad packet detected return BadPacket Instance else return null
     * @see BadPacket
     */
    BadPacket analyze(byte[] tcpPayload);
}
