package com.zjucsc.application.tshark.analyzer;

import com.zjucsc.AttackType;
import com.zjucsc.IArtDecode;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.ThreadLocalWrapper;
import com.zjucsc.application.util.AbstractAnalyzer;
import com.zjucsc.application.util.Analyzed;
import com.zjucsc.application.util.CommonUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class ArtAnalyzer extends AbstractAnalyzer<Map<String, IArtDecode>> implements Analyzed {


    public ArtAnalyzer(Map<String, IArtDecode> stringAbstractArtPacketFilterMap) {
        super(stringAbstractArtPacketFilterMap);
    }

    @Override
    public Object analyze(Object... objs) {
        String tcpPayload = (String)objs[0];
        //System.out.println(tcpPayload);
        String protocol = ((String) objs[1]);
        byte[] tcpPayloadArrInByte = PacketDecodeUtil.hexStringToByteArray(tcpPayload);
        protocol = transferProtocol(protocol);

        IArtDecode iArtDecode;
        if ((iArtDecode = getAnalyzer().get(protocol))!=null){
            Map<String,Float> res = iArtDecode.decode(CommonUtil.getGlobalArtMap(),tcpPayloadArrInByte);
            List<AttackType> attackTypeList = null;
            if (res!=null){
                attackTypeList = iArtDecode.attackDecode(CommonUtil.getGlobalAttackList(), tcpPayloadArrInByte,res);
            }
            return new ThreadLocalWrapper(res , attackTypeList);
        }else {
            //log.debug("can not decode art args of protocol : {}" , protocol);
            return null;
        }
    }

    /**
     *
     * @param rawProtocol 原始协议字符串
     * @return 一些协议需要更改
     */
    private String transferProtocol(String rawProtocol){
        if (rawProtocol.contains("s7comm")){
            rawProtocol = "s7comm";
        }
        return rawProtocol;
    }
}
