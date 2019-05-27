package com.zjucsc;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.LogBean;
import jdk.internal.util.xml.impl.Input;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void tsharkTest() throws IOException {
        String command = "tshark -l -n  -i en0 -T ek -e frame.protocols -e eth.dst -e eth.src -e frame.cap_len -e ip.dst -e ip.src -e tcp.srcport -e tcp.dstport -e eth.trailer -e eth.fcs -e tcp.payload -Y \"not s7comm and not modbus and not 104apci\"";
        Process process = Runtime.getRuntime().exec(new String[]{"bash" , "-c" ,command});
        doStream(process.getInputStream() , process.getErrorStream());
    }

    private void doStream(InputStream inputStream,InputStream errorStream) throws IOException {
        BufferedReader bfreader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader bfreader_error = new BufferedReader(new InputStreamReader(errorStream));
        System.out.println(bfreader_error.readLine());
        for (;;){
            String s = bfreader.readLine();
            if (s == null){
                System.out.println("end read");
                break;
            }else{
                System.out.println(s);
            }
        }
    }
}
