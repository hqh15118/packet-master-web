package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author hongqianhui
 */
public interface ArtConfigMapper extends BaseMapper<BaseArtConfig> {

    /**
     * 分页获取工艺参数
     * @param pagedArtConfig
     * @return
     */
    BaseResponse getConfigPaged(PagedArtConfig pagedArtConfig);

    /**
     * 获取所有的工艺参数，查所有的表
     * @return
     */
    List<BaseArtConfig> selectAllConfig();

    /**
     * 筛选出所有标记显示的工艺参数，showGraph为1的所有参数配置的tag
     * @return
     */
    List<String> selectAllShowArt();

    /**
     * 根据协议ID和工艺参数ID【主键】删除工艺参数配置
     * @param protocolId
     * @param id
     * @return
     */
    BaseArtConfig delArtConfigByProtocolIdAndId(int protocolId, int id);


    BaseArtConfig getArtConfigByProtocolIdAndId(int protocolId, int id);

    Map<String, List<ArtShowState>> selectAllArtConfigShowState();

    void changeArtConfigShowState(ArtArgShowState artArgShowState);

    BaseResponse updateByJSONStr(String jsonData);

    BaseResponse insertByJSONStr(String jsonData);

    List<ConfigValue> selectArtNameByProtocolName(String protocol);
}
