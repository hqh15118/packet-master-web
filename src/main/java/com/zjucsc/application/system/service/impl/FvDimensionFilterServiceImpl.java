package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
import com.zjucsc.application.domain.entity.FvDimensionFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.filter.FiveDimensionPacketFilter;
import com.zjucsc.application.system.dao.FvDimensionFilterMapper;
import com.zjucsc.application.system.service.iservice.IFvDimensionFilterService;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;

/**
 * @author hongqianhui
 */
@Service
public class FvDimensionFilterServiceImpl extends ServiceImpl<FvDimensionFilterMapper, FvDimensionFilter> implements IFvDimensionFilterService {

    @Async
    @Override
    public CompletableFuture<Exception> addFvDimensionFilter(List<FvDimensionFilter> fvDimensionFilters) {
        //更新缓存
        int deviceId = fvDimensionFilters.get(0).getDeviceId();
        String userName = fvDimensionFilters.get(0).getUser_name();
        if (Common.FV_DIMENSION_FILTER.get(deviceId) == null){
            //未添加过该设备，缓存中没有该分析器，需要新加一个
            FiveDimensionPacketFilter filter =
                    new FiveDimensionPacketFilter(deviceId + ":" + userName + " : " + FV_DIMENSION);
            filter.setUserName(userName);
            filter.setFilterList(fvDimensionFilters);
            FiveDimensionAnalyzer analyzer = new FiveDimensionAnalyzer(filter);
            Common.FV_DIMENSION_FILTER.put(deviceId,analyzer);
        }else{
            //已经添加过该设备，缓存中有分析器，需要替换掉该分析器中的filter
            Common.FV_DIMENSION_FILTER.get(deviceId).getAnalyzer().setFilterList(fvDimensionFilters);
        }
        //更新数据库
        saveOrUpdateBatch(fvDimensionFilters);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<List<FvDimensionFilter>> getTargetExistIdFilter(int deviceId , boolean cached) {
        if (cached){
            return CompletableFuture.completedFuture(Common.FV_DIMENSION_FILTER.get(deviceId).getAnalyzer().getFilterList());
        }
        QueryWrapper<FvDimensionFilter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FvDimensionFilter::getDeviceId , deviceId);
        List<FvDimensionFilter> list = list(queryWrapper);
        return CompletableFuture.completedFuture(list);
    }
}
