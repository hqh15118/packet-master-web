package com.zjucsc.application.handler;


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

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handleUnKnowException(RuntimeException e){
        return BaseResponse.ERROR(500,e.getMessage());
    }
}
