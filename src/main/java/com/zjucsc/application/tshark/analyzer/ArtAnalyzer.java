package com.zjucsc.application.tshark.analyzer;

import com.zjucsc.IArtDecode;
import com.zjucsc.application.util.AbstractAnalyzer;
import com.zjucsc.application.util.Analyzed;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ArtAnalyzer extends AbstractAnalyzer<Map<String, IArtDecode>> implements Analyzed {


    public ArtAnalyzer(Map<String, IArtDecode> stringAbstractArtPacketFilterMap) {
        super(stringAbstractArtPacketFilterMap);
    }

    @Override
    public Object analyze(Object... objs) {
        String tcpPayload = (String)objs[0];
        String protocol = ((String) objs[1]);
        byte[] tcpPayloadArrInByte = PacketDecodeUtil.hexStringToByteArray(tcpPayload);
        IArtDecode iArtDecode = null;
        if ((iArtDecode = getAnalyzer().get(protocol))!=null){
            return iArtDecode.decode(tcpPayloadArrInByte);
        }else {
            log.debug("can not decode art args of protocol : {}" , protocol);
            return null;
        }
    }
}
