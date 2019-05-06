package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.dao.ConfigurationMapper;
import com.zjucsc.application.system.service.iservice.ConfigurationService;
import com.zjucsc.application.system.service.iservice.IFiveDimensionFilterService;
import com.zjucsc.application.system.service.iservice.IOperationFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;


@Slf4j
@Service("configurationSettingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, OperationFilterEntity> implements ConfigurationService {

    //@Autowired private OperationFilterService operationFilterService;
    //@Autowired private FiveDimensionFilterService fiveDimensionFilterService;
    @Autowired private IOperationFilterService iOperationFilterService;
    @Autowired private IFiveDimensionFilterService iFiveDimensionFilterService;

    @Async
    @Override
    public CompletableFuture<HashMap<String, Object>> loadRule(int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException {
        HashMap<String,Object> map = new HashMap<>();
        map.put(FV_DIMENSION,iFiveDimensionFilterService.getTargetCachedIdAnalyzer(deviceId).get());
        map.put("operation filter" , iOperationFilterService.getTargetCachedIdAnalyzer(deviceId).get());
        return CompletableFuture.completedFuture(map);
    }

    @Override
    public CompletableFuture<HashMap<String, Object>> loadRuleAll(int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException {
        HashMap<String,Object> map = new HashMap<>();
        map.put(FV_DIMENSION,iFiveDimensionFilterService.getTargetExistIdFilter(deviceId).get());
        map.put("operation filter" , iOperationFilterService.getTargetExistIdFilter(deviceId).get());
        return CompletableFuture.completedFuture(map);
    }
}
