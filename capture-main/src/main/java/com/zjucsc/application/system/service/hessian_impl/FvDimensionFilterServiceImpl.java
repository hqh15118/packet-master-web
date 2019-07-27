package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IFvDimensionFilterService;
import com.zjucsc.application.system.service.hessian_iservice.IOptFilterService;
import com.zjucsc.application.system.service.hessian_mapper.FvDimensionFilterMapper;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonFvFilterUtil;
import com.zjucsc.application.util.CommonOptFilterUtil;
import com.zjucsc.common.exceptions.DeviceNotValidException;
import com.zjucsc.common.exceptions.OptFilterNotValidException;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

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
        //删除数据库中的所有旧规则
        deleteAllFilterByDeviceNumberAndGplotId(deviceNumber,Common.GPLOT_ID);
        this.baseMapper.saveOrUpdateBatchRules(rules,Common.GPLOT_ID);//保存五元组
        //保存功能码
        for (Rule rule : rules) {
            try {
                iOptFilterService.addOperationFilter(createOptFilterForFront(rule));
            } catch (ProtocolIdNotValidException | DeviceNotValidException | OptFilterNotValidException e) {
                return CompletableFuture.completedFuture(e);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    private OptFilterForFront createOptFilterForFront(Rule rule){
        OptFilterForFront optFilterForFront = new OptFilterForFront();
        List<Integer> funCodes = rule.getFunCodes();
        optFilterForFront.setDeviceNumber(rule.getFvDimensionFilter().getDeviceNumber());
        optFilterForFront.setFvId(rule.getFvDimensionFilter().getFvId());
        optFilterForFront.setProtocolId(rule.getFvDimensionFilter().getProtocolId());
        optFilterForFront.setFunCodes(funCodes);
        return optFilterForFront;
    }

    @Async
    @Override
    public CompletableFuture<List<Rule>> getTargetExistIdFilter(String deviceNumber , boolean cached) {
        if (cached){
            return CompletableFuture.completedFuture(Common.FV_DIMENSION_FILTER_PRO.get(deviceNumber).getAnalyzer().getFilterList());
        }
        List<Rule> list = this.baseMapper.selectByDeviceId(deviceNumber,Common.GPLOT_ID);
        return CompletableFuture.completedFuture(list);
    }

    @Override
    public void deleteAllFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId) {
        this.baseMapper.deleteAllFilterByDeviceNumberAndGplotId(deviceNumber,gplotId);
    }

    @Override
    public Rule changeRuleStateByDeviceNumberAndFvId(String deviceNumber, String fvId, boolean enable) throws ProtocolIdNotValidException {
        Rule rule = this.baseMapper.changeRuleStateByDeviceNumberAndFvId(deviceNumber, fvId, enable);
        String deviceTag = CommonCacheUtil.getTargetDeviceTagByNumber(deviceNumber);
        if (enable){
            //使能某条规则
            CommonFvFilterUtil.addOrUpdateFvFilter(deviceTag, Collections.singletonList(rule),"");//五元组规则
            CommonOptFilterUtil.addOrUpdateAnalyzer(deviceTag,createOptFilterForFront(rule),"");
        }else{
            CommonFvFilterUtil.removeFvFilter(deviceTag,rule);
            CommonOptFilterUtil.removeTargetDeviceAnalyzeFuncode(createOptFilterForFront(rule));
        }
        return rule;
    }
}
