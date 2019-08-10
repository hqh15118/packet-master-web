package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ArtBeanFWrapper;
import com.zjucsc.application.domain.bean.ArtHistoryData;
import com.zjucsc.application.domain.non_hessian.ArtBean;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

public interface ArtHistoryDataMapper extends BaseMapper<ArtHistoryData> {
    /**
     * 保存工艺数据
     * @param artName 工艺参数名字
     * @param artValue 工艺参数值
     * @param payload 解析此条数据的tcp payload
     */
    void saveArtData(String artName, String artValue, byte[] payload, int gplotId);

    /**
     * 查询历史工艺数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param artName 工艺参数名
     * @return
     */
    List<Float> getArtData(String startTime, String endTime, String artName, int gplotId);

    ArtBeanFWrapper selectArtPacketByTimeStamp(ArtBean artBean);
}
