package com.zjucsc.tshark.handler;

import org.junit.Test;

import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class AbstractAsyncHandlerTest {


    @Test
    public void handlerConnectTest() throws InterruptedException {
        DefaultPipeLine defaultPipeLine = new DefaultPipeLine("hello");
        AbstractAsyncHandler<String> abstractAsyncHandler = new AbstractAsyncHandler<String>(Executors.newSingleThreadExecutor()) {
            @Override
            public String handle(Object t) {
                System.out.println(t);
                return "hello222";
            }
        };
        AbstractAsyncHandler<String> abstractAsyncHandler1 = new AbstractAsyncHandler<String>(Executors.newSingleThreadExecutor()) {
            @Override
            public String handle(Object t) {
                System.out.println(t);
                return ((String) t);
            }
        };
        defaultPipeLine.addLast(abstractAsyncHandler);
        defaultPipeLine.addLast(abstractAsyncHandler1);
        System.out.println(defaultPipeLine);
        defaultPipeLine.pushDataAtHead("hello111");

        Thread.sleep(10000);
    }

}