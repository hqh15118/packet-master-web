package com.zjucsc.application.system.service;

import com.zjucsc.application.tshark.capture.AbstractPacketService;
import com.zjucsc.application.tshark.capture.PacketMain;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import com.zjucsc.application.tshark.decode.PipeLine;
import com.zjucsc.application.tshark.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-30 - 22:28
 */
public class TsharkMainService extends AbstractPacketService {


    @Override
    public PipeLine initPipeLine() {
        return PacketMain.getDefaultPipeLine();
    }
}
