package com.zjucsc.packetmasterweb;


import com.alibaba.fastjson.JSON;
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
}
