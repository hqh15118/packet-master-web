package com.zjucsc.application.handler;


import com.alibaba.fastjson.JSONException;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.base.BaseResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

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
    public BaseResponse handleJSONContentValidException(JSONException e){
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.JSON_ERROR,e.getMessage());
    }

    @ExceptionHandler(DeviceNotValidException.class)
    @ResponseBody
    public BaseResponse handleDeviceException(DeviceNotValidException d){
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.DEVICE_ERROR,d.getMessage());
    }

    @ExceptionHandler(ProtocolIdNotValidException.class)
    @ResponseBody
    public BaseResponse handleProtocolIdNotValidException(ProtocolIdNotValidException e){
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.PROTOCOL_ID_ERROR,e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public BaseResponse handleCommandNotValidException(IOException e){
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.COMMAND_NOT_VALID,e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handleUnKnowException(RuntimeException e){
        return BaseResponse.ERROR(500,e.getMessage());
    }
}
