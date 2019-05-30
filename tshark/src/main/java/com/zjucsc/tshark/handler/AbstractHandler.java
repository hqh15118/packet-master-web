package com.zjucsc.tshark.handler;

/**
 * #project spring-boot-starter
 *
 * @author hongqianhui
 * #create_time 2019-04-25 - 12:15
 */

/**
 *
 * @param <T> return type of this handler
 */
public abstract class AbstractHandler<T> implements Handler<T>{

    private AbstractHandler prevHandler;
    private AbstractHandler nextHandler;
    protected String id;
    /**
     *
     */
    private PipeLine pipeLine;
    public AbstractHandler(){
        id = this.getClass().getSimpleName();
    }

    public AbstractHandler setId(String id){
        this.id = id;
        return this;
    }

    @Override
    public void setPipeLine(PipeLine line) {
        this.pipeLine = line;
    }

    @Override
    public AbstractHandler prevHandler() {
        return prevHandler;
    }

    @Override
    public AbstractHandler nextHandler() {
        return nextHandler;
    }

    @Override
    public void setPrevHandler(AbstractHandler prevHandler) {
        this.prevHandler = prevHandler;
    }

    @Override
    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleAndPass(Object t);

    @Override
    public String toString() {
        return " ---> " + id;
    }
}
