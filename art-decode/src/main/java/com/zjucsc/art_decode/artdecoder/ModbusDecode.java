package com.zjucsc.art_decode.artdecoder;

import com.zjucsc.art_decode.artconfig.ModBusConfig;
import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.common_util.Bytecut;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class ModbusDecode {

    public  static Map<Integer,byte[]> payload_map =new HashMap<>();

    static
    {
        payload_map.put(1,new byte[]{0x00,0x00});
        payload_map.put(2,new byte[]{0x00,0x00});
        payload_map.put(3,new byte[]{0x00,0x00});
        payload_map.put(4,new byte[]{0x00,0x00});
    }

    private Map<Integer,byte[]> Input_reg_map = new HashMap<>();

    private Map<Integer,byte[]> Holding_reg_map = new HashMap<>();

    private Map<Integer,Boolean> Coil_map = new HashMap<>();

    private Map<Integer,Boolean> Dis_input_map = new HashMap<>();

    private Map<String,Map> map= new HashMap<>();

    private Set<ModBusConfig> modbustechconfig = new ConcurrentSkipListSet<>();

    public  void renewconfig(ModBusConfig techconfig)
    {
        if(techconfig!=null)
        {
            modbustechconfig.remove(techconfig);
            modbustechconfig.add(techconfig);
        }
    }

    public void deleteConfig(ModBusConfig config){
        modbustechconfig.remove(config);
    }

    private Map<String,Map> decode( byte[] bytes) {
        renewmap(bytes);
        map.put("离散量输入",Dis_input_map);
        map.put("线圈",Coil_map);
        map.put("保持寄存器",Holding_reg_map);
        map.put("输出寄存器",Input_reg_map);
        return map;
    }

    public String protocol() {
        return "modbus";
    }

    public void renewmap(byte[] payload ){
        int i = (int)payload[7];
        if(i==1 || i==2)
        {
            if((payload[0]==payload_map.get(i)[0]) && (payload[1]==payload_map.get(i)[1]))
            {
                int addr_head = (int) ByteUtil.bytesToShort(payload_map.get(i),8);
                int addr_len = (int)ByteUtil.bytesToShort(payload_map.get(i),10);
                int a=(int)payload[8];
                for(int s=0;s<a;s++) {
                    for (int j = 0; (j < 8) && ((j+8*s )<addr_len); j++) {
                        if(i==1)
                        {Coil_map.put((addr_head + j + 8*s), !((payload[9+s] & (1<<j))==0));}
                        else
                        {Dis_input_map.put((addr_head + j + 8*s), !((payload[9+s] & (1<<j))==0));}
                    }
                }
            }
            else if(payload.length==12)
            {
                payload_map.put(i, payload);
            }
        }
        else if(i==3 || i==4)
        {
            if((payload[0]==payload_map.get(i)[0]) && (payload[1]==payload_map.get(i)[1]))
            {
                int addr_head =(int)ByteUtil.bytesToShort(payload_map.get(i),8);
                int addr_len = (int)ByteUtil.bytesToShort(payload_map.get(i),10);
                for(int j=0;j<addr_len;j++)
                {
                    byte[] Reg_valve = Bytecut.Bytecut(payload, 9+2*j,2);
                    if(i==3)
                    {Holding_reg_map.put((addr_head + j ) , Reg_valve);}
                    else
                    {Input_reg_map.put((addr_head + j ) , Reg_valve);}
                }
            }
            else if(payload.length==12)
            {
                payload_map.put(i, payload);
            }
        }
    }

    public Map<String ,Float> decode_tech(Map<String , Float> tech_map,byte[] payload){
        if (modbustechconfig.size() == 0){
            return tech_map;
        }
        renewmap(payload);
        for (ModBusConfig modBusConfig : modbustechconfig) {
            doDecode(tech_map, modBusConfig);
        }
        return tech_map;
    }

    private void doDecode(Map<String , Float> tech_map,ModBusConfig modbus){
        if(modbus.getLength() ==4) {
            byte[] byte1 = null;
            byte[] byte2 = null;
            if (modbus.getReg_coil() == 3) {
                byte1 = Holding_reg_map.get(modbus.getAddr_head());
                byte2 = Holding_reg_map.get(modbus.getAddr_head() + 1);
            }
            else if(modbus.getReg_coil()==4)
            {
                byte1 = Input_reg_map.get(modbus.getAddr_head());
                byte2 = Input_reg_map.get(modbus.getAddr_head() + 1);
            }
            if(byte1!=null && byte2!=null)
            {
                byte[] byte_all=new byte[]{byte1[0], byte1[1], byte2[0], byte2[1]};
                if(modbus.getType().equals( "float" )){
                    float tech_value = Bytecut.BytesTofloat( byte_all,0);
                    tech_map.put(modbus.getArtName(), tech_value);
                }
                else if(modbus.getType().equals("int")){
                    float tech_value = modbus.getRange()[0] + ByteUtil.bytesToInt(byte_all,0)*(modbus.getRange()[1]-modbus.getRange()[0])/(2^32);
                    tech_map.put(modbus.getArtName(),tech_value);
                }
            }
        }
        else if(modbus.getLength() == 2)
        {
            if(modbus.getType().equals("short"))
            {
                byte[] byte1 =null;
                if (modbus.getReg_coil() == 3) {    /////////读保持寄存器
                    byte1 = Holding_reg_map.get(modbus.getAddr_head());
                }
                else if(modbus.getReg_coil()==4)//////////读输入寄存器
                {
                    byte1 = Input_reg_map.get(modbus.getAddr_head());
                }
                float tech_value = modbus.getRange()[0] + ByteUtil.bytesToShort(byte1,0)*(modbus.getRange()[1]-modbus.getRange()[0])/(2^16);
                tech_map.put(modbus.getArtName(),tech_value);
            }
        }
        else if(modbus.getType().equals("bool"))
        {
            if(modbus.getReg_coil()==1)//////////读输入离散量
            {
                if(Dis_input_map.get(modbus.getAddr_head()))
                {
                    tech_map.put(modbus.getArtName(),1f);
                }
                else
                {
                    tech_map.put(modbus.getArtName(),0f);
                }
            }
            else if(modbus.getReg_coil()==2)///////////读线圈
            {
                if(Coil_map.get(modbus.getAddr_head()))
                {
                    tech_map.put(modbus.getArtName(),1f);
                }
                else
                {
                    tech_map.put(modbus.getArtName(),0f);
                }
            }
        }
    }
}
