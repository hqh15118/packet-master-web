package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtHistoryData;

import java.util.List;

public interface IArtHistoryData {
    /**
     * 保存工艺数据
     * @param artName 工艺参数名字
     * @param artValue 工艺参数值
     * @param payload 解析此条数据的tcp payload
     */
    void saveArtData(String artName, float artValue, byte[] payload);

    /**
     * 查询历史工艺数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param artName 工艺参数名
     * @param timeType 查询的时间类型，年、月、日、时
     * @return
     */
    List<ArtHistoryData> getArtData(String startTime, String endTime, String artName, String timeType);
}
