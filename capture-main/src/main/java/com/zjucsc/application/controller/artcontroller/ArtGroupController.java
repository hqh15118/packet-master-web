package com.zjucsc.application.controller.artcontroller;


import com.zjucsc.application.domain.bean.ArtGroup;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.system.service.hessian_iservice.IArtGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("art_group")
public class ArtGroupController {

    @Autowired private IArtGroupService iArtGroupService;

    @PostMapping("insert")
    public BaseResponse insert(@RequestBody ArtGroup artGroup){
        iArtGroupService.insert(artGroup);
        return BaseResponse.OK();
    }

    @PostMapping("update")
    public BaseResponse update(@RequestBody ArtGroup artGroup){
        iArtGroupService.update(artGroup);
        return BaseResponse.OK();
    }

    @DeleteMapping("delete")
    public BaseResponse delete(@RequestParam String artGroup){
        iArtGroupService.delete(artGroup);
        return BaseResponse.OK();
    }

    @GetMapping("all")
    public BaseResponse selectAll(){
        return BaseResponse.OK(iArtGroupService.selectBatch());
    }
}
