package com.zjucsc.application.tshark.decode;

/**
 * #project spring-boot-starter
 *
 * @author hongqianhui
 * #create_time 2019-04-22 - 10:40
 */
interface Handler<T> {
    T handle(Object t);
    void setPipeLine(PipeLine line);
    AbstractHandler prevHandler();
    AbstractHandler nextHandler();
    void setPrevHandler(AbstractHandler prevHandler);
    void setNextHandler(AbstractHandler nextHandler);
}
