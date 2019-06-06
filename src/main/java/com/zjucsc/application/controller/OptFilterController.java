package com.zjucsc.application.controller;


import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.OptRulePullForFront;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.service.hessian_iservice.IOptFilterService;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonOptFilterUtil;
import com.zjucsc.application.domain.bean.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author hongqianhui
 */
@RestController
@RequestMapping("/opt_filter/")
public class OptFilterController {

    @Autowired private IOptFilterService iOptFilterService;

    @ApiOperation("获取功能码规则")
    @PostMapping("get_opt_filter")
    @Log
    public BaseResponse getOptFilter(@RequestBody @Valid OptRulePullForFront optRulePullForFront) throws ProtocolIdNotValidException, ExecutionException, InterruptedException, DeviceNotValidException {
        boolean cache = false;
        if (optRulePullForFront.getCached() == 1){
            cache = true;
        }
        CompletableFuture<List<Integer>> future =  iOptFilterService.getTargetExistIdFilter(
                optRulePullForFront.getDeviceNumber(),cache,optRulePullForFront.getProtocolId()
        );
        return BaseResponse.OK(future.get());
    }

    /**
     *
     * @param deviceId > 0
     * @param funcode
     * @param protocolId
     * @return
     */
    @ApiOperation("删除功能码规则[分别根据device/protocolId/funcode删除]")
    @DeleteMapping("/delete_opt_filter")
    @Log
    public BaseResponse deleteOptFilter(@RequestParam String deviceId , @RequestParam int funcode , @RequestParam int protocolId) throws ProtocolIdNotValidException {
        String deviceIp = CommonCacheUtil.getTargetDeviceIpByNumber(deviceId);

        if (funcode > 0 ){
            CommonOptFilterUtil.removeTargetDeviceAnalyzerFuncode(deviceIp , funcode , protocolId);
            iOptFilterService.deleteByDeviceNumberAndProtocolIdAndFuncode(deviceId, funcode, protocolId);
        }else if(protocolId > 0){
            CommonOptFilterUtil.removeTargetDeviceAnalyzerProtocol(deviceIp  , protocolId);
            iOptFilterService.deleteByDeviceNumberAndProtocolId(deviceId,protocolId);
        }else{
            CommonOptFilterUtil.removeTargetDeviceAnalyzer(deviceIp);
            iOptFilterService.deleteByDeviceNumber(deviceId);
        }
        return BaseResponse.OK();
    }
}
