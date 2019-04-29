package com.zjucsc.packetmasterweb;


import com.alibaba.fastjson.JSON;
import com.zjucsc.application.util.PacketDecodeUtil;
import org.junit.Test;

import java.util.List;

public class IpTest {

    @Test
    public void IPLengthTest(){

    }

    @Test
    public void arr_json(){
        String[] strings = {"1","2","3","4"};
        System.out.println(JSON.toJSONString(strings));
    }

    //31 个字节数据
    private String s[] = {"03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20"
    ,"da:04:00:00:00:05:01:01:02:17:01"
    ,"da:04:00:00:00:06:01:01:00:00:00:0a"
    ,"d9:04:00:00:00:05:01:02:02:8a:06"
    };

    //excellent performance
    @Test
    public void stringToByteArr(){
        long time1 = System.currentTimeMillis();
        byte[] bytes;
        int kkk = 0;
        for (int i = 0; i < 100000; i++) {
            for (String s1 : s) {
                PacketDecodeUtil.hexStringToByteArray(s1);
                kkk ++;
            }
        }
        System.out.println(kkk);
        System.out.println(System.currentTimeMillis() - time1);
    }
}
