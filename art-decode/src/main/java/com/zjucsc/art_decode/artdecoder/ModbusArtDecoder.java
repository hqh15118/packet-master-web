package com.zjucsc.art_decode.artdecoder;

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
    public Map<String, Float> doDecode(Map<String, Float> globalMap, byte[] payload, Object... obj) {
        ModBusConfig modBusConfig = ModBusConfig.getModbusConfig();
        if (modBusConfig!=null)
            return decoder.decode_tech(globalMap,modBusConfig,payload);
        return globalMap;
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
