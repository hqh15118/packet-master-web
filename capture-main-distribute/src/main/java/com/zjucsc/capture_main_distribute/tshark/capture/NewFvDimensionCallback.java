package com.zjucsc.capture_main_distribute.tshark.capture;

import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface NewFvDimensionCallback {
    void newCome(FvDimensionLayer layer);
}
