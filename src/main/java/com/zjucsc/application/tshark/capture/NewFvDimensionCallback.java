package com.zjucsc.application.tshark.capture;

import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface NewFvDimensionCallback {
    void newCome(FvDimensionLayer layer);
}
