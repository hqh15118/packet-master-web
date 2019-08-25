package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class CommandWrapper {
    private String srcDevice;
    private String dstDevice;
    private String timeStamp;
    private String command;
    private String srcTag;
    private String dstTag;
}
