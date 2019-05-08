package com.zjucsc.packetmasterweb.util;

import com.alibaba.fastjson.JSON;
import com.zjucsc.base.BaseResponse;

public class Util {


    public static void showJSON( String comment ,  BaseResponse baseResponse){
        System.out.println(comment);
        System.out.println(JSON.toJSONString(baseResponse));
    }
}
