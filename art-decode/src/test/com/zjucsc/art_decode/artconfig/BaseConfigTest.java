package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.art_decode.s7comm.S7techpara;
import org.junit.Test;

import java.util.concurrent.ConcurrentSkipListSet;

public class BaseConfigTest {


    @Test
    public void compareTest(){
        ConcurrentSkipListSet<BaseConfig> baseConfigs = new ConcurrentSkipListSet<>();
        for (int i = 0; i < 10; i++) {
            S7techpara baseConfig = new S7techpara();
            baseConfig.setId(i);
            baseConfigs.add(baseConfig);
        }
    }
}