package com.zjucsc.application.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 组态配置实体类
 */
@Data
@TableName("configurations")
//数据库中保存的数据格式
/**********************
 * protocol - content
 * ********************
 */
public class ConfigurationSetting {
    //组态对应的协议
    @TableField("protocol")
    private String protocol;
    //组态对应的内容，以json字符串的形式进行存储
    @TableField("content")
    private String content;

    @Data
    public static class ConfigurationContent{
        //0 white 1 black
        @NotBlank
        private int type;
        private String ip;
        private String port;
        //operation
        private int fun_code;
        //工艺参数
        private ArtificialParam artificalParam;
        @Data
        public static class ArtificialParam {
            private String paramMeaning;
            private int min;
            private int max;
        }
    }

    //前端应该传递的数据格式
    @Data
    public static class Configuration{
        private String protocol;
        private List<ConfigurationContent> configurations;
    }

}
