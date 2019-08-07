package com.zjucsc.application.system.service.common_iservice;

import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CapturePacketService<S,E> {
    CompletableFuture<Exception> start(ProcessCallback<S, E> callback);
    CompletableFuture<Exception> stop();
    void setNewFvDimensionCallback(NewFvDimensionCallback newFvDimensionCallback);

    /**
     * 开始仿真
     * @return 异常
     */
    CompletableFuture<Exception> startSimulate();

    /**
     * 结束仿真
     * @return 异常
     */
    CompletableFuture<Exception> stopSimulate();

    Map<String,Integer> load();

}
