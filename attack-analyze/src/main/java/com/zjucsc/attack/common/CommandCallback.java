package com.zjucsc.attack.common;

import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface CommandCallback {
    void commandCallback(FvDimensionLayer layer , String command, Object...objs);
}
