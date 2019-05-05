package com.zjucsc.application.system.service.impl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.filter.FiveDimensionPacketFilter;
import com.zjucsc.application.system.dao.FiveDimensionFilterMapper;
import com.zjucsc.application.system.service.iservice.IFiveDimensionFilterService;
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
public class FiveDimensionFilterServiceImpl extends ServiceImpl<FiveDimensionFilterMapper, FVDimensionFilterEntity> implements IFiveDimensionFilterService {

    /**
     * 添加或者更新
     * @param deviceId
     * @param userName
     * @param fiveDimensionFilters
     * @return
     */
    @Async
    @Override
    public CompletableFuture<Exception> addFvDimensionFilter(int deviceId, String userName, List<FVDimensionFilterEntity.FiveDimensionFilter> fiveDimensionFilters) {
        //修改缓存
        if (Common.FV_DIMENSION_FILTER.get(deviceId) == null){
            //未添加过该设备
            FiveDimensionPacketFilter filter =
                    new FiveDimensionPacketFilter(deviceId + ":" + userName + " : " + FV_DIMENSION);
            filter.setFilterList(fiveDimensionFilters);
            FiveDimensionAnalyzer analyzer = new FiveDimensionAnalyzer(filter);
            filter.setUserName(userName);
            Common.FV_DIMENSION_FILTER.put(deviceId,analyzer);
        }else{
            //已经添加过该设备
            Common.FV_DIMENSION_FILTER.get(deviceId).getAnalyzer().setFilterList(fiveDimensionFilters);
        }
        //修改数据库
        FVDimensionFilterEntity entity = new FVDimensionFilterEntity();
        entity.setDeviceId(deviceId);
        entity.setUser_name(userName);
        entity.setContent(JSON.toJSONString(fiveDimensionFilters));
        saveOrUpdate(entity);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 这个是给前端查看过滤器使用的
     * @param deviceId
     * @return
     */
    @Async
    @Override
    public CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> getTargetExistIdFilter(int deviceId) {
        FiveDimensionAnalyzer analyzer = null;
        FVDimensionFilterEntity.FiveDimensionFilterForFront fiveDimensionFilterForFront = null;
        CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> future = null;
        if ((future = doGetCached(deviceId)) != null){
            return future;
        }else{
            //如果缓存中没有，就请求数据库
            FVDimensionFilterEntity entity = getById(deviceId);
            if (entity==null){
                return CompletableFuture.completedFuture(null);
            }
            fiveDimensionFilterForFront = new FVDimensionFilterEntity.FiveDimensionFilterForFront();
            List<FVDimensionFilterEntity.FiveDimensionFilter> filter_from_db = JSON.parseArray(entity.getContent(), FVDimensionFilterEntity.FiveDimensionFilter.class);
            fiveDimensionFilterForFront.setFiveDimensionFilters(filter_from_db);
            fiveDimensionFilterForFront.setUserName(entity.getUser_name());
            fiveDimensionFilterForFront.setDeviceId(deviceId);
            return CompletableFuture.completedFuture(fiveDimensionFilterForFront);
        }
    }

    /**
     * 获取缓存中存在已经定义的设备
     * @param deviceId
     * @return
     * @throws DeviceNotValidException
     */
    @Override
    public CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> getTargetCachedIdAnalyzer(int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException {
        CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> future = null;
        if ((future = doGetCached(deviceId)) != null){
            return future;
        }else{
            return CompletableFuture.completedFuture(null);
        }
    }

    private CompletableFuture<FVDimensionFilterEntity.FiveDimensionFilterForFront> doGetCached(int deviceId){
        FVDimensionFilterEntity.FiveDimensionFilterForFront fiveDimensionFilterForFront = null;
        FiveDimensionAnalyzer analyzer = null;
        if ((analyzer = Common.FV_DIMENSION_FILTER.get(deviceId))!=null) {
            fiveDimensionFilterForFront = new FVDimensionFilterEntity.FiveDimensionFilterForFront();
            fiveDimensionFilterForFront.setFiveDimensionFilters(analyzer.getAnalyzer().getFilterList());
            fiveDimensionFilterForFront.setUserName(analyzer.getAnalyzer().getUserName());
            fiveDimensionFilterForFront.setDeviceId(deviceId);
            return CompletableFuture.completedFuture(fiveDimensionFilterForFront);
        }else{
            return null;
        }
    }

    @Async
    @Override
    public CompletableFuture<Exception> deleteTargetExistIdAnalyzer(int deviceId) {
        Common.FV_DIMENSION_FILTER.remove(deviceId);
        if (removeById(deviceId)){
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(new DeviceNotValidException("delete " + deviceId + "failed"));
    }

}
