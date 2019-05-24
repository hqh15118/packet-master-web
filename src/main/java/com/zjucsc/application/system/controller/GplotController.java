package com.zjucsc.application.system.controller;


import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.Gplot;
import com.zjucsc.application.system.service.iservice.IGplotService;
import com.zjucsc.base.BaseResponse;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/gplot/")
public class GplotController {

    @Autowired private IGplotService iGplotService;

    @Log
    @ApiOperation("添加组态图设备位置信息")
    @PostMapping("new_gplot")
    //@Timed(value = "add.gplot")
    public BaseResponse addGplotInfo(@RequestBody @Valid Gplot.GplotForFront gplotForFront){
        Gplot gplot = new Gplot();
        gplot.setInfo(gplotForFront.info);
        gplot.setName(gplotForFront.name);
        gplot.setUpdateTime(new Date().toString());
        iGplotService.addNewGplot(gplot);
        return BaseResponse.OK(gplot.getId());
    }

    @Log
    @ApiOperation("加载组态图设备位置信息")
    @GetMapping("load_gplot")
    public BaseResponse loadGplotInfo(@RequestParam int id) throws RuntimeException {
        Gplot gplot = iGplotService.getById(id);
        if (gplot == null){
            throw new RuntimeException(id + " 不存在");
        }
        return BaseResponse.OK(gplot);
    }

    @Log
    @ApiOperation("加载所有的组态图")
    @GetMapping("load_all_gplot")
    public BaseResponse loadAllGplotInfo(){
        List<Gplot> gplots = iGplotService.list();
        gplots.sort((o1, o2) -> {
            if (o1.getId() <= o2.getId()){return 1;}
            return -1;
        });
        return BaseResponse.OK(gplots);
    }


    @Log
    @ApiOperation("修改组态图设备位置信息")
    @RequestMapping(value = "update_gplot" , method = RequestMethod.POST)
    public BaseResponse updateGplotInfo(@RequestBody Gplot gplot){
        gplot.setUpdateTime(new Date().toString());
        iGplotService.updateById(gplot);
        return BaseResponse.OK();
    }

    @Log
    @ApiOperation("删除组态图设备位置信息")
    @DeleteMapping("delete_gplot")
    public BaseResponse deleteGplotInfo(@RequestParam int gplotId){
        iGplotService.removeById(gplotId);
        return BaseResponse.OK();
    }

    @ApiOperation("设置选定的组态图ID")
    @GetMapping("set_gplot_id")
    @Log
    public BaseResponse setGplotId(@RequestParam int gplotId) throws ProtocolIdNotValidException {
        iGplotService.changeGplot(gplotId);
        return BaseResponse.OK();
    }


}
