package com.zjucsc.tshark.handler;

import com.zjucsc.common.common_util.ExceptionSafeRunnable;
import com.zjucsc.tshark.exception.NextHandlerNotExistException;

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

    public void addPipeLine(PipeLine pipeLines){
        this.pipeLines.add(pipeLines);
        pipeLines.setFirstHandler(this);
    }

    public AbstractAsyncHandler(ExecutorService executor){
        this.executor = executor;
    }

    public ExecutorService getExecutor(){
        return executor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleAndPass(Object inValue){
        if (executor==null){
            //run handle in sync schema
            T t = handle(inValue);
            nextHandler().handleAndPass(t);
            for (PipeLine pipeLine : pipeLines) {
                pipeLine.pushDataAtHead(t);
            }
        }else{
            //run handle in async schema
            executor.execute(new ExceptionSafeRunnable<Object>(){
                @Override
                public void run(Object o) {
                    T t = handle(inValue);
                    if (nextHandler()!=null) {
                        nextHandler().handleAndPass(t);
                    }
                    /**
                     * 把该部分的处理结果推送到其他的pipeLine
                     */
                    if (pipeLines.size() > 0){
                        for (PipeLine pipeLine : pipeLines) {
                            pipeLine.pushDataAtHead(t);
                        }
                    }
                }
            });
        }
    }

    @Override
    public String toString() {
        return " ---> " + id;
    }

    List<PipeLine> getPipeLine(){
        return pipeLines;
    }

    public void directPass(T t){
        if (nextHandler()!=null){
            nextHandler().handleAndPass(t);
        }else{
            throw new NextHandlerNotExistException("can not pass object[T] to next handler cause next handler is null!");
        }
    }
}
