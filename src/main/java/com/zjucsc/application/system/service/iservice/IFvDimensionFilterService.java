package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author hongqianhui
 */
public interface IFvDimensionFilterService extends IService<FvDimensionFilter> {
    /**
     * 向数据库中批量插入
     * @param fvDimensionFilters
     * @return
     */
    CompletableFuture<Exception> addFvDimensionFilter(List<FvDimensionFilter> fvDimensionFilters);
    CompletableFuture<List<FvDimensionFilter>> getTargetExistIdFilter(String deviceId , boolean cached) throws DeviceNotValidException;
    void deleteAllFilterByDeviceNumberAndGplotId(String deviceNumber , int gplotId);
}
