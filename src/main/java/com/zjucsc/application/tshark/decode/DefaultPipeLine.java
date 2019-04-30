package com.zjucsc.application.tshark.decode;

import java.util.LinkedList;
import java.util.List;

/**
 * #project spring-boot-starter
 *
 * @author hongqianhui
 * #create_time 2019-04-25 - 11:20
 */
public class DefaultPipeLine implements PipeLine {
    private LinkedList<AbstractHandler> handlerLinkedList = new LinkedList<>();

    private String pipeId;
    @Override
    public void addLast(AbstractHandler handler) {
        if (handlerLinkedList.size() == 0){
            handlerLinkedList.addLast(handler);
        }else {
            AbstractHandler lastHandler = handlerLinkedList.getLast();
            handlerLinkedList.addLast(handler);
            handler.setPrevHandler(lastHandler);
            lastHandler.setNextHandler(handler);
        }
        handler.setPipeLine(this);
    }

    public DefaultPipeLine(){
        pipeId = this.getClass().getSimpleName();
    }

    public DefaultPipeLine(String pipeId){
        this.pipeId = pipeId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void pushDataAtHead(Object t) {
        handlerLinkedList.getFirst().handleAndPass(t);
    }

    public int pipeLineSize(){
        return handlerLinkedList.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("start[").append(pipeId).append("]");
        for (AbstractHandler handlerInstance : handlerLinkedList) {
            stringBuilder.append(handlerInstance);
        }
        stringBuilder.append("\n");
        for (AbstractHandler handlerInstance : handlerLinkedList) {
            if (handlerInstance instanceof AbstractAsyncHandler){
                List pipeLines = ((AbstractAsyncHandler)handlerInstance).getPipeLine();
                for (Object pipeLine : pipeLines){
                    stringBuilder.append(pipeLine);
                }
            }
        }
        return stringBuilder.toString();
    }
}
