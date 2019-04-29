package com.zjucsc.application.tshark.decode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * #project spring-boot-starter
 *
 * @author hongqianhui
 * #create_time 2019-04-24 - 13:00
 */
public abstract class AbstractAsyncHandler<T> extends AbstractHandler<T> {

    private ExecutorService executor;
    /**
     * 与该handle并行的pipeline
     */
    private List<PipeLine> pipeLines = new ArrayList<>(0);

    public AbstractAsyncHandler(){

    }

    public void addPipeLine(PipeLine pipeLines){
        this.pipeLines.add(pipeLines);
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
        for (PipeLine pipeLine : pipeLines) {
            pipeLine.pushDataAtHead(inValue);
        }
    }

    @Override
    public String toString() {
        return " ---> " + id;
    }

    List<PipeLine> getPipeLine(){
        return pipeLines;
    }
}
