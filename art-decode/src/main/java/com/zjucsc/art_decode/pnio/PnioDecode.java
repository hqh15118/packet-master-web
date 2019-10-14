package com.zjucsc.art_decode.pnio;

import com.zjucsc.art_decode.artconfig.PnioConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.common.util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Arrays;
import java.util.Map;

public class PnioDecode extends BaseArtDecode<PnioConfig> {

    @Override
    public Map<String, Float> decode(PnioConfig pnioConfig, Map<String, Float> globalMap, byte[] bytes, FvDimensionLayer layer, Object... obj) {
        if(bytes==null) {
            return null;
        }
        else{
            decodetech(pnioConfig,globalMap,bytes,layer);
            return globalMap;
        }
    }

    /**
     * 写需要解析的协议名字，不清楚的可以查看PACKET_PROTOCOL.java
     * @return 协议名称如s7comm
     */
    @Override
    public String protocol() {
        return "pni0";
    }

    private static final int paddinglength = 24;//////////modify
    private static final int NUM = 2^31 - 1;
    private static byte[] zero =new byte[] {0x00,0x00,0x00,0x00};

    private void decodetech(PnioConfig pnioConfig, Map<String, Float> map, byte[] rawload, FvDimensionLayer layer)
    {
        if(rawload[12]==(byte)0x88 && rawload[13]==(byte)0x92 && rawload.length>(paddinglength + 20))
        {
            String srcmacaddress = layer.eth_src[0];
            byte[] status = Bytecut.Bytecut(rawload,rawload.length-paddinglength-4,4);
            byte[] data = Bytecut.Bytecut(rawload,16,rawload.length - paddinglength-20);
            if(srcmacaddress.equals(pnioConfig.getMacaddress()) && rawload[14]==(byte)0x80 && rawload[15]==(byte)0x00 && status[2]==0x35 && status[3]==0x00)
            {
                if(pnioConfig.getLength()==4)
                {
                    if(pnioConfig.getType().equals("float") && !Arrays.equals(data,zero)) {
                        map.put(pnioConfig.getTag(), Bytecut.BytesTofloat(data, pnioConfig.getByteoffset()));
                        callback(pnioConfig.getTag(),map.get(pnioConfig.getTag()),layer);
                    }
                    if(pnioConfig.getType().equals("int"))
                    {
                        map.put(pnioConfig.getTag(), (float)ByteUtil.bytesToInt(data,pnioConfig.getByteoffset())/NUM*pnioConfig.getRange()[1]);
                        callback(pnioConfig.getTag(),map.get(pnioConfig.getTag()),layer);
                    }
                }
                else if(pnioConfig.getLength()==2)
                {
                    if(pnioConfig.getType().equals("short"))
                    {
                        map.put(pnioConfig.getTag(),(float) ByteUtil.bytesToShort(data,pnioConfig.getByteoffset())/28686*pnioConfig.getRange()[1]);
                        callback(pnioConfig.getTag(),map.get(pnioConfig.getTag()),layer);
                    }
                }
                else if(pnioConfig.getType().equals("bool")) {
                    if (pnioConfig.getByteoffset() < data.length && pnioConfig.getBitoffset() < 8) {
                        if (((int) data[pnioConfig.getByteoffset()] & (1 << pnioConfig.getBitoffset())) == 0) {
                            map.put(pnioConfig.getTag(), 0f);
                            callback(pnioConfig.getTag(), map.get(pnioConfig.getTag()), layer);
                        } else {
                            map.put(pnioConfig.getTag(), 1f);
                            callback(pnioConfig.getTag(), map.get(pnioConfig.getTag()), layer);
                        }
                    }
                }
            }
        }
    }

}
