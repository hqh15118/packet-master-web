package com.zjucsc.art_decode.artdecoder;

import com.zjucsc.art_decode.artconfig.BaseConfig;
import com.zjucsc.art_decode.artconfig.S7techpara;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.other.AttackType;

import java.util.List;
import java.util.Map;

public class S7CommArtDecoder extends BaseArtDecode<S7Decode, S7techpara> {

    public S7CommArtDecoder(S7Decode decoder) {
        super(decoder);
    }

    @Override
    public void addArtConfig(S7techpara a) {
        this.decoder.renewconfig(a);
    }

    @Override
    public void updateArtConfig(S7techpara a) {

    }

    @Override
    public void deleteArtConfig(BaseConfig baseConfig) {
        this.decoder.deleteConfig((S7techpara) baseConfig);
    }


    @Override
    public Map<String, Float> decode(Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return decoder.decode_tech(globalMap, payload, obj);
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
