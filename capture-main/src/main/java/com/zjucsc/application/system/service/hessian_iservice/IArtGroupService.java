package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ArtGroup;

import java.util.List;

public interface IArtGroupService {
    void insert(ArtGroup artGroup);
    void update(ArtGroup artGroup);
    //删除该组别后，将原先属于该组别的所有工艺参数所属组别全部修改为defalut,
    //返回被修改为defalut的工艺参数名字
    List<String> delete(String artGroup);
    List<ArtGroup> selectBatch();
}
