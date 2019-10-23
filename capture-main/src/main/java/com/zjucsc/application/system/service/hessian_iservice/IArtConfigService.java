package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.mapper.base.IService;
import com.zjucsc.art_decode.base.BaseConfig;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * @author hongqianhui
 */
public interface IArtConfigService extends IService<BaseArtConfig> {
    BaseResponse getConfigPaged(PagedArtConfig pagedArtConfig);
    List<BaseArtConfig> selectAllConfig();
    List<String> selectAllShowArt();
    CompletableFuture<Exception> saveScriptFile(InputStream is, String name);
    boolean scriptExistOrNot(String fileName);
    BaseArtConfig delArtConfigByProtocolIdAndId(int protocolId, int id);
    BaseArtConfig getArtConfigByProtocolIdAndId(int protocolId, int id);
    Map<String,List<ArtShowState>> selectAllArtConfigShowState();
    void changeArtConfigShowState(ArtArgShowState artArgShowState);
    BaseResponse updateByJSONStr(String jsonData);
    BaseResponse insertByJSONStr(String jsonData);
    List<ConfigValue> selectArtNameByProtocolName(String protocol);

    BaseResponse setArtConfig(int protocolId, String jsonData, BaseConfig baseConfig);
}
