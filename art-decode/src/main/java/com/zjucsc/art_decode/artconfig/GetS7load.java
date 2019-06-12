package com.zjucsc.art_decode.artconfig;


import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.common_util.Bytecut;

import java.util.Arrays;

public class GetS7load {

    private byte[] getfromtcp(byte[] payload)
    {
        int length = ByteUtil.bytesToShort(payload,2);
        int ISOlen = (int)payload[4];
        return Bytecut.Bytecut(payload,(5 + ISOlen),(length - 5 - ISOlen));
    }

    private byte[] getfromiso(byte[] rawload)
    {
        int length = ByteUtil.bytesToShort(rawload,12);
        if(Arrays.equals(Bytecut.Bytecut(rawload,14,3),new byte[] {(byte)0xfe,(byte)0xfe,(byte)0x03}))
        {
            int len1 = rawload[17];
            byte[] rawload1 = Bytecut.Bytecut(rawload,17 + len1,-1);
            int len2 = rawload1[0];
            return Bytecut.Bytecut(rawload1,len2+1,length - 5 - len1 - len2);
        }
        return null;
    }

    public byte[] S7load(byte[] load, int i)
    {
        if(load != null) {
            if (i == 0) {
                return getfromiso(load);
            } else if (i == 1) {
                return getfromtcp(load);
            }
        }
        return null;
    }
}
