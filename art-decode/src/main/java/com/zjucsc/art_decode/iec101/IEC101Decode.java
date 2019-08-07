package com.zjucsc.art_decode.iec101;

import com.zjucsc.art_decode.artconfig.IEC101Config;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public class IEC101Decode extends BaseArtDecode<IEC101Config> {
    @Override
    public Map<String, Float> decode(IEC101Config iec101Config, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer layer, Object... obj) {
        IEC101DecodeMain.iec101DecodeProgram(payload,globalMap,iec101Config,layer);
        return globalMap;
    }

    @Override
    public String protocol() {
        return "iec101";
    }

}
