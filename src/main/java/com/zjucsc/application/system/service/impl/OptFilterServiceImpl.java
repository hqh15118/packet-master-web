package com.zjucsc.application.system.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.analyzer.OperationAnalyzer;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.entity.OptFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.system.dao.OptFilterMapper;
import com.zjucsc.application.system.service.iservice.IOptFilterService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongqianhui
 */
@Service
public class OptFilterServiceImpl extends ServiceImpl<OptFilterMapper, OptFilter> implements IOptFilterService {

    @Async
    @Override
    public CompletableFuture<Exception> addOperationFilter(HashMap<Integer,List<OptFilter>> optFiltersMap) throws ProtocolIdNotValidException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        int deviceId = -1;
        String userName = "";
        ConcurrentHashMap<String,OperationAnalyzer> analyzerMap = new ConcurrentHashMap<>(); //每一个设备都需要这样一个map，key是协议类型，value是该协议下对应的分析器
        for (int protocolId : optFiltersMap.keySet()){
            String protocol = Common.PROTOCOL_STR_TO_INT.get(protocolId);
            if (protocol == null){
                throw new ProtocolIdNotValidException("can not find protocol id : " + protocolId);
            }
            List<OptFilter> optFilters = optFiltersMap.get(protocolId); //该协议下的所有过滤规则
            if (optFilters.size() == 0){
                continue;
            }
            //更新数据库
            saveOrUpdateBatch(optFilters);
            if (first) {
                 deviceId = optFilters.get(0).getDeviceId();
                 userName = optFilters.get(0).getUser_name();
                first = false;
            }
            OperationPacketFilter<Integer,String> operationPacketFilter = new OperationPacketFilter<>
                    (sb.append(deviceId).append(" : ").append(userName).append(protocol).toString());//该协议对应的分析器的报文过滤器

            for (OptFilter filter : optFilters) {
                if (filter.getFilterType() == 0){
                    //白名单
                    operationPacketFilter.addWhiteRule(filter.getFun_code(),
                            Common.CONFIGURATION_MAP.get(protocol).get(filter.getFun_code()));//添加功能码以及该功能码对应的含义
                }else{
                    //黑名单
                    operationPacketFilter.addBlackRule(filter.getFun_code(),
                            Common.CONFIGURATION_MAP.get(protocol).get(filter.getFun_code()));//添加功能码以及该功能码对应的含义
                }
            }
            analyzerMap.put(Common.PROTOCOL_STR_TO_INT.get(protocolId) , new OperationAnalyzer(operationPacketFilter));
        }
        Common.OPERATION_FILTER.put(deviceId,analyzerMap);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<OptFilter>> getTargetExistIdFilter(int deviceId, int type , boolean cached) throws DeviceNotValidException {
        if (cached){
            ConcurrentHashMap<String, OperationAnalyzer> map = Common.OPERATION_FILTER.get(deviceId);

        }
        return null;
    }
}
