package com.zjucsc.tshark.pre_processor2;

public interface TsharkListener {
    void success(String tsharkCommand,Process process);
    void error(String msg);
}
