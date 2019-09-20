package com.zjucsc.tshark.pre_processor2;

import com.alibaba.fastjson.JSON;
import com.zjucsc.common.common_util.ExceptionSafeRunnable;
import com.zjucsc.tshark.packets.PacketDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class TsharkProxy {
    private TsharkPreProcessor tsharkPreProcessor = new TsharkPreProcessor();
    static final String DEFAULT_TSHARK_PATH = "tshark";
    private LinkedBlockingQueue<String> FVDIMENSIONLAYER_JSON_BUFFER;
    private NewDataCallback<PacketDetail> newDataCallback;
    private static Logger logger = LoggerFactory.getLogger(TsharkProxy.class);
    public void setTsharkPreProcessorInfos(String macAddress,
                                            String interfaceName,
                                            String tsharkPath,
                                            int jsonDataBufferedSize,
                                            TsharkListener tsharkListener,
                                            DecodeErrorCallback decodeErrorCallback
                                            ){
        tsharkPreProcessor.setCaptureInterface(macAddress, interfaceName);
        tsharkPreProcessor.setTsharkPath(tsharkPath == null ? DEFAULT_TSHARK_PATH : tsharkPath);
        tsharkPreProcessor.registerPacketCallback(tsharkListener, (NewDataCallback<String>) data -> {
            if (!FVDIMENSIONLAYER_JSON_BUFFER.offer(data))
            {
                decodeErrorCallback.error("[FVDIMENSIONLAYER_JSON_BUFFER] over flow");
            }
        });
        FVDIMENSIONLAYER_JSON_BUFFER = new LinkedBlockingQueue<>(jsonDataBufferedSize);
    }

    public void start(){
        tsharkPreProcessor.startTshark();
        Thread processThread = new Thread(new ExceptionSafeRunnable<Object>() {
            @Override
            public void run(Object o) {
                for (;;)
                {
                    try {
                        String data = FVDIMENSIONLAYER_JSON_BUFFER.take();
                        PacketDetail packetDetail = JSON.parseObject(data,PacketDetail.class);
                        packetDetail.setRawJsonData(data);
                        newDataCallback.callback(packetDetail);
                    } catch (InterruptedException e) {
                        logger.error("" , e);
                    }
                }
            }
        });
        processThread.setName("-tshark-main-thread-" + processThread.getThreadGroup() + "-" + processThread.getId());
        processThread.start();
    }

    public void registerFvDimensionCallback(NewDataCallback<PacketDetail> newDataCallback){
        this.newDataCallback = newDataCallback;
    }
}
