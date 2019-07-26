package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

public class DNP3Config extends BaseConfig {
    private int objGroup;     //对象组
    private int index;       //索引

    public void setObjGroup(int objGroup) { this.objGroup = objGroup; }

    public void setindex(int index) { this.index = index; }

    public int getObjGroup() { return objGroup; }

    public int getindex() { return index; }

}
