package com.zjucsc.application.tshark.capture;

import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;

public interface NewFvDimensionCallback {
    void newCome(FvDimensionLayer layer);
}
