package com.zjucsc.application.controller.artcontroller;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.ArtBeanFWrapper;
import com.zjucsc.application.domain.bean.ArtHistoryBean;
import com.zjucsc.application.domain.bean.ArtHistoryForFront;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.non_hessian.ArtBean;
import com.zjucsc.application.system.service.hessian_iservice.IArtHistoryDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/art_data/")
public class ArtHistoryDataController {

    @Autowired private IArtHistoryDataService iArtHistoryDataService;

    @ApiOperation("查询一段时间内的历史数据")
    @PostMapping("history")
    public BaseResponse getArtHistoryData(@RequestBody ArtHistoryBean artHistoryBean) throws Exception {
        CompletableFuture<ArtHistoryForFront> artHistoryForFrontCompletableFuture =  iArtHistoryDataService.getArtData(artHistoryBean.getStart(),
                artHistoryBean.getEnd(),artHistoryBean.getNameList());
        ArtHistoryForFront artHistoryForFront = artHistoryForFrontCompletableFuture.get();
        return BaseResponse.OK(JSON.toJSONString(artHistoryForFront));
    }

    @ApiOperation("查询工艺参数有关报文")
    @PostMapping("all")
    public BaseResponse getAllArtPacket(@RequestBody ArtBean artBean) throws ExecutionException, InterruptedException {
        CompletableFuture<ArtBeanFWrapper> artBeanFCompletableFuture = iArtHistoryDataService.getAllArtData(artBean);
        return BaseResponse.OK(artBeanFCompletableFuture.get());
    }


}
