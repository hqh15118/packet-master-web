package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.ArtPacketDetail;
import com.zjucsc.application.domain.bean.ArtPacketOption;
import com.zjucsc.application.system.service.hessian_iservice.IArtPacketService;
import com.zjucsc.application.system.service.hessian_mapper.ArtPacketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtPacketServiceImpl implements IArtPacketService {

    @Autowired private ArtPacketMapper artPacketMapper;

    @Async("common_async")
    @Override
    public void insertArtPacket(String artName, ArtPacketDetail layer) {
        artPacketMapper.insertArtPacket(artName, layer);
    }

    @Override
    public List<ArtPacketDetail> selectPacketsByArtName(ArtPacketOption artPacketOption) {
        return artPacketMapper.selectPacketsByArtName(artPacketOption);
    }
}
