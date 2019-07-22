package com.zjucsc.application.controller;

import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.DeviceProtocol;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.domain.non_hessian.FvDimensionFilterCondition;
import com.zjucsc.application.system.service.hessian_iservice.IFvDimensionFilterService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.ConfigUtil;
import com.zjucsc.common.exceptions.DeviceNotValidException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.Config;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
/*-****************************************************
 * FiveDimensionFilterForFront
 * {
 *     device_id : xxx   设备ID         P_K
 *     userName : xxx   用户名         验证
 *     [
 *       {
 *          filterType : 0 / 1        过滤类别：0表示白名单，1表示黑名单
 *          protocolID : 协议ID        过滤的协议，需要转换为String
 *          port ： 端口
 *          src_ip ：
 *          dst_ip ：
 *       },
 *       ...
 *     ]
 * }
 *-***************************************************/
@RestController
@RequestMapping("/gen/fv_dimension_filter")
public class FvDimensionFilterController {

    @Autowired private IFvDimensionFilterService iFvDimensionFilterService;

    @Log
    @ApiOperation("添加/更新[五元组 + 功能码]过滤规则")
    @PostMapping("/new_rule")
    public BaseResponse addFvDimensionFilterRules(@RequestBody @Valid @NotEmpty List<Rule> list) throws ExecutionException, InterruptedException {
        if (list.size() == 0){
            return BaseResponse.ERROR(500,"未添加任何规则");
        }
        CompletableFuture<Exception> future =  iFvDimensionFilterService.addFvDimensionFilter(list);
        return BaseResponse.OK(future.get());
    }

    @Log
    @ApiOperation(value = "查询五元组异常报文规则")
    @GetMapping("/get_fv_packet_rule")
    public BaseResponse loadFvDimensionPacketRule(@RequestParam String deviceNumber) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<List<Rule>> future =  iFvDimensionFilterService.getTargetExistIdFilter(deviceNumber , false);
        return BaseResponse.OK(future.get());
    }

    @Log
    @ApiOperation(value = "查询已经成功挂载的五元组异常报文规则[确认是否已经将规则下载到服务器]")
    @GetMapping("/get_fv_packet_rule_cached")
    public BaseResponse loadFvDimensionPacketRuleCached(@RequestParam String deviceNumber) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<List<Rule>> future =  iFvDimensionFilterService.getTargetExistIdFilter(deviceNumber , true);
        return BaseResponse.OK(future.get());
    }


    @ApiOperation("实时五元组过滤规则")
    @PostMapping("filter_rule")
    public BaseResponse addRealTimeFilterRule(@RequestBody FvDimensionFilterCondition fvDimensionFilterCondition){
        ConfigUtil.setData("filter_src_mac",fvDimensionFilterCondition.getSrcMac());
        ConfigUtil.setData("filter_src_ip",fvDimensionFilterCondition.getSrcIp());
        ConfigUtil.setData("filter_src_port",fvDimensionFilterCondition.getSrcPort());
        ConfigUtil.setData("filter_dst_mac",fvDimensionFilterCondition.getDstMac());
        ConfigUtil.setData("filter_dst_ip",fvDimensionFilterCondition.getDstIp());
        ConfigUtil.setData("filter_dst_port",fvDimensionFilterCondition.getDstPort());
        ConfigUtil.setData("filter_protocol",fvDimensionFilterCondition.getProtocol());
        ConfigUtil.setData("filter_funcode",fvDimensionFilterCondition.getFunCode());
        CommonCacheUtil.addOrUpdateFvDimensionFilterCondition(fvDimensionFilterCondition);
        return BaseResponse.OK();
    }
}