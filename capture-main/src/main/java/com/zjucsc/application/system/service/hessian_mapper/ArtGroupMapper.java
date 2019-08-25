package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.ArtGroup;

import java.util.List;

public interface ArtGroupMapper {
    void insert(ArtGroup artGroup);
    void update(ArtGroup artGroup);
    List<String> delete(String artGroup);
    List<ArtGroup> selectBatch();
}
