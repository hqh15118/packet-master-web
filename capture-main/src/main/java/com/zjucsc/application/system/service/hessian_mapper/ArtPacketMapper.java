package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ArtPacketDetail;
import com.zjucsc.application.domain.bean.ArtPacketOption;

import java.util.List;

public interface ArtPacketMapper {
    void insertArtPacket(String artName, ArtPacketDetail layer);
    List<ArtPacketDetail> selectPacketsByArtName(ArtPacketOption artPacketOption);
}
