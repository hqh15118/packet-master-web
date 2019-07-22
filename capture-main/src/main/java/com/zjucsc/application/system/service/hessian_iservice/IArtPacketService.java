package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtPacketDetail;
import com.zjucsc.application.domain.bean.ArtPacketOption;

import java.util.List;

public interface IArtPacketService {
    void insertArtPacket(String artName, ArtPacketDetail layer);
    List<ArtPacketDetail> selectPacketsByArtName(ArtPacketOption artPacketOption);
}
