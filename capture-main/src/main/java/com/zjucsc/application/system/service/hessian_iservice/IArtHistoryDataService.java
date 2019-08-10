package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtBeanFWrapper;
import com.zjucsc.application.domain.bean.ArtHistoryForFront;
import com.zjucsc.application.domain.non_hessian.ArtBean;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IArtHistoryDataService {
    /**
     * 保存工艺数据
     * @param artName 工艺参数名字
     * @param artValue 工艺参数值
     * @param payload 解析此条数据的tcp payload
     */
    void saveArtData(String artName, String artValue, byte[] payload);

    /**
     * 查询历史工艺数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    CompletableFuture<ArtHistoryForFront> getArtData(String startTime, String endTime, List<String> artNames) throws Exception;

    CompletableFuture<ArtBeanFWrapper> getAllArtData(ArtBean artBean);
}
