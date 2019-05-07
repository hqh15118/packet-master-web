package com.zjucsc.application.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.analyzer.OperationAnalyzer;
import com.zjucsc.application.domain.entity.OptFilter;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.system.dao.OptFilterMapper;
import com.zjucsc.application.system.service.iservice.IOptFilterService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @author hongqianhui
 */
@Service
public class OptFilterServiceImpl extends ServiceImpl<OptFilterMapper, OptFilter> implements IOptFilterService {

    /**
     * 每次传的时候，都传功能码那里所有协议下的所有配置
     * 会将该设备ID下的map[协议，分析器]整个替换掉。
     * @param optFilterForFronts
     * @return
     * @throws ProtocolIdNotValidException
     */
    @Transactional
    @Async
    @Override
    public CompletableFuture<Exception> addOperationFilter(List<OptFilter.OptFilterForFront> optFilterForFronts) throws ProtocolIdNotValidException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        int deviceId = -1;
        String userName = optFilterForFronts.get(0).getUserName();
        Map<String, Object> removeMap = null;
        ConcurrentHashMap<String,OperationAnalyzer> analyzerMap = new ConcurrentHashMap<>(); //每一个设备都需要这样一个map，key是协议类型，value是该协议下对应的分析器
        for (OptFilter.OptFilterForFront optFilterForFront : optFilterForFronts) {
            String protocol = Common.PROTOCOL_STR_TO_INT.get(optFilterForFront.getProtocolId());
            if (protocol == null){
                throw new ProtocolIdNotValidException("can not find protocol id : " + optFilterForFront.getProtocolId());
            }
            List<OptFilter> optFilters = optFilterForFront.getOptFilterList(); //该协议下的所有过滤规则
            if (first) {
                deviceId = optFilters.get(0).getDeviceId();
                first = false;
                removeMap = new HashMap<>();
                removeMap.put("device_id" , deviceId);
            }
            if (optFilters.size() == 0){
                continue;
            }
            //更新数据库
            //先删除数据库中已有的配置
            removeByMap(removeMap);
            saveBatch(optFilters);                           //将新的过滤规则保存到数据库

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
            analyzerMap.put(Common.PROTOCOL_STR_TO_INT.get(optFilterForFront.getProtocolId()) , new OperationAnalyzer(operationPacketFilter));
        }
        Common.OPERATION_FILTER.put(deviceId,analyzerMap);  //替换旧的过滤规则
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<List<OptFilter>> getTargetExistIdFilter(int deviceId, int type , boolean cached) throws ProtocolIdNotValidException {
        if (cached){
            ConcurrentHashMap<String, OperationAnalyzer> map = Common.OPERATION_FILTER.get(deviceId);
            if (map == null){
                throw new ProtocolIdNotValidException("缓存中不存在ID为 " + deviceId + " 的规则");
            }
            final List<OptFilter> optFilterList  = new ArrayList<>();
            map.forEach((protocolName, operationAnalyzer) -> {
                if (type == 0){
                    //white list
                    addOptFilter(optFilterList, type , operationAnalyzer.getAnalyzer().getWhiteMap(),
                            deviceId , protocolName);
                }else{
                    addOptFilter(optFilterList, type , operationAnalyzer.getAnalyzer().getBlackMap(),
                            deviceId , protocolName);
                }
            });
            return CompletableFuture.completedFuture(optFilterList);
        }else{
            QueryWrapper<OptFilter> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(OptFilter::getDeviceId , deviceId);
            List<OptFilter> list = list(queryWrapper);
            return CompletableFuture.completedFuture(list);
        }
    }


    private void addOptFilter(List<OptFilter> optFilterList , int type ,
                              HashMap<Integer,String> filterMap , int deviceId ,
                              String protocolName){
        filterMap.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer fun_code, String fun_code_meaning) {
                OptFilter optFilter = new OptFilter();
                optFilter.setDeviceId(deviceId);
                optFilter.setFilterType(type);
                optFilter.setFun_code(fun_code);
                optFilter.setProtocol_id(Common.PROTOCOL_STR_TO_INT.inverse().get(protocolName));
                optFilter.setUser_name("");
                optFilterList.add(optFilter);
            }
        });
    }


}
