package com.zjucsc.application.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.domain.bean.ConfigurationForSelect;
import com.zjucsc.application.system.entity.Configuration;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigurationMapper extends BaseMapper<Configuration> {
    List<Configuration> selectPageInfo(@Param("codeDes") String codeDes , @Param("startIndex") int startIndex ,
                                       @Param("endIndex") int endIndex);
    int selectCount(@Param("configurationForSelect") ConfigurationForSelect configurationForSelect);
}
