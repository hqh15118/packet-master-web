package com.zjucsc.art_decode.artdecoder;

import com.zjucsc.art_decode.artconfig.BaseConfig;
import com.zjucsc.art_decode.artconfig.ModBusConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.other.AttackType;

import java.util.List;
import java.util.Map;

public class ModbusArtDecoder extends BaseArtDecode<ModbusDecode,ModBusConfig> {


    public ModbusArtDecoder(ModbusDecode decoder) {
        super(decoder);
    }

    @Override
    public void addArtConfig(ModBusConfig modBusConfig) {
        decoder.renewconfig(modBusConfig);
    }

    @Override
    public void updateArtConfig(ModBusConfig a) {

    }

    @Override
    public void deleteArtConfig(BaseConfig baseConfig) {
        ModBusConfig modBusConfig = (ModBusConfig)baseConfig;
        decoder.deleteConfig(modBusConfig);
    }


    @Override
    public Map<String, Float> decode(Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return decoder.decode_tech(globalMap,payload);
    }

    @Override
    public String protocol() {
        return "modbus";
    }

    @Override
    public List<AttackType> attackDecode(List<AttackType> globalAttackList, byte[] payload, Object... obj) {
        return null;
    }
}
