package com.zjucsc.packetmasterweb;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.tshark.handler.BasePacketHandler;
import com.zjucsc.application.tshark.handler.PacketDecodeHandler;
import com.zjucsc.application.tshark.handler.PacketSendHandler;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 23:48
 */
public class OtherTest {

    @Test
    public void pipeLineBannerTest(){
        DefaultPipeLine pipeLine = new DefaultPipeLine();
        pipeLine.addLast(new BasePacketHandler(Executors.newSingleThreadExecutor()));
        pipeLine.addLast(new PacketDecodeHandler(Executors.newFixedThreadPool(10)));
        PacketSendHandler handler = new PacketSendHandler(Executors.newSingleThreadExecutor());
        pipeLine.addLast(handler);
        DefaultPipeLine pipeLine1 = new DefaultPipeLine();
        handler.addPipeLine(pipeLine1);
        pipeLine1.addLast(new BasePacketHandler(Executors.newSingleThreadExecutor()).setId("base packet handler"));
        pipeLine1.addLast(new PacketDecodeHandler(Executors.newFixedThreadPool(10)));
        pipeLine1.addLast(new PacketSendHandler(Executors.newSingleThreadExecutor()));
        System.out.println(pipeLine);
    }

    @Test
    public void load_class_Test() throws ClassNotFoundException {
        long time1 = 0;
        for (int i = 0; i < 10; i++) {
            time1 = System.nanoTime();
            Class<?> iProtocolFuncodeMapClass = Class.forName("com.zjucsc.IProtocolFuncodeMap");
            System.out.println(System.nanoTime() - time1);
        }
    }


    public static class TestClass<T>{
        public T t;
        public TestClass(T t){
            this.t = t;
        }
    }

    @Test
    public void json_test(){
        TestClass<String> stringTestClass = new TestClass<>("hongqianhui");
        System.out.println(JSON.toJSONString(stringTestClass));
    }

    @Test
    public void default_analyzer(){
        DefaultAnalyzer defaultAnalyzer = new DefaultAnalyzer(null);
        OperationPacketFilter<String,String> packetFilter = new OperationPacketFilter<>("packet filter");
        packetFilter.addBlackRule("1" , "hello");
        packetFilter.addBlackRule("2" , "okokok!");
        defaultAnalyzer.setAnalyzer(packetFilter);
        System.out.println(JSON.toJSONString(defaultAnalyzer));
    }

    @Test
    public void string_builder_Test(){
        StringBuilder sb = new StringBuilder();
        sb.append("hello").append(" ").append("world");
        String str1 = sb.toString();
        System.out.println(str1);
        sb.delete(0,sb.length());
        String str2 = sb.append("hello").append(":").append("world2").toString();
        assert str1!=str2;
    }

    @Test
    public void less_test(){
        String str = "{\"index\" : {\"_index\": \"packets-2019-05-04\", \"_type\": \"pcap_file\", \"_score\": null}}";
        System.out.println(str.length());
    }

}
