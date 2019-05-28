package com.zjucsc.application.tshark.filter.art;

import com.zjucsc.tshark.art.ArtPacketFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class S7ArtPacketFilter implements ArtPacketFilter {



    @Override
    public Map<String, Double> decode(byte[] payload) {
        return null;
    }
}
