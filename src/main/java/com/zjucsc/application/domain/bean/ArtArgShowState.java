package com.zjucsc.application.domain.bean;

import com.zjucsc.application.domain.BaseBean;
import lombok.Data;

@Data
public class ArtArgShowState extends BaseBean {
    private int protocolId;
    private String artName;
    private boolean showState;
}
