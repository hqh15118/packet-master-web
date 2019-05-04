package com.zjucsc.application.system.service.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.filter.FiveDimensionPacketFilter;
import com.zjucsc.application.domain.entity.FiveDimensionFilterEntity;
import com.zjucsc.application.domain.exceptions.ConfigurationNotValidException;
import com.zjucsc.application.system.dao.filter.FiveDimensionFilterMapper;
import com.zjucsc.application.system.service.UserOptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zjucsc.application.config.Common.BAD_PACKET_FILTER_PRO_1;
import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 20:23
 */
@Service
public class FiveDimensionFilterServiceImpl extends ServiceImpl<FiveDimensionFilterMapper, FiveDimensionFilterEntity> implements FiveDimensionFilterService {


    @Autowired
    private UserOptService userOptService;

    @Async
    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<Exception> configFiveDimensionRule(FiveDimensionFilterEntity.FiveDimensionFilterForFront fiveDimensionFilterForFront) {
        if (!userOptService.onServer(fiveDimensionFilterForFront.getUserName())){
            return CompletableFuture.completedFuture(new ConfigurationNotValidException("用户未登录或不存在"));
        }
        /*-****************************************************
         * FiveDimensionFilterForFront
         * {
         *     userName : xxx   用户名         验证
         *     [
         *       {
         *          filterType : 0 / 1        过滤类别：0表示白名单，1表示黑名单
         *          protocol : 协议           过滤的协议
         *          port ： 端口
         *          src_ip ：
         *          dst_ip ：
         *       },
         *       ...
         *     ]
         * }
         *-***************************************************/
        List<FiveDimensionFilterEntity.FiveDimensionFilter> filters = fiveDimensionFilterForFront.getFiveDimensionFilters();
//        OperationPacketFilter<String,String> packetFilter = new OperationPacketFilter<>("five-dimension filter");//五元组
//        for (FiveDimensionFilterEntity.FiveDimensionFilter filter : filters) {
//            if (filter.getFilterType() == 0){
//                packetFilter.addWhiteRule(filter.getProtocol(),"not support");
//                //FIXME 这里暂不支持
////                packetFilter.addWhiteRule(filter.getFun_code(), Common.CONFIGURATION_MAP.get(configuration.getProtocol())
////                        .get(filter.getFun_code()));
//            }else{
//                packetFilter.addBlackRule(filter.getProtocol(),"not support");
////                packetFilter.addBlackRule(filter.getFun_code(),Common.CONFIGURATION_MAP.get(configuration.getProtocol())
////                        .get(filter.getFun_code()));
//            }
//        }
        //BAD_PACKET_FILTER.put(FilterType.PROTOCOL,packetFilter);
        ((FiveDimensionPacketFilter) (BAD_PACKET_FILTER_PRO_1.get(FV_DIMENSION).getAnalyzer()))
                .setFilterList(filters);
        FiveDimensionFilterEntity fiveDimensionFilterEntity = new FiveDimensionFilterEntity();
        fiveDimensionFilterEntity.setUserName(fiveDimensionFilterForFront.getUserName());
        fiveDimensionFilterEntity.setContent(JSON.toJSONString(fiveDimensionFilterForFront.getFiveDimensionFilters()));
        saveOrUpdate(fiveDimensionFilterEntity);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public List<FiveDimensionFilterEntity.FiveDimensionFilter> loadRule(String userName) {
        return JSON.parseArray(getById(userName).getContent(),FiveDimensionFilterEntity.FiveDimensionFilter.class);
    }
}
