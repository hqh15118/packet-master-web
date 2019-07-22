package com.zjucsc.art_decode.base;

import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface ValidPacketCallback {
    void callback(String argName, float value, FvDimensionLayer layer);
}
