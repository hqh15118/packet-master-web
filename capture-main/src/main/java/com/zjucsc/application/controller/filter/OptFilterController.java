package com.zjucsc.application.controller.filter;


import com.zjucsc.application.config.auth.Log;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.OptRulePullForFront;
import com.zjucsc.application.system.service.hessian_iservice.IOptFilterService;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.OptFilterUtil;
import com.zjucsc.common.exceptions.DeviceNotValidException;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
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
        CompletableFuture<List<String>> future =  iOptFilterService.getTargetExistIdFilter(
                optRulePullForFront.getDeviceNumber(),cache,optRulePullForFront.getProtocolId()
        );
        return BaseResponse.OK(future.get());
    }

//    /**
//     *
//     * @param deviceId > 0
//     * @param funcode
//     * @param protocolId
//     * @return
//     */
//    @ApiOperation("删除功能码规则[分别根据device/protocolId/funcode删除]")
//    @DeleteMapping("/delete_opt_filter")
//    @Log
//    public BaseResponse deleteOptFilter(@RequestParam String deviceId , @RequestParam int funcode , @RequestParam int protocolId) throws ProtocolIdNotValidException {
//        String deviceTag = CacheUtil.getTargetDeviceTagByNumber(deviceId);
//
//        if (funcode > 0 ){
//            iOptFilterService.deleteByDeviceNumberAndProtocolIdAndFuncode(deviceId, funcode, protocolId);
//            OptFilterUtil.removeTargetDeviceAnalyzerFuncode(deviceTag , funcode , protocolId);
//        }else if(protocolId > 0){
//            iOptFilterService.deleteByDeviceNumberAndProtocolId(deviceId,protocolId);
//            OptFilterUtil.removeTargetDeviceAnalyzerProtocol(deviceTag  , protocolId);
//        }else{
//            iOptFilterService.deleteByDeviceNumber(deviceId);
//            OptFilterUtil.disableTargetDeviceAnalyzer(deviceTag);
//        }
//        return BaseResponse.OK();
//    }
}
