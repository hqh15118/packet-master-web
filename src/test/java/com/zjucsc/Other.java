package com.zjucsc;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.LogBean;
import org.junit.Test;

public class Other {

    @Test
    public void showLogBeanInfo(){
        Exception exception = new Exception("test_exception");
        LogBean logBean = LogBean.builder()
                .result("result")
                .methodArgs(new Object[]{1,2,3})
                .logType(0)
                .methodName("method_name")
                .clazzName("clazz_name")
                .exception(exception.toString())
                .costTime(100)
                .build();
        System.out.println(JSON.toJSONString(logBean));
    }
}
