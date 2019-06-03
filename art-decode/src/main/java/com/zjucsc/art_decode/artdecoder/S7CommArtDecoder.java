package com.zjucsc.art_decode.artdecoder;

import com.zjucsc.art_decode.artconfig.S7CommArtConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.other.AttackType;

import java.util.List;
import java.util.Map;

public class S7CommArtDecoder extends BaseArtDecode<S7CommDecode,S7CommArtConfig> {

    public S7CommArtDecoder(S7CommDecode decoder) {
        super(decoder);
    }

    @Override
    public String protocol() {
        return "s7comm";
    }

    @Override
    public List<AttackType> attackDecode(List<AttackType> globalAttackList, byte[] payload, Object... obj) {
        return null;
    }

    @Override
    public Map<String, Float> doDecode(Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return decoder.decode(globalMap, payload, obj);
    }
}
