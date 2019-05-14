package com.zjucsc.application.system.controller;


import com.zjucsc.application.system.entity.Art;
import com.zjucsc.application.system.service.IArtService;
import com.zjucsc.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/art")
public class ArtController {

    @Autowired private IArtService iArtService;

    @PostMapping("add_art")
    public BaseResponse addArtConfig(@RequestBody List<Art> artForFronts){
        iArtService.saveOrUpdateBatch(artForFronts);
        return null;
    }
}
