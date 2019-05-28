package com.zjucsc.application.system.service.iservice;

import com.zjucsc.application.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.application.tshark.capture.ProcessCallback;

import java.util.concurrent.CompletableFuture;

public interface CapturePacketService<S,E> {
    CompletableFuture<Exception> start(ProcessCallback<S,E> callback);
    CompletableFuture<Exception> stop();
    void setNewFvDimensionCallback(NewFvDimensionCallback newFvDimensionCallback);
}
