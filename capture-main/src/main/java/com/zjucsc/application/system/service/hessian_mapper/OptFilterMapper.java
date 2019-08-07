package com.zjucsc.application.system.service.hessian_mapper;
import com.zjucsc.application.domain.bean.OptFilter;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface OptFilterMapper extends BaseMapper<OptFilter> {

        List<String> selectTargetOptFilter(String device, int protocolId, int gplotId);

        void saveOrUpdateBatch(OptFilterForFront optFilterForFront, int gplotId);

        void deleteByDeviceNumber(String deviceNumber, int gplotId);

        void deleteByDeviceNumberAndProtocolId(String deviceNumber, int protocolId, int gplotId);

        void deleteByDeviceNumberAndProtocolIdAndFuncode(String deviceNumber, int protocolId, int funCode, int gplotId);


}