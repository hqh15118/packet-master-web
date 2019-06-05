package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.StatisticInfoSaveBean;
import com.zjucsc.application.system.service.hessian_iservice.IDeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceServiceImplTest {

    @Autowired private IDeviceService iDeviceService;

    @Test
    public void saveStatisticInfo() {
        Map<String, StatisticInfoSaveBean> map = new HashMap<>();
        map.put("str",new StatisticInfoSaveBean());
        iDeviceService.saveStatisticInfo(map);
    }
}