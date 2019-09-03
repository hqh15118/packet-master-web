package com.zjucsc.application.controller;


import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.system.service.hessian_iservice.IArtOptNameService;
import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.attack.s7comm.S7OpName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("art_opt_name")
public class ArtOptNameController {

    @Autowired private IArtOptNameService iArtOptNameService;

    @PostMapping("insert")
    public BaseResponse insert(@RequestBody S7OpName s7OptName){
        iArtOptNameService.insertArtOptName(s7OptName);
        return BaseResponse.OK();
    }

    @PostMapping("update")
    public BaseResponse update(@RequestBody S7OpName s7OptName){
        iArtOptNameService.updateArtOptName(s7OptName);
        return BaseResponse.OK();
    }

    @DeleteMapping("delete")
    public BaseResponse delete(@RequestParam String optName){
        BaseOpName baseOpName = iArtOptNameService.deleteArtOptName(optName);
        return BaseResponse.OK();
    }

    @GetMapping("select")
    public BaseResponse select(@RequestParam String s7OptName){
        return BaseResponse.OK(iArtOptNameService.selectArtOptName(s7OptName));
    }

    @GetMapping("all")
    public BaseResponse selectAll(){
        return BaseResponse.OK(iArtOptNameService.selectBatch());
    }

    @GetMapping("select_protocol")
    public BaseResponse selectByProtocol(@RequestParam String protocol){
        return BaseResponse.OK(iArtOptNameService.selectByProtocol(protocol));
    }

}
