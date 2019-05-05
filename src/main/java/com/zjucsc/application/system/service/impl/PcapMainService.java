package com.zjucsc.application.system.service.impl;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 21:58
 */

import com.zjucsc.application.pcap4j.PacketListenHandler;
import com.zjucsc.application.tshark.capture.AbstractPacketService;
import com.zjucsc.application.tshark.decode.PipeLine;
import com.zjucsc.application.util.PcapUtils;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.zjucsc.application.config.Common.COMMON_THREAD_EXCEPTION_HANDLER;

/**
 * pcap4j 抓取并分析非TCP的包
 */
@Service
@Slf4j
public class PcapMainService {

    @Autowired PacketListenHandler packetListenHandler;

    private String deviceName;
    private AbstractPacketService.ProcessCallback processCallback;
    private PipeLine pipeLine;

    public void start(String deviceName , AbstractPacketService.ProcessCallback processCallback){
        assert processCallback!=null;
        this.processCallback = processCallback;
        this.deviceName = deviceName;
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        processCallback.start();
                        PcapHandle handle = PcapUtils.openDevice(deviceName,65536,3000);
                        try {
                            handle.setFilter("not tcp" , BpfProgram.BpfCompileMode.OPTIMIZE);
                        } catch (PcapNativeException | NotOpenException e) {
                            log.error("exception when set pcap handle filter " , e);
                            processCallback.error(e);
                            return;
                        }
                        PcapUtils.endlessLoopHandler(handle,packetListenHandler);
                    }
                }
        );
        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
        thread.setName("-pcap-capture-thread");
        thread.start();
    }

    public void stop(){
        PcapUtils.closeDevice(deviceName);
        processCallback.end();
    }
}
