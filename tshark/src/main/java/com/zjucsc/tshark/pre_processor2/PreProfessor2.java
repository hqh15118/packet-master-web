package com.zjucsc.tshark.pre_processor2;

import java.io.IOException;

public interface PreProfessor2 {
    void registerPacketCallback(TsharkListener tsharkListener, NewDataCallback<String> newDataCallback);
    void startTshark() throws IOException;
    void stopTshark();
    void restartTshark();
    void setTsharkPath(String tsharkPath);
    void setCaptureInterface(String macAddress,String interfaceName);
}
