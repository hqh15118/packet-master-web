package com.zjucsc.art_decode.artdecoder;

import com.zjucsc.art_decode.artconfig.BaseConfig;
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
    public void addArtConfig(S7CommArtConfig a) {

    }

    @Override
    public void deleteArtConfig(BaseConfig baseConfig) {

    }


    @Override
    public Map<String, Float> decode(Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return decoder.decode(globalMap, payload, obj);
    }

    @Override
    public String protocol() {
        return "s7comm";
    }

    @Override
    public List<AttackType> attackDecode(List<AttackType> globalAttackList, byte[] payload, Object... obj) {
        return null;
    }
}
