package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.OptFilter;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IOptFilterService;
import com.zjucsc.application.system.service.hessian_mapper.OptFilterMapper;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.OptFilterUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.zjucsc.application.util.CacheUtil.convertIdToName;

/**
 * @author hongqianhui
 */
@Slf4j
@Service
public class OptFilterServiceImpl extends BaseServiceImpl<OptFilter,OptFilterMapper> implements IOptFilterService {

    @Override
    public CompletableFuture<Exception> addOperationFilter(List<OptFilterForFront> optFilterForFront) {
        this.baseMapper.deleteByDeviceNumber(optFilterForFront.get(0).getDeviceNumber(),Common.GPLOT_ID);
        this.baseMapper.saveOrUpdateBatch(optFilterForFront,Common.GPLOT_ID);
        return null;
    }

    /**
     * 获取某个设备针对某个协议的功能码配置
     * @param deviceNumber 设备
     * @param cached 是否从缓存里面找
     * @param protocolId 协议
     * @return
     * @throws ProtocolIdNotValidException
     */
    @Async("common_async")
    @Override
    public CompletableFuture<List<String>> getTargetExistIdFilter(String deviceNumber, boolean cached , int protocolId) throws ProtocolIdNotValidException {
        if (cached){
            String deviceTag = CacheUtil.getTargetDeviceTagByNumber(deviceNumber);
            ConcurrentHashMap<String, OperationAnalyzer> map = OptFilterUtil.getTargetProtocolToOperationAnalyzerByDeviceTag(deviceTag);
            if (map == null){
                throw new ProtocolIdNotValidException("缓存中不存在ID为 " + deviceNumber + " 的规则");
            }
            final List<String> optFilterList  = new ArrayList<>();
            OperationAnalyzer analyzer = map.get(convertIdToName(protocolId));
            if (analyzer == null || analyzer.getAnalyzer() == null || analyzer.getAnalyzer().getWhiteMap() == null){
                return CompletableFuture.completedFuture(new ArrayList<>(0));
            }
            analyzer.getAnalyzer().getWhiteMap().forEach((integer, s) -> optFilterList.add(integer));
            return CompletableFuture.completedFuture(optFilterList);
        }else{
            return CompletableFuture.completedFuture(selectTargetOptFilter(deviceNumber  , protocolId));
        }
    }

    @Override
    public List<String> selectTargetOptFilter(String device, int protocolId) {
        return this.baseMapper.selectTargetOptFilter(device,protocolId,Common.GPLOT_ID);
    }

    @Override
    public void deleteByDeviceNumber(String deviceNumber) {
        this.baseMapper.deleteByDeviceNumber(deviceNumber,Common.GPLOT_ID);
    }


    @Override
    public void deleteByDeviceNumberAndProtocolId(String deviceNumber, int protocolId) {
        this.baseMapper.deleteByDeviceNumberAndProtocolId(deviceNumber, protocolId,Common.GPLOT_ID);
    }

    @Override
    public void deleteByDeviceNumberAndProtocolIdAndFuncode(String deviceNumber, int protocolId, int funCode) {
        this.baseMapper.deleteByDeviceNumberAndProtocolIdAndFuncode(deviceNumber, protocolId, funCode,Common.GPLOT_ID);
    }
}
