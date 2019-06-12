package com.zjucsc.art_decode.artconfig;


import com.zjucsc.art_decode.base.BaseConfig;

public class ModBusConfig extends BaseConfig {
    /**
     *  type 类型 数据类型
     *  length 数据长度
     *  addr_head 偏移
     *  reg_coil 寄存器类型 1 2 3 4
     *  range float数组 量程最大值最小值
     */
    private String type;
    private int length;
    private int addr_head;
    private int reg_coil;
    private float[] range;

    public String getType()
    {
        return type;
    }

    public int getLength()
    {
        return length;
    }

    public int getAddr_head()
    {
        return addr_head;
    }

    public int getReg_coil()
    {
        return reg_coil;
    }

    public float[] getRange()
    {
        return range;
    }

    public void setType( String type)
    {
        this.type = type;
    }

    public void setLength(int length )
    {
        this.length = length;
    }

    public void setAddr_head(int addr_head )
    {
        this.addr_head =addr_head ;
    }

    public void setReg_coil(int reg_coil )
    {
        this.reg_coil =reg_coil ;
    }

    public void setRange(float[] range)
    {
        this.range =range ;
    }

}