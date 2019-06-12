package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;
import org.junit.Test;

import java.util.concurrent.ConcurrentSkipListSet;

public class BaseConfigTest {


    @Test
    public void compareTest(){
        ConcurrentSkipListSet<BaseConfig> baseConfigs = new ConcurrentSkipListSet<>();
        for (int i = 0; i < 10; i++) {
            S7Config baseConfig = new S7Config();
            baseConfig.setId(i);
            baseConfigs.add(baseConfig);
        }
    }
}