package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.bean.Gplot;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.mapper.base.IService;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IGplotService extends IService<Gplot> {
    BaseResponse addNewGplot(Gplot gplot);


    BaseResponse changeGplot(int gplotId) throws ProtocolIdNotValidException;

    List<Gplot> selectAll();
}
