package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.ArtGroup;
import com.zjucsc.application.system.service.hessian_iservice.IArtGroupService;
import com.zjucsc.application.system.service.hessian_mapper.ArtGroupMapper;
import com.zjucsc.application.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArtGroupServiceImpl implements IArtGroupService {

    @Autowired private ArtGroupMapper artGroupMapper;

    @Override
    public void insert(ArtGroup artGroup) {
        artGroupMapper.insert(artGroup);
    }

    @Override
    public void update(ArtGroup artGroup) {
        artGroupMapper.update(artGroup);
    }

    @Override
    public List<String> delete(String artGroup) {
        List<String> defaultGroupArtName = artGroupMapper.delete(artGroup);
        CacheUtil.removeArtGroup(defaultGroupArtName);
        return defaultGroupArtName;
    }

    @Override
    public List<ArtGroup> selectBatch() {
        return artGroupMapper.selectBatch();
    }
}
