package com.zjucsc.application.controller;


import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.system.service.hessian_iservice.IArtOptNameService;
import com.zjucsc.attack.s7comm.S7OptName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("art_opt_name")
public class ArtOptNameController {

    @Autowired private IArtOptNameService iArtOptNameService;

    @PostMapping("insert")
    public BaseResponse insert(@RequestBody S7OptName s7OptName){
        iArtOptNameService.insertArtOptName(s7OptName);
        return BaseResponse.OK();
    }

    @PostMapping("update")
    public BaseResponse update(@RequestBody S7OptName s7OptName){
        iArtOptNameService.updateArtOptName(s7OptName);
        return BaseResponse.OK();
    }

    @GetMapping("delete")
    public BaseResponse delete(@RequestParam String optName){
        S7OptName s7OptName = iArtOptNameService.deleteArtOptName(optName);
        return BaseResponse.OK();
    }

    @GetMapping("select")
    public BaseResponse select(@RequestBody String s7OptName){
        return BaseResponse.OK(iArtOptNameService.selectArtOptName(s7OptName));
    }

}
