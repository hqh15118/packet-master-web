package com.zjucsc.capture_main_distribute.service;

import com.zjucsc.capture_main_distribute.tshark.capture.NewFvDimensionCallback;
import com.zjucsc.capture_main_distribute.tshark.capture.ProcessCallback;

import java.util.concurrent.CompletableFuture;

public interface CapturePacketService<S,E> {
    CompletableFuture<Exception> start(ProcessCallback<S, E> callback);
    CompletableFuture<Exception> stop();
    void setNewFvDimensionCallback(NewFvDimensionCallback newFvDimensionCallback);
}
