package com.zjucsc.tshark.pre_processor;

import com.zjucsc.tshark.handler.PipeLine;

public interface TsharkPreProcessor {
    void startCapture(String tsharkPath , String macAddress, String interfaceName, PipeLine pipeLine,
                      int type , int limit);
    void stopCapture();
    void restartCapture();
}
