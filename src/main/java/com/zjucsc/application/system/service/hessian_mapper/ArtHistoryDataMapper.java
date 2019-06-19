package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ArtHistoryData;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;
import java.util.Set;

public interface ArtHistoryDataMapper extends BaseMapper<ArtHistoryData> {
    /**
     * 保存工艺数据
     * @param artName 工艺参数名字
     * @param artValue 工艺参数值
     * @param payload 解析此条数据的tcp payload
     */
    void saveArtData(String artName,String artValue,byte[] payload,int gplotId);

    /**
     * 查询历史工艺数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param artName 工艺参数名
     * @param timeType 查询的时间类型，年、月、日、时
     * @return
     */
    List<String> getArtData(String startTime, String endTime, String artName, int timeType,int gplotId);
}
