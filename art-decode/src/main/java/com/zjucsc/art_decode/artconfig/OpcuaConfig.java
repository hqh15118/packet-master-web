package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

import java.util.Map;

public class OpcuaConfig extends BaseConfig {
    private Map<String,String> nameMap;

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }
}
