package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.domain.bean.RuleWrapper;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IFvDimensionFilterService;
import com.zjucsc.application.system.service.hessian_iservice.IOptFilterService;
import com.zjucsc.application.system.service.hessian_mapper.FvDimensionFilterMapper;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.FvFilterUtil;
import com.zjucsc.application.util.OptFilterUtil;
import com.zjucsc.common.exceptions.DeviceNotValidException;
import com.zjucsc.common.exceptions.OptFilterNotValidException;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zjucsc.application.util.OptFilterUtil.getOptFilterForFront;

/**
 * @author hongqianhui
 */
@Service
public class FvDimensionFilterServiceImpl extends BaseServiceImpl<Rule,FvDimensionFilterMapper> implements IFvDimensionFilterService {

    @Autowired private IOptFilterService iOptFilterService;

    @SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
    @Override
    public CompletableFuture<Exception> addFvDimensionFilter(List<Rule> rules) throws ProtocolIdNotValidException {
        //更新缓存
        String deviceNumber = rules.get(0).getFvDimensionFilter().getDeviceNumber();
        //删除数据库中的所有旧规则
        deleteAllFilterByDeviceNumberAndGplotId(deviceNumber,Common.GPLOT_ID);
        this.baseMapper.saveOrUpdateBatchRules(rules,Common.GPLOT_ID);//保存五元组
        //保存功能码
        List<OptFilterForFront> optFilterForFronts = new ArrayList<>();
        for (Rule rule : rules) {
            optFilterForFronts.add(createOptFilterForFront(rule));
        }
        try {
            iOptFilterService.addOperationFilter(optFilterForFronts);
        } catch (ProtocolIdNotValidException | OptFilterNotValidException | DeviceNotValidException e) {
            return CompletableFuture.completedFuture(e);
        }
        reAddRules(deviceNumber);
        return CompletableFuture.completedFuture(null);
    }

    private OptFilterForFront createOptFilterForFront(Rule rule){
        return getOptFilterForFront(rule);
    }


    @Override
    public CompletableFuture<List<Rule>> getTargetExistIdFilter(String deviceNumber , boolean cached) {
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
//        String deviceTag = CacheUtil.getTargetDeviceTagByNumber(deviceNumber);
//        if (enable){
//            //使能某条规则
//            FvFilterUtil.addOrUpdateFvFilter(deviceTag, Collections.singletonList(rule),"");//五元组规则
//            OptFilterUtil.addOrUpdateAnalyzer(deviceTag,createOptFilterForFront(rule),"");
//        }else{
//            FvFilterUtil.removeFvFilter(deviceTag,rule);
//            OptFilterUtil.removeTargetDeviceAnalyzeFuncode(createOptFilterForFront(rule));
//            reAddRules(deviceNumber);
//        }
        reAddRules(deviceNumber);
        return rule;
    }

    private void reAddRules(String deviceNumber) throws ProtocolIdNotValidException {
        List<Rule> rules = this.baseMapper.selectByDeviceId(deviceNumber,Common.GPLOT_ID);
        String deviceTag = CacheUtil.getTargetDeviceTagByNumber(deviceNumber);
        FvFilterUtil.addOrUpdateFvFilter(deviceTag, rules,"");//五元组规则
        for (Rule rule : rules) {
            if (rule.isEnable()){
                OptFilterUtil.addOrUpdateAnalyzer(deviceTag,createOptFilterForFront(rule),"");
            }
        }
    }

    @Override
    public void removeRuleByFvIds(List<String> fvIds) {
        RuleWrapper ruleWrapper = this.baseMapper.deleteRuleByFvId(fvIds);
        String deviceNumber = ruleWrapper.getRemovedRules().get(0).getFvDimensionFilter().getDeviceNumber();
        String deviceTag = CacheUtil.getTargetDeviceTagByNumber(deviceNumber);
        if (ruleWrapper.isEmpty()){
            //如果已经移除了该设备的所有规则，那么就禁用该设备的匹配
            FvFilterUtil.disableDeviceAllConfig(deviceTag);
            OptFilterUtil.disableTargetDeviceAnalyzer(deviceTag);
        }else{
            for (Rule rule : ruleWrapper.getRemovedRules()) {
                FvFilterUtil.removeFvFilter(deviceTag,rule);
                try {
                    OptFilterUtil.removeTargetDeviceAnalyzeFuncode(createOptFilterForFront(rule));
                } catch (ProtocolIdNotValidException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
