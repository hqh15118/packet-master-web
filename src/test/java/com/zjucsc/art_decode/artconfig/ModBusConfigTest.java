package com.zjucsc.art_decode.artconfig;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ModBusConfigTest {


    @Test
    public void test(){
        ModBusConfig modBusConfig = new ModBusConfig();
        ModBusConfig modBusConfig1 = new ModBusConfig();
        modBusConfig.setArtName("art1");
        modBusConfig1.setArtName("art2");
        Set<ModBusConfig> modBusConfigs = new HashSet<ModBusConfig>(){
            {
                add(modBusConfig);
                add(modBusConfig1);
            }
        };
        for (ModBusConfig busConfig : modBusConfigs) {
            System.out.println(busConfig);
        }
        System.out.println("--------------------");
        ModBusConfig modBusConfig2 = new ModBusConfig();
        modBusConfig2.setArtName("art1");
        modBusConfig2.setType("art1");
        modBusConfigs.remove(modBusConfig2);
        modBusConfigs.add(modBusConfig2);
        modBusConfigs.add(modBusConfig2);
        for (ModBusConfig busConfig : modBusConfigs) {
            System.out.println(busConfig);
        }
    }
}