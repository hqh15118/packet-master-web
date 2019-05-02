package com.zjucsc.application.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjucsc.application.domain.entity.ConfigurationSetting;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ConfigurationMapper extends BaseMapper<ConfigurationSetting> {

    @Results(id = "all_rules", value = {
            //@Result(property = "id", column = "id", id = true),
            @Result(property = "protocol", column = "protocol"),
            @Result(property = "content", column = "configuration_content")})
    @Select({"SELECT * FROM configurations"})
    List<ConfigurationSetting> loadAllRule();
}
