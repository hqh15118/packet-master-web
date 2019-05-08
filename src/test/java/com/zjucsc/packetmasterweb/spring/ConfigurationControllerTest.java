package com.zjucsc.packetmasterweb.spring;

import com.zjucsc.application.domain.bean.ConfigurationForDelete;
import com.zjucsc.application.domain.bean.ConfigurationForFront;
import com.zjucsc.application.domain.bean.ConfigurationForSelect;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.controller.ConfigurationController;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import com.zjucsc.packetmasterweb.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationControllerTest {

    @Autowired private ConfigurationController configurationController;


    @Autowired private ConfigurationService configurationService;

    @Test
    public void getAllConfiguration(){
        //Util.showJSON("getAllConfiguration" , configurationController.get());
    }

    @Test
    public void updateConfiguration(){
        List<ConfigurationForFront> forFronts = new ArrayList<>();
        ConfigurationForFront configurationForFront = new ConfigurationForFront();
        configurationForFront.setProtocolId(2);
        ConfigurationForFront.ConfigurationWrapper wrapper = new ConfigurationForFront.ConfigurationWrapper(2 , "cpu server");
        configurationForFront.setConfigurationWrappers(Arrays.asList(
                wrapper
        ));
        forFronts.add(configurationForFront);
        Util.showJSON("getAllConfiguration" , configurationController.updateConfiguration(forFronts));
    }

    @Test
    public void selectCount(){
        ConfigurationForSelect configurationForSelect = new ConfigurationForSelect();
        configurationForSelect.codeDes = "下载";
        System.out.println(configurationService.selectConfigurationCount(configurationForSelect));
        configurationForSelect.page = 1;
        configurationForSelect.limit = 2;
        System.out.println(configurationService.selectConfigurationInfo(configurationForSelect));
    }

    @Test
    public void deleteConfig() throws InterruptedException, ProtocolIdNotValidException {
        ConfigurationForDelete delete = new ConfigurationForDelete();
        delete.setFunCodes(Arrays.asList(1,2,3));
        delete.setProtocolName("s7comm");
        configurationController.deleteConfiguration(Arrays.asList(delete));
        Thread.sleep(1000000);
    }

    @Test
    public void addConfig() throws InterruptedException {
//        ConfigurationForDelete delete = new ConfigurationForDelete();
//        delete.setFunCodes(Arrays.asList(1,2,3));
//        delete.setProtocolName("s7comm");
//        configurationController.deleteConfiguration(Arrays.asList(delete));
//        Thread.sleep(1000000);
    }
}
