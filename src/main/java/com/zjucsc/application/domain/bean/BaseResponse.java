package com.zjucsc.application.domain.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "return")
public class BaseResponse implements Serializable {
    @ApiModelProperty(value = "code")
    public int code;
    @ApiModelProperty(value = "msg")
    public String msg;
    @ApiModelProperty(value = "data")
    public Object data;
    private final static BaseResponse OK = new BaseResponse();
    public static BaseResponse ERROR(int errorCode, String errorMsg){
        BaseResponse response = new BaseResponse();
        response.code = errorCode;
        response.msg = errorMsg;
        return response;
    }
    static {
        OK.code = 200;
        OK.msg = "操作成功";
    }

    public static BaseResponse OK(Object data){
        BaseResponse OK = new BaseResponse();
        OK.code = 200;
        OK.msg = "操作成功";
        OK.data = data;
        return OK;
    }

    public static BaseResponse OK(){
        return OK;
    }

    public BaseResponse setData(Object data){
        this.data = data;
        return this;
    }

    public BaseResponse(){

    }
}
