package com.zjucsc.application.controller.artcontroller;


import com.zjucsc.application.domain.bean.ArtOpNameConfigStateDTO;
import com.zjucsc.application.domain.bean.ArtOpNameDTO;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.system.service.hessian_iservice.IArtOptNameService;
import com.zjucsc.attack.bean.BaseOpName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("art_opt_name")
public class ArtOptNameController {

    @Autowired private IArtOptNameService iArtOptNameService;

    @PostMapping("insert")
    public BaseResponse insert(@RequestBody BaseOpName opNameConfig){
        iArtOptNameService.insertArtOptName(opNameConfig);
        return BaseResponse.OK();
    }

    @PostMapping("update")
    public BaseResponse update(@RequestBody BaseOpName opNameConfig){
        iArtOptNameService.updateArtOptName(opNameConfig);
        return BaseResponse.OK();
    }

    @PostMapping("delete")
    public BaseResponse delete(@RequestBody ArtOpNameDTO artOpNameDTO){
        iArtOptNameService.deleteArtOptName(artOpNameDTO);
        return BaseResponse.OK();
    }

    @PostMapping("select")
    public BaseResponse select(@RequestBody ArtOpNameDTO artOpNameDTO){
        return BaseResponse.OK(iArtOptNameService.selectArtOptName(artOpNameDTO));
    }

    @GetMapping("all")
    public BaseResponse selectAll(){
        return BaseResponse.OK(iArtOptNameService.selectBatch());
    }

    @GetMapping("select_protocol")
    public BaseResponse selectByProtocol(@RequestParam String protocol){
        return BaseResponse.OK(iArtOptNameService.selectByProtocol(protocol));
    }

    @PostMapping("enable")
    public BaseResponse enableArtOpConfig(@RequestBody ArtOpNameConfigStateDTO artOpNameConfigStateDTO){
        if (iArtOptNameService.enableOptNameConfig(artOpNameConfigStateDTO)) {
            return BaseResponse.OK();
        }else{
            return BaseResponse.ERROR(500,"状态修改失败");
        }
    }

}
