package com.zjucsc.application.tshark.decode;

import lombok.experimental.SuperBuilder;

import java.util.LinkedList;

public class DefaultPipeLine implements PipeLine {
    private LinkedList<AbstractHandler> handlerLinkedList = new LinkedList<>();

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

    @SuppressWarnings("unchecked")
    @Override
    public void pushDataAtHead(Object t) {
        handlerLinkedList.getFirst().handleAndPass(t);
    }

    public int pipeLineSize(){
        return handlerLinkedList.size();
    }
}
