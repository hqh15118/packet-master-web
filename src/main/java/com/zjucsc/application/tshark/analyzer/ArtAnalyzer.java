package com.zjucsc.application.tshark.analyzer;

import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.tshark.filter.art.S7ArtPacketFilter;
import com.zjucsc.application.util.AbstractAnalyzer;
import com.zjucsc.application.util.Analyzed;
import com.zjucsc.application.util.PacketDecodeUtil;

public class ArtAnalyzer extends AbstractAnalyzer<S7ArtPacketFilter> implements Analyzed {

    public ArtAnalyzer(S7ArtPacketFilter s7ArtPacketFilter) {
        super(s7ArtPacketFilter);
    }

    @Override
    public Object analyze(Object... objs) throws ProtocolIdNotValidException {
        String tcpPayload = (String)objs[0];
        byte[] tcpPayloadB = PacketDecodeUtil.hexStringToByteArray(tcpPayload);
        return null;
    }
}
