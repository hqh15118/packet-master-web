package com.zjucsc.tshark.art;

import java.util.Map;

public interface ArtPacketFilter {

    Map<String,Double> decode(byte[] payload);
}
