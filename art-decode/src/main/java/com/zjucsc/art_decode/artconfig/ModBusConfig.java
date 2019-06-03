package com.zjucsc.art_decode.artconfig;

public class ModBusConfig {
    private String tech_name;
    private String type;
    private int length;
    private int addr_head;
    private int reg_coil;
    private float[] range;


    public ModBusConfig()
    { }

    public String getTech_name(){
        return tech_name;
    }

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

    public void setTech_name( String  tech_name)
    {
        this.tech_name = tech_name;
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

    public ModBusConfig(String tech_name, String type, int length, int addr_head, int reg_coil, float[] range) {
        this.tech_name = tech_name;
        this.type = type;
        this.length = length;
        this.addr_head = addr_head;
        this.reg_coil = reg_coil;
        this.range = range;
    }

    private static ModBusConfig modBusConfig = null;

    public static void setModBusConfig(String tech_name, String type, int length, int addr_head, int reg_coil, float[] range){
        modBusConfig = new ModBusConfig(tech_name,type,length,addr_head,reg_coil,range);
    }

    public static ModBusConfig getModbusConfig(){
        return modBusConfig;
    }
}
