package com.zjucsc.application.system.service.impl;

import static com.zjucsc.application.config.Common.recvPacketFlow;
import static com.zjucsc.application.config.Common.recvPacketNuber;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-30 - 22:34
 */

public class PacketAnalyzeService {



    public void addPacketNumber(){
        recvPacketNuber++;
    }

    public void addPacketFlow(int packetFlowLength){
        recvPacketFlow += packetFlowLength;
    }

    public long getRecvPacketNumber(){
        return recvPacketNuber;
    }
    public long getRecvPacketFlow(){
        return recvPacketFlow;
    }
}
