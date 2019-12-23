package com.zjucsc.application.controller.artcontroller.eleccontroller;


import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.dto.ElecDTO;
import com.zjucsc.art_decode.ArtDecodeUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("elec")
public class ElecController {

    @Data
    private static class ResultWrapper{
        private float value;
        private String id;

        public ResultWrapper(float value, String id) {
            this.value = value;
            this.id = id;
        }
    }

    @PostMapping("art_info")
    public BaseResponse getElecArtInfo(@RequestBody @Valid ElecDTO elecDTO){
        List<ResultWrapper> floats = new ArrayList<>(0);
        switch (elecDTO.getProtocol()){
            case "iec104":
                for (String id : elecDTO.getId()) {
                    Float res = ArtDecodeUtil.getElecDataByIpAddressAndId(elecDTO.getProtocol(), elecDTO.getIpAddress(),id);
                    if (res!=null){
                        floats.add(new ResultWrapper(res,id));
                    }
                }
                return BaseResponse.OK(floats);
        }
        throw new RuntimeException("暂不支持{" + elecDTO.getProtocol() + "}的电网数据查询");
    }
}
