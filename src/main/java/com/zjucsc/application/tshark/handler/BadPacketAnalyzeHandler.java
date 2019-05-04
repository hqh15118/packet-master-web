package com.zjucsc.application.tshark.handler;

import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.util.AbstractAnalyzer;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static com.zjucsc.application.config.Common.BAD_PACKET_FILTER_PRO_1;
import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;
import static com.zjucsc.application.config.PACKET_PROTOCOL.OTHER;

@Slf4j
public class BadPacketAnalyzeHandler extends AbstractAsyncHandler<Void> {

    private Executor sendBadPacketThreadPool = Executors.newSingleThreadExecutor(
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("-BadPacketAnalyzeHandler-");
                    return thread;
                }
            }
    );

    public BadPacketAnalyzeHandler(ExecutorService executor) {
        super(executor);
    }

    @Override
    public Void handle(Object t) {
        FiveDimensionPacketWrapper packet = ((FiveDimensionPacketWrapper) t);
        //System.out.println("bad packet handler : " + packet);
        operationAnalyze(packet.fiveDimensionPacket.code,packet);
        protocolAnalyze(packet);
        artifactAnalyze(packet.tcpPayload , packet);
        return null;
    }

    private void artifactAnalyze(byte[] tcpPayload, FiveDimensionPacketWrapper packet) {

    }

    private void protocolAnalyze(FiveDimensionPacketWrapper packetWrapper) {
        /*
         * 调用五元组分析器进行解析
         */
        BadPacket badPacket = ((BadPacket) BAD_PACKET_FILTER_PRO_1.get(FV_DIMENSION).analyze(packetWrapper));
        if (badPacket!=null){
            sendBadPacket(badPacket);
        }
    }

    private void sendBadPacket(BadPacket badPacket) {

        sendBadPacketThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                //System.out.println("send bad packet : " + badPacket);
                //System.out.println("**********************");
                SocketServiceCenter.updateAllClient(SocketIoEvent.BAD_PACKET,badPacket);
            }
        });
    }


    private void operationAnalyze(String funCode , FiveDimensionPacketWrapper packetWrapper){
        //"0x00000004" "4"   -->  fun_code of int
        int fun_code = PacketDecodeUtil.decodeFuncode(funCode
                ,packetWrapper.fiveDimensionPacket.protocol);
        /*
         * 根据解析得到的协议，调用相应的分析器进行解析
         */
        AbstractAnalyzer<?> analyzer = BAD_PACKET_FILTER_PRO_1.get(packetWrapper.fiveDimensionPacket.protocol);
        BadPacket badPacket = null;
        if (analyzer != null){
            badPacket = ((BadPacket) analyzer.analyze(fun_code , packetWrapper));
        }else{
            BAD_PACKET_FILTER_PRO_1.get(OTHER).analyze(packetWrapper.fiveDimensionPacket.protocol);
        }


        if (badPacket!=null){
            sendBadPacket(badPacket);
        }
    }
}
