package com.zjucsc.application.handler;


import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.base.BaseResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Configuration
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OpenCaptureServiceException.class)
    public BaseResponse handleOpenCaptureServiceException(OpenCaptureServiceException e){
        return BaseResponse.ERROR(226,e.getMessage());
    }
}
