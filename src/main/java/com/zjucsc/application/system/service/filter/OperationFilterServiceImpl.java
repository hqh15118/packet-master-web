package com.zjucsc.application.system.service.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.domain.entity.OperationFilterEntity;
import com.zjucsc.application.domain.exceptions.ConfigurationNotValidException;
import com.zjucsc.application.system.dao.filter.OperationFilterMapper;
import com.zjucsc.application.system.service.UserOptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zjucsc.application.config.Common.BAD_PACKET_FILTER_PRO_1;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 20:34
 */
@Service
//Fun_code filter
public class OperationFilterServiceImpl extends ServiceImpl<OperationFilterMapper, OperationFilterEntity> implements OperationFilterService{

    @Autowired
    private UserOptService userOptService;

    @Async
    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<Exception> configOperationRule(OperationFilterEntity.OperationFilterForFront configuration) {
        if (!userOptService.onServer(configuration.getUserName())){
            return CompletableFuture.completedFuture(new ConfigurationNotValidException("用户未登录或不存在"));
        }
        /*-****************************************************
         * ConfigurationForFront
         * {
         *     userName : xxx   用户名         验证
         *     protocol ： xxx  协议           该过滤器对应的协议如S7、modbus等
         *     [
         *       {
         *          filterType : 0 / 1        过滤类别：0表示白名单，1表示黑名单
         *          fun_code : 操作码          操作码，协议对应的功能码
         *       },
         *       ...
         *     ]
         * }
         *-***************************************************/
        List<OperationFilterEntity.OperationFilter> filters = configuration.getOperationFilters();
        OperationPacketFilter<Integer,String> packetFilter = new OperationPacketFilter<>("operation filter");//功能码
        for (OperationFilterEntity.OperationFilter filter : filters) {
            if (filter.getFilterType() == 0){
                packetFilter.addWhiteRule(filter.getFun_code(),"not support");
                //FIXME 这里暂不支持
//                packetFilter.addWhiteRule(filter.getFun_code(), Common.CONFIGURATION_MAP.get(configuration.getProtocol())
//                        .get(filter.getFun_code()));
            }else{
                packetFilter.addBlackRule(filter.getFun_code(),"not support");
//                packetFilter.addBlackRule(filter.getFun_code(),Common.CONFIGURATION_MAP.get(configuration.getProtocol())
//                        .get(filter.getFun_code()));
            }
        }
        //BAD_PACKET_FILTER.put(FilterType.OPERATION,packetFilter);
        if (!BAD_PACKET_FILTER_PRO_1.containsKey(configuration.getProtocol())){
            return CompletableFuture.completedFuture(new ConfigurationNotValidException("未定义该协议" + configuration.getProtocol()));
        }
        BAD_PACKET_FILTER_PRO_1.get(configuration.getProtocol()).setAnalyzer(packetFilter);
        OperationFilterEntity operationFilterEntity = new OperationFilterEntity();
        operationFilterEntity.setProtocol(configuration.getProtocol());
        operationFilterEntity.setContent(JSON.toJSONString(configuration.getOperationFilters()));
        operationFilterEntity.setUserName(configuration.getUserName());
        saveOrUpdate(operationFilterEntity);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public OperationFilterEntity.OperationFilterForFront loadRule(String userName , String protocol) {
        if (!userOptService.onServer(userName)){
            throw new ConfigurationNotValidException("用户未登录或不存在");
        }
        OperationFilterEntity.OperationFilterForFront filterForFront = new OperationFilterEntity.OperationFilterForFront();
        OperationFilterEntity operationEntry = getById(protocol);
        filterForFront.setOperationFilters(JSON.parseArray(operationEntry.getContent(),OperationFilterEntity.OperationFilter.class));
        filterForFront.setProtocol(operationEntry.getProtocol());
        return filterForFront;
    }

    @Async
    @Override
    public CompletableFuture<List<OperationFilterEntity.OperationFilterForFront>> loadAllRule(int deviceId) {
        List<OperationFilterEntity.OperationFilterForFront> operationFilterForFronts = new ArrayList<>();
        for (OperationFilterEntity entity : list()){
            OperationFilterEntity.OperationFilterForFront filterForFront = new OperationFilterEntity.OperationFilterForFront();
            filterForFront.setProtocol(entity.getProtocol());
            filterForFront.setDeviceId(deviceId);
            filterForFront.setUserName("");
            filterForFront.setDeviceId(entity.getDeviceId());
            filterForFront.setOperationFilters(JSON.parseArray(entity.getContent(), OperationFilterEntity.OperationFilter.class));
            operationFilterForFronts.add(filterForFront);
        }
        return CompletableFuture.completedFuture(operationFilterForFronts);
    }
}
