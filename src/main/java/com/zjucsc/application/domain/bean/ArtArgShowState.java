package com.zjucsc.application.domain.bean;

import com.zjucsc.application.domain.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

public class ArtArgShowState {
    private int protocolId;
    private String artName;
    private boolean showState;
}
