package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.beans.PacketInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacketStatisticsHandler extends AbstractAsyncHandler<Void> {

    /**
     * 只允许单线程运行，防止线程不安全统计出错
     * @param executor
     */
    public PacketStatisticsHandler(ExecutorService executor) {
        super(Executors.newSingleThreadExecutor());
    }

    @Override
    public Void handle(Object t) {
        PacketInfo.PacketWrapper wrapper = ((PacketInfo.PacketWrapper) t);
        Common.addPacketFlow(Integer.parseInt(wrapper.packetLength));
        Common.addPacketNumber();
        return null;
    }
}
