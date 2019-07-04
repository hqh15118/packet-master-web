package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.FvDimensionFilter;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.application.system.mapper.base.BaseMapper;
import org.apache.tomcat.util.digester.Rules;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface FvDimensionFilterMapper extends BaseMapper<Rule> {

    List<Rule> selectByDeviceId(String deviceId, int gplotId);

    void deleteAllFilterByDeviceNumberAndGplotId(String deviceNumber, int gplotId);

    void saveOrUpdateBatch(List<FvDimensionFilter> ruleList, int gplotId);

    void saveWhiteProtocol(String deviceNumber,String protocolName);

    /**
     * 存储五元组规则
     * @param rules 规则List
     * @param gplotId 组态图ID
     */
    void saveOrUpdateBatchRules(List<Rule> rules,int gplotId);
}
