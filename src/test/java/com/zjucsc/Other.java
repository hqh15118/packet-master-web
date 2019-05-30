package com.zjucsc;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import jdk.internal.util.xml.impl.Input;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.json.simple.JSONValue.toJSONString;

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

    /**
     * 10W 条750ms
     */
    @Test
    public void fastJSONTestOfFvDimensionInstanceToStr(){
        FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
        fvDimensionLayer.frame_protocols = new String[]{"s7comm"};
        fvDimensionLayer.ip_dst = new String[]{"192.168.0.121"};
        fvDimensionLayer.dst_port = new String[]{"9090"};
        fvDimensionLayer.src_port = new String[]{"8989"};
        fvDimensionLayer.eth_dst = new String[]{"11:22:22:33:44:55"};
        fvDimensionLayer.eth_src = new String[]{"11:22:22:33:44:66"};
        fvDimensionLayer.ip_src = new String[]{"192.168.0.122"};
        fvDimensionLayer.frame_cap_len = new String[]{"10"};
        String trailer = "00:03:0d:0d:fc:6b:07:e4:ae:78:63:b0:fc:6b:07:e4:ae:78:64:20";
        fvDimensionLayer.eth_trailer = new String[]{trailer};
        String fcs = "0x00000067";

        fvDimensionLayer.eth_fcs = new String[]{fcs};
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            JSON.toJSONString(fvDimensionLayer);
        }
        System.out.println(System.currentTimeMillis() - time1);
    }
}
