package com.zjucsc.tshark;

import com.zjucsc.tshark.packets.FvDimensionLayer;

public interface Entry {

    boolean append(FvDimensionLayer layer);
}
