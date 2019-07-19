package com.zjucsc.art_decode.dnp;//从配置接口读取所填的内容，不需要考虑多条配置内容，在外部轮询，当成单次比较即可

import com.zjucsc.art_decode.base.BaseConfig;

public class DNP3Config extends BaseConfig {
    private int objGroup;     //对象组
    private int index;       //索引

    public void setObjGroup(int objGroup) { this.objGroup = objGroup; }

    public void setindex(int index) { this.index = index; }

    public int getObjGroup() { return objGroup; }

    public int getindex() { return index; }

}
