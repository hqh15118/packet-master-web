package com.zjucsc.application.controller.config;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.Gplot;
import com.zjucsc.application.system.service.hessian_iservice.IGplotService;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        return BaseResponse.OK(iGplotService.addNewGplot(gplot).data);
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
        List<Gplot> gplots = iGplotService.selectAll();
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
        iGplotService.deleteById(gplotId);
        Common.GPLOT_ID = 0;
        return BaseResponse.OK();
    }

    @ApiOperation("设置选定的组态图ID")
    //@GetMapping("set_gplot_id")
    @Log
    public BaseResponse setGplotId(@RequestParam int gplotId) throws ProtocolIdNotValidException {
        iGplotService.changeGplot(gplotId);
        return BaseResponse.OK();
    }


}
