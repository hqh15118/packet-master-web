package com.zjucsc.application.system.service.hessian_mapper;
import com.zjucsc.application.domain.bean.OptFilter;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface OptFilterMapper extends BaseMapper<OptFilter> {

    List<Integer> selectTargetOptFilter(String device, int type,
                                         int protocolId);

    void saveBatch(List<OptFilter> optFilters);

    void deleteByDeviceNumber(String deviceNumber);

    void deleteByDeviceNumberAndProtocolId(String deviceNumber,int protocolId);

    void deleteByDeviceNumberAndPorocolIdAndFuncode(String deviceNumber,int protocolId,int funCode);


}