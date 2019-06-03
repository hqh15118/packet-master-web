package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtHistoryForFront;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IArtHistoryDataService {
    /**
     * 保存工艺数据
     * @param artName 工艺参数名字
     * @param artValue 工艺参数值
     * @param payload 解析此条数据的tcp payload
     */
    void saveArtData(String artName, float artValue, byte[] payload);

    /**
     * 查询历史工艺数据
     * @param artName 工艺参数名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param timeType 查询的时间类型，年、月、日、时
     * @return
     */
    CompletableFuture<ArtHistoryForFront> getArtData(String startTime, String endTime, List<String> artNames, String timeType) throws Exception;
}
