package com.zjucsc.application.controller;


import com.zjucsc.application.domain.bean.ArtOptCommand1;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.system.service.hessian_iservice.IArtOptCommandService;
import com.zjucsc.attack.s7comm.S7OptCommandConfig;
import com.zjucsc.attack.s7comm.S7OptName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("art_opt_command")
public class ArtOptCommandController {

    @Autowired private IArtOptCommandService iArtOptCommandService;

    @PostMapping("insert")
    public BaseResponse insert(@RequestBody S7OptCommandConfig s7OptCommandConfig){
        iArtOptCommandService.insertArtOptCommand(s7OptCommandConfig);
        return BaseResponse.OK();
    }

    @PostMapping("update")
    public BaseResponse update(@RequestBody S7OptCommandConfig s7OptCommandConfig){
        iArtOptCommandService.updateArtOptCommand(s7OptCommandConfig);
        return BaseResponse.OK();
    }

    @GetMapping("delete")
    public BaseResponse delete(@RequestParam int id){
        //
        S7OptCommandConfig s7OptCommandConfig = iArtOptCommandService.deleteArtOptCommand(id);
        return BaseResponse.OK();
    }

    @GetMapping("select")
    public BaseResponse select(@RequestBody ArtOptCommand1 artOptCommand1){
        return BaseResponse.OK(iArtOptCommandService.selectArtOptCommand(artOptCommand1.getDeviceMac(),
                artOptCommand1.getOpName()));
    }
}
