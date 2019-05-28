package com.zjucsc.tshark.handler;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-01 - 01:14
 */
public abstract class AbstractPipeLine implements PipeLine{
    private AbstractHandler firstHandler;
    @Override
    public void setFirstHandler(AbstractHandler firstHandler){
        this.firstHandler = firstHandler;
    }
    @Override
    public AbstractHandler getFirstHandler(){
        return firstHandler;
    }
}
