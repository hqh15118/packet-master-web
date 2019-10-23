package com.zjucsc.art_decode.modbus;


import com.zjucsc.art_decode.artconfig.ModBusConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.s7comm.S7Decode;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.common.util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ModbusDecode extends BaseArtDecode<ModBusConfig> {

    private Map<String, ModbusDecode.ModbusInner> innerMap = new ConcurrentHashMap<>();

    private class ModbusInner{
        private Map<Integer,byte[]> payload_map =new HashMap<>();

        {
            payload_map.put(1,new byte[]{0x00,0x00});
            payload_map.put(2,new byte[]{0x00,0x00});
            payload_map.put(3,new byte[]{0x00,0x00});
            payload_map.put(4,new byte[]{0x00,0x00});
            payload_map.put(23,new byte[]{0x00,0x00});
        }

        private Map<Integer,byte[]> Input_reg_map = new HashMap<>();

        private Map<Integer,byte[]> Holding_reg_map = new HashMap<>();

        private Map<Integer,Boolean> Coil_map = new HashMap<>();

        private Map<Integer,Boolean> Dis_input_map = new HashMap<>();
    }

    @Override
    public Map<String, Float> decode(ModBusConfig modBusConfig, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer layer, Object... obj) {
        return decode_tech(modBusConfig,globalMap,payload,layer);
    }

    public String protocol() {
        return "modbus";
    }

    private void renewmap(ModbusInner modbusInner , byte[] payload){
        int i = (int)payload[7];
        if(i==1 || i==2)
        {
            if(modbusInner.payload_map.get(i)!=null
                    && (payload[0]==modbusInner.payload_map.get(i)[0])
                    && (payload[1]==modbusInner.payload_map.get(i)[1]))
            {
                int addr_head = (int) ByteUtil.bytesToShort(modbusInner.payload_map.get(i),8);
                int addr_len = (int)ByteUtil.bytesToShort(modbusInner.payload_map.get(i),10);
                int a=(int)payload[8];
                for(int s=0;s<a;s++) {
                    for (int j = 0; (j < 8) && ((j+8*s )<addr_len); j++) {
                        if(i==1)
                        {modbusInner.Coil_map.put((addr_head + j + 8*s), !((payload[9+s] & (1<<j))==0));}
                        else
                        {modbusInner.Dis_input_map.put((addr_head + j + 8*s), !((payload[9+s] & (1<<j))==0));}
                    }
                }
            }
            else if(payload.length==12)
            {
                modbusInner.payload_map.put(i, payload);
            }
        }
        else if(i==3 || i==4 || i==23)
        {
            if(modbusInner.payload_map.get(i)!=null
                    && (payload[0]==modbusInner.payload_map.get(i)[0])
                    && (payload[1]==modbusInner.payload_map.get(i)[1]))
            {
                int addr_head =(int)ByteUtil.bytesToShort(modbusInner.payload_map.get(i),8);
                int addr_len = (int)ByteUtil.bytesToShort(modbusInner.payload_map.get(i),10);
                for(int j=0;j<addr_len;j++)
                {
                    byte[] Reg_valve = Bytecut.Bytecut(payload, 9+2*j,2);
                    if(i==3 || i==23)
                    {modbusInner.Holding_reg_map.put((addr_head + j ) , Reg_valve);}
                    else
                    {modbusInner.Input_reg_map.put((addr_head + j ) , Reg_valve);}
                }
            }
            else if(payload.length>=12)
            {
                modbusInner.payload_map.put(i, payload);
            }
        }
    }

    private Map<String ,Float> decode_tech(ModBusConfig modbus, Map<String, Float> tech_map, byte[] payload,FvDimensionLayer layer){
        ModbusInner modbusInner = innerMap.computeIfAbsent(modbus.getDeviceMac(),
                s -> new ModbusInner());
        renewmap(modbusInner,payload);
        doDecode(modbusInner,tech_map,modbus,layer);
        return tech_map;
    }

    private void doDecode(ModbusInner modbusInner , Map<String, Float> tech_map, ModBusConfig modbus, FvDimensionLayer layer){
        if(modbus.getLength() ==4) {
            byte[] byte1 = null;
            byte[] byte2 = null;
            if (modbus.getReg_coil() == 3) {
                byte1 = modbusInner.Holding_reg_map.get(modbus.getAddr_head());
                byte2 = modbusInner.Holding_reg_map.get(modbus.getAddr_head() + 1);
            }
            else if(modbus.getReg_coil()==4 )
            {
                byte1 = modbusInner.Input_reg_map.get(modbus.getAddr_head());
                byte2 = modbusInner.Input_reg_map.get(modbus.getAddr_head() + 1);
            }
            if(byte1!=null && byte2!=null)
            {
                byte[] byte_all=new byte[]{byte1[0], byte1[1], byte2[0], byte2[1]};
                if(modbus.getType().equals( "float" )){
                    float tech_value = Bytecut.BytesTofloat( byte_all,0);
                    tech_map.put(modbus.getTag(), tech_value);
                    callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
                }
                else if(modbus.getType().equals("int")){
                    float tech_value =ByteUtil.bytesToInt(byte_all,0)*modbus.getRange()[1]/(2^31-1);
                    tech_map.put(modbus.getTag(),tech_value);
                    callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
                }
            }
        }
        else if(modbus.getLength() == 2)
        {
            if(modbus.getType().equals("short"))
            {
                byte[] byte1 =null;
                if (modbus.getReg_coil() == 3) {    /////////读保持寄存器
                    byte1 = modbusInner.Holding_reg_map.get(modbus.getAddr_head());
                }
                else if(modbus.getReg_coil()==4)//////////读输入寄存器
                {
                    byte1 = modbusInner.Input_reg_map.get(modbus.getAddr_head());
                }
                float tech_value = ByteUtil.bytesToShort(byte1,0)*modbus.getRange()[1]/27686;////西门子规定最大到27686
                tech_map.put(modbus.getTag(),tech_value);
                callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
            }
        }
        else if(modbus.getType().equals("bool"))
        {
            if(modbus.getReg_coil()==1)//////////读输入离散量
            {
                if(modbusInner.Dis_input_map.get(modbus.getAddr_head()))
                {
                    tech_map.put(modbus.getTag(),1f);
                }
                else
                {
                    tech_map.put(modbus.getTag(),0f);
                }
                callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
            }
            else if(modbus.getReg_coil()==2)///////////读线圈
            {
                if(modbusInner.Coil_map.get(modbus.getAddr_head()))
                {
                    tech_map.put(modbus.getTag(),1f);
                }
                else
                {
                    tech_map.put(modbus.getTag(),0f);
                }
                callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
            }
            else if(modbus.getReg_coil()==3)
            {
                if(modbusInner.Holding_reg_map.get(modbus.getAddr_head())!=null) {
                    if (modbus.getBitoffset() < 8) {
                        if (((int) modbusInner.Holding_reg_map.get(modbus.getAddr_head())[1] & 1 << modbus.getBitoffset()) == 0) {
                            tech_map.put(modbus.getTag(), 0f);
                        } else {
                            tech_map.put(modbus.getTag(), 1f);
                        }
                    } else if (modbus.getBitoffset() >= 8 && modbus.getBitoffset() < 16) {
                        if (((int) modbusInner.Holding_reg_map.get(modbus.getAddr_head())[0] & 1 << (modbus.getBitoffset() - 8)) == 0) {
                            tech_map.put(modbus.getTag(), 0f);
                        } else {
                            tech_map.put(modbus.getTag(), 1f);
                        }
                    }
                    callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
                }
            }
            else if(modbus.getReg_coil()==4)
            {
                if(modbus.getBitoffset()<8)
                {
                    if(((int)modbusInner.Input_reg_map.get(modbus.getAddr_head())[0] & 1<<modbus.getBitoffset()) == 0)
                    {
                        tech_map.put(modbus.getTag(),0f);
                    }
                    else
                    {
                        tech_map.put(modbus.getTag(),1f);
                    }
                    callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
                }
                else if(modbus.getBitoffset()>=8 && modbus.getBitoffset()<16)
                {
                    if(((int)modbusInner.Input_reg_map.get(modbus.getAddr_head())[1] & 1<<(modbus.getBitoffset()-8)) == 0)
                    {
                        tech_map.put(modbus.getTag(),0f);
                    }
                    else
                    {
                        tech_map.put(modbus.getTag(),1f);
                    }
                    callback(modbus.getTag(),tech_map.get(modbus.getTag()),layer);
                }
            }
        }
    }

}
