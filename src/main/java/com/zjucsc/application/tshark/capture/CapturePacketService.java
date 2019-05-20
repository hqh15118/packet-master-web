package com.zjucsc.application.tshark.capture;

import java.util.concurrent.CompletableFuture;

public interface CapturePacketService<S,E> {
    CompletableFuture<Exception> start(ProcessCallback<S,E> callback);
    CompletableFuture<Exception> stop();
    void setNewFvDimensionCallback(NewFvDimensionCallback newFvDimensionCallback);
}
