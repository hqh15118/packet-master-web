package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.system.entity.Gplot;

/**
 * @author hongqianhui
 */
public interface IGplotService extends IService<Gplot> {
    void addNewGplot(Gplot gplot);


    void changeGplot(int gplotId);
}
