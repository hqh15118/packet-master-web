package com.zjucsc.application.handler;


import com.alibaba.fastjson.JSONException;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.common.exceptions.*;
import javassist.CannotCompileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@Configuration
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(OpenCaptureServiceException.class)
    @ResponseBody
    public BaseResponse handleOpenCaptureServiceException(OpenCaptureServiceException e){
        printException(e);
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
        printException(e);
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.JSON_ERROR,e.getMessage());
    }

    @ExceptionHandler(DeviceNotValidException.class)
    @ResponseBody
    public BaseResponse handleDeviceException(DeviceNotValidException e){
        printException(e);
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.DEVICE_ERROR,e.getMsg());
    }

    @ExceptionHandler(ProtocolIdNotValidException.class)
    @ResponseBody
    public BaseResponse handleProtocolIdNotValidException(ProtocolIdNotValidException e){
        printException(e);
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.PROTOCOL_ID_ERROR,e.getMsg());
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public BaseResponse handleCommandNotValidException(IOException e){
        printException(e);
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.COMMAND_NOT_VALID,e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public BaseResponse handleCommandNotValidException(SQLException e){
        printException(e);
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SQL_ERROR,e.getMessage());
    }

    @ExceptionHandler(TokenNotValidException.class)
    @ResponseBody
    public BaseResponse handleCommandNotValidException(TokenNotValidException e){
        printException(e);
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.TOKEN_NOT_VALID,e.getMsg());
    }

    @ExceptionHandler(ArtConfigNotValidException.class)
    @ResponseBody
    public BaseResponse handleArtConfigNotValidException(ArtConfigNotValidException e){
        printException(e);
        return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.ART_CONFIG_NOT_VALID,e.getMsg());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public BaseResponse handleDataAccessException(DataAccessException e){
        printException(e);
        return BaseResponse.ERROR(e.getCode(),e.getMsg());
    }
    
    @ExceptionHandler(CannotCompileException.class)
    @ResponseBody
    public BaseResponse handleFormulaCompileException(CannotCompileException e){
        log.error("公式配置错误",e);
        return BaseResponse.ERROR(500,"公式配置错误");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handleUnKnowException(Exception e){
        log.error("controller exception \n",e);
        return BaseResponse.ERROR(500,e.getMessage());
    }

    private void printException(Exception e){
        log.error("catch controller exception : \n **************\nclass : {} ; msg : {} \n**************" , e.getClass() , e.getStackTrace()[0].toString());
        e.printStackTrace();
    }
}
