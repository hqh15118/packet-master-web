package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.attack.bean.DosConfig;

import java.util.List;

public interface DosConfigMapper {
    void addOrUpdateDosConfig(DosConfig dosConfig);
    DosConfig removeDosConfig(DosConfig dosConfig);
    List<DosConfig> selectDosConfigByDeviceNumber(String deviceNumber);
}
