package com.zjucsc.application;


import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.service.hessian_mapper.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * hessian mapper interface test
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class HessianConfigTest {

    @Autowired private ArtConfigMapper artConfigMapper;
    @Autowired private ArtHistoryDataMapper artHistoryDataMapper;
    @Autowired private ConfigurationSettingMapper configurationSettingMapper;
    @Autowired private DeviceMapper deviceMapper;
    @Autowired private FvDimensionFilterMapper fvDimensionFilterMapper;
    @Autowired private GplotMapper gplotMapper;
    @Autowired private OptFilterMapper optFilterMapper;
    @Autowired private ProtocolIdMapper protocolIdMapper;
    @Autowired private UserOptMapper userOptMapper;

    @Test
    public void ArtConfigMapperTest(){
        System.out.println(artConfigMapper.getById(1));
    }

    @Test
    public void ArtHistoryDataMapperTest(){
        System.out.println(artHistoryDataMapper.getById(1));
    }

    @Test
    public void ConfigurationSettingMapperTest(){
        System.out.println(configurationSettingMapper.getById(1));
    }

    @Test
    public void DeviceMapperTest(){
        System.out.println(deviceMapper.getById(1));
    }

    @Test
    public void FvDimensionFilterMapper(){
        System.out.println(fvDimensionFilterMapper.selectByDeviceId("asdfasdf", Common.GPLOT_ID));
    }

    @Test
    public void GplotMapperTest(){
        System.out.println(gplotMapper.getById(1));
    }

    @Test
    public void OptFilterMapperTest(){
        System.out.println(optFilterMapper.getById(1));
    }

    @Test
    public void ProtocolIdMapperTest(){
        System.out.println(protocolIdMapper.getById(1));
    }

    @Test
    public void UserOptMapperTest(){
        System.out.println(userOptMapper.getById("1"));
    }
}
