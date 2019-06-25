package com.zjucsc.application.tshark.capture;

public interface ProcessCallback<S,E> {
    void error(Exception e);
    void start(S start);
    void end(E end);
}
