package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.FvDimensionFilter;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IFvDimensionFilterService;
import com.zjucsc.application.system.service.hessian_iservice.IOptFilterService;
import com.zjucsc.application.system.service.hessian_mapper.FvDimensionFilterMapper;
import com.zjucsc.application.tshark.analyzer.FiveDimensionAnalyzer;
import com.zjucsc.application.tshark.filter.FiveDimensionPacketFilter;
import com.zjucsc.common.exceptions.DeviceNotValidException;
import com.zjucsc.common.exceptions.OptFilterNotValidException;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;

/**
 * @author hongqianhui
 */
@Service
public class FvDimensionFilterServiceImpl extends BaseServiceImpl<Rule,FvDimensionFilterMapper> implements IFvDimensionFilterService {

    @Autowired
    private IOptFilterService iOptFilterService;

    @Async
    @Override
    public CompletableFuture<Exception> addFvDimensionFilter(List<Rule> rules) {
        //更新缓存
        String deviceNumber = rules.get(0).getFvDimensionFilter().getDeviceNumber();
        deleteAllFilterByDeviceNumberAndGplotId(deviceNumber,Common.GPLOT_ID);
        if (Common.FV_DIMENSION_FILTER_PRO.get(deviceNumber) == null){
            //未添加过该设备，缓存中没有该分析器，需要新加一个
            FiveDimensionPacketFilter filter =
                    new FiveDimensionPacketFilter(deviceNumber + " : " + FV_DIMENSION);
            filter.setFilterList(rules);
            FiveDimensionAnalyzer analyzer = new FiveDimensionAnalyzer(filter);
            Common.FV_DIMENSION_FILTER_PRO.put(deviceNumber,analyzer);
        }else{
            //已经添加过该设备，缓存中有分析器，需要替换掉该分析器中的过滤器
            Common.FV_DIMENSION_FILTER_PRO.get(deviceNumber).getAnalyzer().setFilterList(rules);
        }
        //更新数据库
        List<FvDimensionFilter> list = new ArrayList<>();
        for (Rule rule : rules) {
            list.add(rule.getFvDimensionFilter());
        }
        this.baseMapper.saveOrUpdateBatch(list,Common.GPLOT_ID);//保存五元组

        //保存功能码
        OptFilterForFront optFilterForFront = new OptFilterForFront();
        for (Rule rule : rules) {
            List<Integer> funCodes = rule.getFunCodes();
            optFilterForFront.setDeviceNumber(rule.getFvDimensionFilter().getDeviceNumber());
            optFilterForFront.setFvId(rule.getFvDimensionFilter().getFvId());
            optFilterForFront.setProtocolId(rule.getFvDimensionFilter().getProtocolId());
            optFilterForFront.setFunCodes(funCodes);
            try {
                iOptFilterService.addOperationFilter(optFilterForFront);
            } catch (ProtocolIdNotValidException | DeviceNotValidException | OptFilterNotValidException e) {
                return CompletableFuture.completedFuture(e);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<List<Rule>> getTargetExistIdFilter(String deviceId , boolean cached) {
        if (cached){
            return CompletableFuture.completedFuture(Common.FV_DIMENSION_FILTER_PRO.get(deviceId).getAnalyzer().getFilterList());
        }
        List<Rule> list = this.baseMapper.selectByDeviceId(deviceId,Common.GPLOT_ID);
        return CompletableFuture.completedFuture(list);
    }

    @Override
    public void deleteAllFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        this.baseMapper.deleteAllFilterByDeviceNumberAndGplotId(deviceNumber,gplotId);
    }
}
