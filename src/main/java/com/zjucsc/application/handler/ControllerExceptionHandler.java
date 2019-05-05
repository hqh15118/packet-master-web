package com.zjucsc.application.handler;


import com.alibaba.fastjson.JSONException;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.base.BaseResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(OpenCaptureServiceException.class)
    @ResponseBody
    public BaseResponse handleOpenCaptureServiceException(OpenCaptureServiceException e){
        return BaseResponse.ERROR(226,e.getMessage());
    }

    /**
     * JSON格式异常检测
     * @param e
     * @return
     */
    @ExceptionHandler(JSONException.class)
    @ResponseBody
    public BaseResponse handlerJSONContentValidException(JSONException e){
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.JSON_ERROR,e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    public BaseResponse handlerDeviceException(DeviceNotValidException d){
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.DEVICE_ERROR,d.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handleUnKnowException(RuntimeException e){
        return BaseResponse.ERROR(500,e.getMessage());
    }
}
