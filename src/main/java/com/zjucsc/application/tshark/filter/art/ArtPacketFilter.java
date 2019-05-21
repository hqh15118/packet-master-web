package com.zjucsc.application.tshark.filter.art;

import java.util.Map;

public interface ArtPacketFilter {

    Map<String,Double> decode(byte[] payload);
}
