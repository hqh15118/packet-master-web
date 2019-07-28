package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class TaskOverFlowError {
    private String poolName;
    private String msg;

    public TaskOverFlowError(String poolName, String msg) {
        this.poolName = poolName;
        this.msg = msg;
    }
}
