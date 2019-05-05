package com.zjucsc.application.system.service.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.filter.FiveDimensionPacketFilter;
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
public class FiveDimensionFilterServiceImpl extends ServiceImpl<FiveDimensionFilterMapper, FVDimensionFilterEntity> implements FiveDimensionFilterService {


    @Autowired
    private UserOptService userOptService;

    @Async
    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<Exception> configFiveDimensionRule(FVDimensionFilterEntity.FiveDimensionFilterForFront fiveDimensionFilterForFront) {
        if (!userOptService.onServer(fiveDimensionFilterForFront.getUserName())){
            return CompletableFuture.completedFuture(new ConfigurationNotValidException("用户未登录或不存在"));
        }

        /*-****************************************************
         * FiveDimensionFilterForFront
         * {
         *     device_id:xxx    设备ID         P_K
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
        List<FVDimensionFilterEntity.FiveDimensionFilter> filters = fiveDimensionFilterForFront.getFiveDimensionFilters();
        //修改缓存
        ((FiveDimensionPacketFilter) (BAD_PACKET_FILTER_PRO_1.get(FV_DIMENSION).getAnalyzer()))
                .setFilterList(filters);//setFilterList，将前端传入的PROTOCOL_ID转换为PROTOCOL字符串
        //修改数据库
        FVDimensionFilterEntity FVDimensionFilterEntity = new FVDimensionFilterEntity();
        FVDimensionFilterEntity.setDeviceId(fiveDimensionFilterForFront.getDeviceId());
        FVDimensionFilterEntity.setUser_name(fiveDimensionFilterForFront.getUserName());
        FVDimensionFilterEntity.setContent(JSON.toJSONString(fiveDimensionFilterForFront.getFiveDimensionFilters()));
        saveOrUpdate(FVDimensionFilterEntity);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public List<FVDimensionFilterEntity.FiveDimensionFilter> loadRule(int deviceId) throws DeviceNotValidException {
        FVDimensionFilterEntity entity = getById(deviceId);
        if (entity == null){
            throw new DeviceNotValidException("未配置设备ID : " + deviceId);
        }else {
            return JSON.parseArray(entity.getContent(), FVDimensionFilterEntity.FiveDimensionFilter.class);
        }
    }
}
