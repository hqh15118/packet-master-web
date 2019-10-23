package com.zjucsc.application.controller.artcontroller;


import com.zjucsc.application.domain.bean.ArtPacketOption;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.system.service.hessian_iservice.IArtPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("art_packet")
public class ArtPacketController {

    @Autowired private IArtPacketService iArtPacketService;

    @PostMapping("all")
    public BaseResponse getPacketsByArtName(@RequestBody ArtPacketOption artPacketOption){
        return BaseResponse.OK(iArtPacketService.selectPacketsByArtName(artPacketOption));
    }
}
