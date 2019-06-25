package com.zjucsc.application.domain.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 17:46
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ArtifactFilterEntity  extends BaseResponse implements Serializable {
    private String userName;
    private String content;

    @Data
    public static class ArtifactFilterForFront{
        private String userName;
        private List<ArtifactFilter> artifactFilters;
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
