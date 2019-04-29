package com.zjucsc.application.tshark.decode;

public abstract class AbstractHandler<T> implements Handler<T>{

    private AbstractHandler prevHandler;
    private AbstractHandler nextHandler;
    private PipeLine pipeLine;

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
}
