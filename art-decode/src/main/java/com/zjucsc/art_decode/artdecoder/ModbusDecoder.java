package com.zjucsc.art_decode.artdecoder;

import com.zjucsc.art_decode.artconfig.ModBusConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.other.AttackType;

import java.util.List;
import java.util.Map;

public class ModbusDecoder  extends BaseArtDecode<ModBusConfig> {
    @Override
    public Map<String, Float> doDecode(Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return null;
    }

    @Override
    public String protocol() {
        return null;
    }

    @Override
    public List<AttackType> attackDecode(List<AttackType> globalAttackList, byte[] payload, Object... obj) {
        return null;
    }
}
