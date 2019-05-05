package com.zjucsc.application.system.controller;


import com.zjucsc.application.domain.entity.Gplot;
import com.zjucsc.application.system.service.iservice.IGplotService;
import com.zjucsc.base.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/gplot/")
public class GplotController {

    @Qualifier("gplotservice")
    @Autowired private IGplotService iGplotService;

    @ApiOperation("添加组态图设备位置信息")
    @PostMapping("new_gplot")
    public BaseResponse addGplotInfo(@RequestBody @Valid Gplot.GplotForFront gplotForFront){
        Gplot gplot = new Gplot();
        gplot.setInfo(gplotForFront.info);
        gplot.setDevice_name(gplotForFront.device_name);
        iGplotService.save(gplot);
        return BaseResponse.OK(gplot.getDevice_id());
    }

    @ApiOperation("加载组态图设备位置信息")
    @GetMapping("load_gplot")
    public Gplot loadGplotInfo(@RequestParam int id) throws RuntimeException {
        Gplot gplot = iGplotService.getById(id);
        if (gplot == null){
            throw new RuntimeException(id + " 不存在");
        }
        System.out.println(gplot);
        return gplot;
    }

    @ApiOperation("修改组态图设备位置信息")
    @RequestMapping(value = "update_glot" , method = RequestMethod.POST)
    public BaseResponse updateGplotInfo(@RequestBody Gplot gplot){
        iGplotService.updateById(gplot);
        return BaseResponse.OK();
    }

    @ApiOperation("删除组态图设备位置信息")
    @DeleteMapping("delete_glot")
    public BaseResponse deleteGplotInfo(@RequestParam int gplotId){
        iGplotService.removeById(gplotId);
        return BaseResponse.OK();
    }
}
