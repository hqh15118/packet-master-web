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
        CacheUtil.addOrUpdateArtName2ArtGroup(artGroup.getArtName(),artGroup.getGroup());
    }

    @Override
    public void update(ArtGroup artGroup) {
        artGroupMapper.update(artGroup);
        CacheUtil.addOrUpdateArtName2ArtGroup(artGroup.getArtName(),artGroup.getGroup());
    }

    @Override
    public List<String> delete(String artGroup) {
        List<String> defalutGroupArtName = artGroupMapper.delete(artGroup);
        CacheUtil.removeArtGroup(defalutGroupArtName);
        return defalutGroupArtName;
    }

    @Override
    public List<ArtGroup> selectBatch() {
        return artGroupMapper.selectBatch();
    }
}
