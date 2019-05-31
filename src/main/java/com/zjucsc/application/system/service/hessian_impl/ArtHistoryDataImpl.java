package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.ArtHistoryData;
import com.zjucsc.application.system.service.hessian_iservice.IArtHistoryData;
import com.zjucsc.application.system.service.hessian_mapper.ArtHistoryDataMapper;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArtHistoryDataImpl extends BaseServiceImpl<ArtHistoryDataMapper, ArtHistoryData> implements IArtHistoryData {

    @Override
    public void saveArtData(String artName, float artValue, byte[] payload) {
        this.baseMapper.saveArtData(artName, artValue, payload);
    }

    @Override
    public List<ArtHistoryData> getArtData(String startTime, String endTime, String artName, String timeType) {
        return this.baseMapper.getArtData(startTime, endTime, artName, timeType);
    }
}
