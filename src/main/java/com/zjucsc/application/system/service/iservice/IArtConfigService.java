package com.zjucsc.application.system.service.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.entity.ArtConfig;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IArtConfigService extends IService<ArtConfig> {
    void deleteByProtocolIdAndMinLength(int protocolId,int minLength);
    List<ArtConfig> getConfigPaged(ArtConfigPaged artConfigPaged);
}
