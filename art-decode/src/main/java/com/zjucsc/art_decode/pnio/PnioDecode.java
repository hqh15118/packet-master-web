package com.zjucsc.art_decode.pnio;

import com.zjucsc.art_decode.artconfig.PnioConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.other.AttackType;
import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.common_util.Bytecut;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PnioDecode extends BaseArtDecode<PnioConfig> {

    /**
     * 解析入口方法
     * @param testConfig 自定义的通用配置
     * @param map 全局的参数map，key是参数名，value是参数值
     * @param bytes payload【原始数据/tcp payload】
     * @param objects 其他数据
     * @return 返回map即可
     */
    @Override
    public Map<String, Float> decode(PnioConfig testConfig, Map<String, Float> map, byte[] bytes, Object... objects) {
        if(bytes==null) {
            return null;
        }
        else{
            decodetech(testConfig,map,bytes);
            return map;
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

    private void decodetech(PnioConfig pnioConfig, Map<String, Float> map, byte[] rawload)
    {
        if(rawload[12]==(byte)0x88 && rawload[13]==(byte)0x92 && rawload.length>(paddinglength + 20))
        {
            byte[] srcmacaddress = Bytecut.Bytecut(rawload,6,6);
            byte[] status = Bytecut.Bytecut(rawload,rawload.length-paddinglength-4,4);
            byte[] data = Bytecut.Bytecut(rawload,16,rawload.length - paddinglength-20);
            if(Arrays.equals(srcmacaddress,pnioConfig.getMacaddress()) && rawload[14]==(byte)0x80 && rawload[15]==(byte)0x00 && status[2]==0x35 && status[3]==0x00)
            {
                if(pnioConfig.getLength()==4)
                {
                    if(pnioConfig.getType().equals("float")) {
                        map.put(pnioConfig.getTag(), Bytecut.BytesTofloat(data, pnioConfig.getByteoffset()));
                    }
                    if(pnioConfig.getType().equals("int"))
                    {
                        map.put(pnioConfig.getTag(), (float)ByteUtil.bytesToInt(data,pnioConfig.getByteoffset())/(2^31-1)*pnioConfig.getRange()[1]);
                    }
                }
                else if(pnioConfig.getLength()==2)
                {
                    if(pnioConfig.getType().equals("short"))
                    {
                        map.put(pnioConfig.getTag(),(float) ByteUtil.bytesToShort(data,pnioConfig.getByteoffset())/32767*pnioConfig.getRange()[1]);
                    }
                }
                else if(pnioConfig.getType().equals("bool"))
                {
                    if(pnioConfig.getByteoffset()<data.length && pnioConfig.getBitoffset()<8)
                    if(((int)data[pnioConfig.getByteoffset()] & 1<<pnioConfig.getBitoffset()) == 0)
                    {
                        map.put(pnioConfig.getTag(),0f);
                    }
                    else{
                        map.put(pnioConfig.getTag(),1f);
                    }
                }
            }
        }
    }

}
