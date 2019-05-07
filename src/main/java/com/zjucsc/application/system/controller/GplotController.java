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

    @Autowired private IGplotService iGplotService;

    @ApiOperation("添加组态图设备位置信息")
    @PostMapping("new_gplot")
    public BaseResponse addGplotInfo(@RequestBody @Valid Gplot.GplotForFront gplotForFront){
        Gplot gplot = new Gplot();
        gplot.setInfo(gplotForFront.info);
        gplot.setName(gplotForFront.name);
        iGplotService.save(gplot);
        return BaseResponse.OK(gplot.getId());
    }

    @ApiOperation("加载组态图设备位置信息")
    @GetMapping("load_gplot")
    public BaseResponse loadGplotInfo(@RequestParam int id) throws RuntimeException {
        Gplot gplot = iGplotService.getById(id);
        if (gplot == null){
            throw new RuntimeException(id + " 不存在");
        }
        return BaseResponse.OK(gplot);
    }

    @ApiOperation("加载所有的组态图")
    @GetMapping("load_all_gplot")
    public BaseResponse loadAllGplotInfo(){
        return BaseResponse.OK(iGplotService.list());
    }

    @ApiOperation("修改组态图设备位置信息")
    @RequestMapping(value = "update_gplot" , method = RequestMethod.POST)
    public BaseResponse updateGplotInfo(@RequestBody Gplot gplot){
        iGplotService.updateById(gplot);
        return BaseResponse.OK();
    }

    @ApiOperation("删除组态图设备位置信息")
    @DeleteMapping("delete_gplot")
    public BaseResponse deleteGplotInfo(@RequestParam int gplotId){
        iGplotService.removeById(gplotId);
        return BaseResponse.OK();
    }
}
