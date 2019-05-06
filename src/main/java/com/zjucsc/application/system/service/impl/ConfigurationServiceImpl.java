package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.dao.ConfigurationMapper;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import com.zjucsc.application.system.service.filter.FiveDimensionFilterService;
import com.zjucsc.application.system.service.filter.OperationFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;


@Slf4j
@Service("configurationSettingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, OperationFilterEntity> implements ConfigurationService {

    @Autowired private OperationFilterService operationFilterService;
    @Autowired private FiveDimensionFilterService fiveDimensionFilterService;

    @Override
    public HashMap<String, Object> loadRule(int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException {
        CompletableFuture<List<OperationFilterEntity.OperationFilterForFront>> future =
                operationFilterService.loadAllRule(deviceId);
        HashMap<String,Object> map = new HashMap<>();
        List<OperationFilterEntity.OperationFilterForFront> list = future.get();
        for (OperationFilterEntity.OperationFilterForFront filterForFront : list) {
            map.put(filterForFront.getProtocol(),filterForFront.getOperationFilters());
        }
        List<FVDimensionFilterEntity.FiveDimensionFilter> list1 = fiveDimensionFilterService.loadRule(deviceId);
        map.put(FV_DIMENSION,list1);
        return map;
    }
}
