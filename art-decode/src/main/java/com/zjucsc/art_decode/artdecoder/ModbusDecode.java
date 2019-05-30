package com.zjucsc.art_decode.artdecoder;


import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.common_util.Bytecut;

import java.util.HashMap;
import java.util.Map;

public class ModbusDecode {

    public  static Map<Integer,byte[]> payload_map =new HashMap <>();

    static
    {
        payload_map.put(1,new byte[]{0x00,0x00});
        payload_map.put(2,new byte[]{0x00,0x00});
        payload_map.put(3,new byte[]{0x00,0x00});
        payload_map.put(4,new byte[]{0x00,0x00});
    }

    public  Map<Integer,byte[]> Reg_map = new HashMap<>();

    public  Map<Integer,Boolean> Coil_map = new HashMap<>();

    private  Map<String,Map> map= new HashMap<>();


    public Map<String,Map> decode( byte[] bytes) {
        renewmap(bytes);
        map.put("线圈值",Coil_map);
        map.put("寄存器值",Reg_map);
        return map;
    }

    public String protocol() {
        return "modbus";
    }

    private void renewmap(byte[] payload ){
        int i = (int)payload[7];
        if(i==1 || i==2)
        {
            if((payload[0]==payload_map.get(i)[0]) && (payload[1]==payload_map.get(i)[1]))
            {
                int addr_head = (int) ByteUtil.bytesToShort(payload_map.get(i),8);
                int addr_len = (int) ByteUtil.bytesToShort(payload_map.get(i),10);
                int a=(int)payload[8];
                for(int s=0;s<a;s++) {
                    for (int j = 0; (j < 8) && ((j+8*s )<addr_len); j++) {
                        Coil_map.put((addr_head + j + 8*s), !((payload[9+s] & (1<<j))==0));
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
                    Reg_map.put((addr_head + j ) , Reg_valve);
                }
            }
            else if(payload.length==12)
            {
                payload_map.put(i, payload);
            }
        }
    }
}
