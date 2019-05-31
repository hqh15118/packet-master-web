package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.entity.ArtConfig;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IArtConfigService {
    void deleteByProtocolIdAndMinLength(int protocolId, int minLength);
    List<ArtConfig> getConfigPaged(ArtConfigPaged artConfigPaged);
}
