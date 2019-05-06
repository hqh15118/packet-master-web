package com.zjucsc.application.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.analyzer.OperationAnalyzer;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.system.dao.OperationFilterMapper;
import com.zjucsc.application.system.service.iservice.IOperationFilterService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


/**
 * @author hongqianhui
 */
@Service
public class OperationFilterServiceImpl extends ServiceImpl<OperationFilterMapper, OperationFilterEntity> implements IOperationFilterService {

    /*-****************************************************
     * ConfigurationForFront
     * {
     *     userName : xxx   用户名         验证
     *     device_id : xxx  设备id
     *     [
     *       {  //hash map
     *       protocol ： xxx  协议           该过滤器对应的协议如S7、modbus等
     *          [{
     *              filterType : 0 / 1        过滤类别：0表示白名单，1表示黑名单
     *              fun_code : 操作码          操作码，协议对应的功能码
     *           },
     *          ...
     *          ]
     *       }
     *     ]
     * }
     *-***************************************************/
    @Async
    @Override
    public CompletableFuture<Exception> addOperationFilter(OperationFilterEntity.OperationFilterForFront forFront) throws ProtocolIdNotValidException {
        //修改缓存
        int deviceId = forFront.getDeviceId();
        String userName = forFront.getUserName();
        HashMap<Integer, List<OperationFilterEntity.OperationFilter>> filterMap = forFront.getProtocolToFilterList();
        StringBuilder sb = new StringBuilder();
        ConcurrentHashMap<String,OperationAnalyzer> analyzerMap = null;
        if ((analyzerMap = Common.OPERATION_FILTER.get(deviceId)) == null){ // if ==null --> 未添加过该设备
            analyzerMap = getNewFilterMap();    //每一个设备都需要这样一个map，key是协议类型，value是该协议下对应的分析器
            /*
             * 遍历传递的每一个protocolId
             */
            for (int protocolId : filterMap.keySet()){
                sb.delete(0 , sb.length());
                String protocolName = Common.PROTOCOL_STR_TO_INT.get(protocolId);   //获取该协议ID对应的协议名称字符串
                if (protocolName == null){
                    throw new ProtocolIdNotValidException("没有发现指定ID对应的协议 : " + protocolId);
                    //如果发现有错误格式的协议id，就直接返回，不存储
                }
                OperationPacketFilter<Integer,String> operationPacketFilter = new OperationPacketFilter<>
                        (sb.append(deviceId).append(" : ").append(userName).append(protocolName).toString());//该协议对应的分析器的报文过滤器
                //遍历该协议id下的所有filter[filterType,fun_code]，设置报文过滤器
                for (OperationFilterEntity.OperationFilter filter : forFront.getProtocolToFilterList().get(protocolId)) {
                    if (filter.getFilterType() == 0){
                        //白名单
                        operationPacketFilter.addWhiteRule(filter.getFun_code(),
                                Common.CONFIGURATION_MAP.get(protocolName).get(filter.getFun_code()));//添加功能码以及该功能码对应的含义
                    }else{
                        //黑名单
                        operationPacketFilter.addBlackRule(filter.getFun_code(),
                                Common.CONFIGURATION_MAP.get(protocolName).get(filter.getFun_code()));//添加功能码以及该功能码对应的含义
                    }
                }
                analyzerMap.put(Common.PROTOCOL_STR_TO_INT.get(protocolId) , new OperationAnalyzer(operationPacketFilter));

                Common.OPERATION_FILTER.put(deviceId,analyzerMap);
                //filterSet.add(analyzerMap);
            }

        }else{
            //已经添加过该设备
            for (int protocolId : filterMap.keySet()) {
                sb.delete(0 , sb.length());
                String protocolName = Common.PROTOCOL_STR_TO_INT.get(protocolId);   //获取该协议ID对应的协议名称字符串
                if (protocolName == null){
                    throw new ProtocolIdNotValidException("没有发现指定ID对应的协议 : " + protocolId);
                }
                OperationPacketFilter<Integer,String> operationPacketFilter = new OperationPacketFilter<>
                        (sb.append(deviceId).append(" : ").append(userName).append(protocolName).toString());//该协议对应的分析器的报文过滤器
                for (OperationFilterEntity.OperationFilter filter : forFront.getProtocolToFilterList().get(protocolId)) {
                    if (filter.getFilterType() == 0){
                        //白名单
                        operationPacketFilter.addWhiteRule(filter.getFun_code(),
                                Common.CONFIGURATION_MAP.get(protocolName).get(filter.getFun_code()));//添加功能码以及该功能码对应的含义
                    }else{
                        //黑名单
                        operationPacketFilter.addBlackRule(filter.getFun_code(),
                                Common.CONFIGURATION_MAP.get(protocolName).get(filter.getFun_code()));//添加功能码以及该功能码对应的含义
                    }
                }
                analyzerMap.get(protocolName).setAnalyzer(operationPacketFilter);  //直接替换该协议对应的分析器
            }
        }
        //修改数据库
        OperationFilterEntity entity = new OperationFilterEntity();
        entity.setUserName(userName);
        entity.setDeviceId(deviceId);
        entity.setContent(JSON.toJSONString(forFront.getProtocolToFilterList()));
        saveOrUpdate(entity);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Object> getTargetExistIdFilter(int deviceId) {
        if (Common.OPERATION_FILTER.get(deviceId) != null){
            //缓存中有
            return CompletableFuture.completedFuture(Common.OPERATION_FILTER.get(deviceId));
        }else{
            //如果缓存中没有，就请求数据库
            OperationFilterEntity entity = getById(deviceId);
            if (entity==null){
                return CompletableFuture.completedFuture(null);
            }
            HashMap filter_from_db = JSON.parseObject(entity.getContent(), HashMap.class);
            return CompletableFuture.completedFuture(filter_from_db);
        }
    }

    @Async
    @Override
    public CompletableFuture<Object> getTargetCachedIdAnalyzer(int deviceId) throws DeviceNotValidException, ExecutionException, InterruptedException {
        return CompletableFuture.completedFuture(Common.OPERATION_FILTER.get(deviceId));
    }


    private ConcurrentHashMap<String, OperationAnalyzer> getNewFilterMap(){
        return  new ConcurrentHashMap<String, OperationAnalyzer>(5){
            {
            }
        };
    }
}
