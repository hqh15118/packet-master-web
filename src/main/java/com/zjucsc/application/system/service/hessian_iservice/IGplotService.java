package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.Gplot;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;

/**
 * @author hongqianhui
 */
public interface IGplotService {
    void addNewGplot(Gplot gplot);


    void changeGplot(int gplotId) throws ProtocolIdNotValidException;

}
