package com.zjucsc.application.tshark.capture;

public interface CapturePacketService<S,E> {
    void start(ProcessCallback<S,E> callback);
    void stop();
}
