package com.zjucsc.application.tshark.decode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractAsyncHandler<T> extends AbstractHandler<T> {

    private ExecutorService executor;

    public AbstractAsyncHandler(){

    }

    public AbstractAsyncHandler(ExecutorService executor){
        this.executor = executor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleAndPass(Object inValue){
        if (executor==null){
            //run handle in sync schema
            nextHandler().handleAndPass(handle(inValue));
        }else{
            //run handle in async schema
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (nextHandler()!=null) {
                        T t = handle(inValue);
                        nextHandler().handleAndPass(t);
                    }else{
                        handle(inValue);
                    }
                }
            });
        }
    }
}
