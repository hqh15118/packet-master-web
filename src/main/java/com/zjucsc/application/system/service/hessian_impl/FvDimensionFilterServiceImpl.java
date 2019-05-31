package com.zjucsc.application.system.service.hessian_impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IFvDimensionFilterService;
import com.zjucsc.application.system.service.hessian_mapper.FvDimensionFilterMapper;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.filter.FiveDimensionPacketFilter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;

/**
 * @author hongqianhui
 */
@Service
public class FvDimensionFilterServiceImpl extends BaseServiceImpl<FvDimensionFilterMapper, FvDimensionFilter> implements IFvDimensionFilterService {

    @Async
    @Override
    public CompletableFuture<Exception> addFvDimensionFilter(List<FvDimensionFilter> fvDimensionFilters) {
        //更新缓存
        String deviceNumber = fvDimensionFilters.get(0).getDeviceNumber();
        String userName = fvDimensionFilters.get(0).getUserName();
        deleteAllFilterByDeviceNumberAndGplotId(deviceNumber,Common.GPLOT_ID);
        if (Common.FV_DIMENSION_FILTER_PRO.get(deviceNumber) == null){
            //未添加过该设备，缓存中没有该分析器，需要新加一个
            FiveDimensionPacketFilter filter =
                    new FiveDimensionPacketFilter(deviceNumber + ":" + userName + " : " + FV_DIMENSION);
            filter.setUserName(userName);
            filter.setFilterList(fvDimensionFilters);
            FiveDimensionAnalyzer analyzer = new FiveDimensionAnalyzer(filter);
            Common.FV_DIMENSION_FILTER_PRO.put(deviceNumber,analyzer);
        }else{
            //已经添加过该设备，缓存中有分析器，需要替换掉该分析器中的过滤器
            Common.FV_DIMENSION_FILTER_PRO.get(deviceNumber).getAnalyzer().setFilterList(fvDimensionFilters);
        }
        //更新数据库
        this.baseMapper.saveOrUpdateBatch(fvDimensionFilters);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<List<FvDimensionFilter>> getTargetExistIdFilter(String deviceId , boolean cached) {
        if (cached){
            return CompletableFuture.completedFuture(Common.FV_DIMENSION_FILTER_PRO.get(deviceId).getAnalyzer().getFilterList());
        }
        List<FvDimensionFilter> list = this.baseMapper.selectByDeviceId(deviceId);
        return CompletableFuture.completedFuture(list);
    }

    @Override
    public void deleteAllFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        this.baseMapper.deleteAllFilterByDeviceNumberAndGplotId(deviceNumber,gplotId);
    }
}
