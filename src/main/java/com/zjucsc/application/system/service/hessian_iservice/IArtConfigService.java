package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtConfig;
import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.system.mapper.base.IService;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


/**
 * @author hongqianhui
 */
public interface IArtConfigService extends IService<ArtConfig> {
    void deleteByProtocolIdAndMinLength(int protocolId, int minLength);
    List<ArtConfig> getConfigPaged(ArtConfigPaged artConfigPaged);
    List<ArtConfig> selectAllConfig();
    List<String> selectAllShowArt();
    CompletableFuture<Exception> saveScriptFile(InputStream is,String name);
    boolean scriptExistOrNot(String fileName);
}
