package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

import java.util.Map;

public class OpcdaConfig extends BaseConfig {
    /** name_en为英文名称（工程中的名称），name_cn为中文名称（前端配置的名称） **/

    private String name_en;

    private String name_cn;

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getName_en() {
        return name_en;
    }

    public String getName_cn() {
        return name_cn;
    }

}
