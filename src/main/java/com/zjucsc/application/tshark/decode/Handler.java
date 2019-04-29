package com.zjucsc.application.tshark.decode;


interface Handler<T> {
    T handle(Object t);
    void setPipeLine(PipeLine line);
    AbstractHandler prevHandler();
    AbstractHandler nextHandler();
    void setPrevHandler(AbstractHandler prevHandler);
    void setNextHandler(AbstractHandler nextHandler);
}
