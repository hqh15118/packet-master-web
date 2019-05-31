package com.zjucsc.application.domain.bean;

import com.zjucsc.application.domain.BaseBean;
import lombok.Data;

@Data
public class ArtConfigPaged extends BaseBean {
    private int protocolId;
    private int minLength;
    private int page;
    private int limit;
}
