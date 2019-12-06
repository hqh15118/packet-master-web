package com.zjucsc.application.controller.artcontroller;


import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.ArtOpNameConfigStateDTO;
import com.zjucsc.application.domain.bean.ArtOpNameDTO;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.dto.ArtOptPageDTO;
import com.zjucsc.application.system.service.hessian_iservice.IArtOptNameService;
import com.zjucsc.attack.bean.BaseOpName;
import com.zjucsc.base.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("art_opt_name")
public class ArtOptNameController {

    @Autowired private IArtOptNameService iArtOptNameService;

    @PostMapping("insert")
    public BaseResponse insert(HttpServletRequest httpServletRequest){
        iArtOptNameService.insertArtOptName(HttpUtil.getJSONDataFromRequest(httpServletRequest));
        return BaseResponse.OK();
    }

    @PostMapping("update")
    public BaseResponse update(HttpServletRequest httpServletRequest){
        iArtOptNameService.updateArtOptName(HttpUtil.getJSONDataFromRequest(httpServletRequest));
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

    @PostMapping("select_protocol")
    public BaseResponse selectByProtocol(@RequestBody ArtOptPageDTO artOpNameDTOprotocol){
        return BaseResponse.OK(iArtOptNameService.
                selectByProtocolPaged(artOpNameDTOprotocol.getPage(),
                                      artOpNameDTOprotocol.getLimit(),
                                      artOpNameDTOprotocol.getProtocol()));
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
