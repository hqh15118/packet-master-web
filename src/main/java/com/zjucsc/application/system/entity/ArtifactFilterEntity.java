package com.zjucsc.application.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 17:46
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("artifact_filter")
public class ArtifactFilterEntity {
    @TableId("user_name")
    private String userName;
    @NotBlank(message = "过滤器种类不能为空")
    @TableField("content")
    @NotBlank(message = "组态内容不能为空")
    private String content;

    @Data
    public static class ArtifactFilterForFront{
        private String userName;
        private List<ArtifactFilterEntity.ArtifactFilter> artifactFilters;
    }

    @Data
    public static class ArtifactFilter{
        //白名单还是黑名单
        private int filterType;
        private String protocol;
        private String artifactName;
        private int min;
        private int max;
    }
}
