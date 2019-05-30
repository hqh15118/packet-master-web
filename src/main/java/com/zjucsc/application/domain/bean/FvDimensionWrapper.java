package com.zjucsc.application.domain.bean;

import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.Builder;
import lombok.Data;

//五元组的包裹器
@Data
@Builder
public class FvDimensionWrapper {
    private FvDimensionLayer layer;
    private int collectorId;
    private int delay = -1;
}
