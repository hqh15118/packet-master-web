package com.zjucsc.packetmasterweb.spring;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.controller.ConfigurationSettingController;
import com.zjucsc.application.system.service.iservice.IConfigurationSettingService;
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

    @Autowired private ConfigurationSettingController configurationSettingController;


    @Autowired private IConfigurationSettingService iConfigurationSettingService;

    @Test
    public void getAllConfiguration(){
        //Util.showJSON("getAllConfiguration" , configurationController.get());
    }

    @Test
    public void updateConfiguration() throws ProtocolIdNotValidException {
        List<ConfigurationForFront> forFronts = new ArrayList<>();
        ConfigurationForFront configurationForFront = new ConfigurationForFront();
        configurationForFront.setProtocolId(2);
        ConfigurationForFront.ConfigurationWrapper wrapper = new ConfigurationForFront.ConfigurationWrapper(2 , "cpu server zzz");
        configurationForFront.setConfigurationWrappers(Arrays.asList(
                wrapper
        ));
        forFronts.add(configurationForFront);
        //Util.showJSON("将S7协议功能码为2该为cpu server" , configurationSettingController.updateConfiguration(forFronts));
    }

    @Test
    public void selectCount(){
        ConfigurationForSelectCount configurationForSelect = new ConfigurationForSelectCount();
        configurationForSelect.setCodeDes("下载");
        configurationForSelect.setProtocolId(2);
        Util.showJSON("select count : " , configurationSettingController.getConfigurationSize(configurationForSelect));
    }

    @Test
    public void getAllInfos(){
        System.out.println(configurationSettingController.selectAllProtocolInfo());
    }

    @Test
    public void getLikeRules(){
        ConfigurationForSelect configurationForSelect = new ConfigurationForSelect();
        configurationForSelect.limit = 10;
        configurationForSelect.page = 1;
        configurationForSelect.codeDes = "下载";
        configurationForSelect.protocolId = 2;
        System.out.println(
                JSON.toJSONString(configurationSettingController.selectConfigurationPageInfo(configurationForSelect))
        );
    }

    @Test
    public void deleteConfig() throws InterruptedException, ProtocolIdNotValidException {
        ConfigurationForDelete delete = new ConfigurationForDelete();
        delete.setFunCodes(Arrays.asList(1,2,3));
        delete.setProtocolId(1);
        configurationSettingController.deleteConfiguration(Arrays.asList(delete));
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

    @Test
    public void addNewProtocol() throws ProtocolIdNotValidException {
        ConfigurationForNewProtocol newProtocol = new ConfigurationForNewProtocol();
        newProtocol.setProtocolName("test protocol no fun code");
        newProtocol.setConfigurationWrappers(new ArrayList<>());
        Util.showJSON("test protocol no fun code " , configurationSettingController.addNewProtocol(newProtocol));
        newProtocol.setProtocolName("test protocol with fun code");
        newProtocol.setConfigurationWrappers(Arrays.asList(
                new ConfigurationForFront.ConfigurationWrapper(1,"test 1"),
            new ConfigurationForFront.ConfigurationWrapper(2,"test 2")
        ));
        Util.showJSON("test protocol no fun code " , configurationSettingController.addNewProtocol(newProtocol));
    }
}
