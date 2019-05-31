package com.zjucsc.application.controller;

import com.zjucsc.application.domain.bean.ArtHistoryBean;
import com.zjucsc.application.system.service.hessian_mapper.ArtHistoryDataMapper;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/art_data/")
public class ArtHistoryDataController {
    @Autowired
    private ArtHistoryDataMapper artHistoryDataMapper;

    @ApiOperation("查询一段时间内的历史数据")
    @PostMapping("history")
    public BaseResponse getArtHistoryData(@RequestBody ArtHistoryBean artHistoryBean){
        return BaseResponse.OK(artHistoryDataMapper.getArtData(artHistoryBean.getStartTime(),
                artHistoryBean.getEndTime(),artHistoryBean.getArtArg() , artHistoryBean.getTimeType()));
    }

}
