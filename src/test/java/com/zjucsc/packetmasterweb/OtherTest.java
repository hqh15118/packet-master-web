package com.zjucsc.packetmasterweb;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.pcap4j.PacketListenHandler;
import com.zjucsc.application.tshark.capture.PacketMain;
import com.zjucsc.application.tshark.handler.BasePacketHandler;
import com.zjucsc.application.tshark.handler.PacketDecodeHandler;
import com.zjucsc.application.tshark.handler.PacketSendHandler;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import com.zjucsc.application.util.ByteUtils;
import com.zjucsc.packetmasterweb.new_format_test.TimeStampBean;
import org.junit.Test;
import org.pcap4j.core.*;

import java.io.*;
import java.lang.reflect.Field;
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

    @Test
    public void check_class_type() throws IllegalAccessException {
        Field[] fields = PACKET_PROTOCOL.class.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.get(null));
        }
    }

    @Test
    public void hex_to_int(){
        String hex = "1";       //modbus
        long time1 = System.currentTimeMillis();
        long id = 0;
        for (int i = 0; i < 400000; i++) {
            id = Integer.decode(hex);
        }
        System.out.println(id);
        System.out.println(System.currentTimeMillis() - time1);

        System.out.println("*********************");
        //
        hex = "0x00000004";
        time1 = System.currentTimeMillis();
        for (int i = 0; i < 400000; i++) {
            id = Integer.decode(hex);
        }
        System.out.println(id);
        System.out.println(System.currentTimeMillis() - time1);
        System.out.println("*********************");
        hex = "fafacadadwerq";
        try{
            id = Integer.decode(hex);   //java.lang.NumberFormatException: For input string: "fafacadadwerq"
        }catch (RuntimeException e){
            System.out.println(e);
        }
        System.out.println(id);
    }

    @Test
    public void pcap_decode_test() throws PcapNativeException, NotOpenException, InterruptedException {
        String path = OtherTest.class.getResource("/").getPath() +  "test_pcap.pcap";
        //PcapHandle handle = Pcaps.openOffline("C:\\Users\\Administrator\\IdeaProjects\\packet-master-web\\src\\test\\resource\\test_pcap.pcap");
        PcapHandle handle = Pcaps.openOffline("C:\\Users\\Administrator\\Desktop\\packets.pcap");
        handle.setFilter("tcp" , BpfProgram.BpfCompileMode.OPTIMIZE);
        handle.loop(50,new PacketListenHandler());
        Thread.sleep(100000000);
    }


    @Test
    public void stream_speed_test() throws PcapNativeException {
        //String command = "/Applications/Wireshark.app/Contents/MacOS/tshark -T ek -l  -n -V  -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap";
//        String command = new TsharkCommand.Builder()
//                .tsharkPath("tshark")
//                .outputType(TsharkCommand.OutputType.EK)
//                .ek_E("eth.trailer")
//                .ek_E(" eth.fcs")
//                .pcapFilePath("/Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap")
//                .build();
        Process process = PacketMain.runTargetCommand(Common.CAPTURE_COMMAND_WIN);
        //0 packet --> 526
        //2000 packets --> 942
        int packetNum = 0;
        TimeStampBean timeStampBean = null;
        //PcapHandle handle = Pcaps.openOffline("/Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap");
        try (InputStream is = process.getInputStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(is)) {
            long startTime = System.currentTimeMillis();
            byte[] buffered = new byte[1024];
            while((bufferedInputStream.read(buffered))>0){
                    packetNum++;
            }
            System.out.println(packetNum);
            System.out.println(System.currentTimeMillis() - startTime);
            System.out.println("quit");
        } catch (RuntimeException | IOException e) {
            throw new OpenCaptureServiceException("can not get pipe input of tshark");
        } finally {
            process.destroy();
            System.out.println("process exit value : {} " +  process.exitValue());
        }
    }

    @Test
    public void time_stamp_test() throws PcapNativeException {
        String timeStampStr = "{\"timestamp\":\"1557129889030\",\"layers\":{\"eth_trailer\":[\"00020d040000369b8027a8000000369b8027c400\"],\"eth_fcs\":[\"0x00000061\"]}}";
        TimeStampBean timeStampBean = JSON.parseObject(timeStampStr , TimeStampBean.class);

        byte[] lastTFByte = ByteUtils.contractBytes(24,
                ByteUtils.hexStringToByteArray(timeStampBean.layers.eth_trailer[0]),
                ByteUtils.hexStringToByteArray(timeStampBean.layers.eth_fcs[0] , 2)
        );

        for (byte b : lastTFByte) {
            System.out.println(Integer.toHexString(Byte.toUnsignedInt(b)));
        }
    }

    @Test
    public void stream_speed_test1() throws PcapNativeException {
        //String command = "/Applications/Wireshark.app/Contents/MacOS/tshark -T ek -l  -n -V  -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap";
//        String command = new TsharkCommand.Builder()
//                .tsharkPath("tshark")
//                .outputType(TsharkCommand.OutputType.EK)
//                .ek_E("eth.trailer")
//                .ek_E("-e eth.fcs")
//                .pcapFilePath("/Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap")
//                .build();
        Process process = PacketMain.runTargetCommand(Common.CAPTURE_COMMAND_MAC);
        //0 packet --> 526
        //2000 packets --> 942
        int packetNum = 0;
        TimeStampBean timeStampBean = null;
        //PcapHandle handle = Pcaps.openOffline("/Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/question_1531953261_01.pcap");
        try (InputStream is = process.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            long startTime = System.currentTimeMillis();
            for (; ; ) {
                String str;
                if ((str = reader.readLine()) != null) {
                    if (str.length() > 85)
                    {
                        //timeStampBean = JSON.parseObject(str , TimeStampBean.class);
                        packetNum ++;
//                        byte[] lastTFByte = ByteUtils.contractBytes(24,
//                                ByteUtils.hexStringToByteArray(timeStampBean.layers.eth_trailer[0]),
//                                ByteUtils.hexStringToByteArray(timeStampBean.layers.eth_fcs[0] , 2)
//                        );
                    }
                } else {
                    System.out.println(System.currentTimeMillis() - startTime);
                    break;
                }
            }
            System.out.println(packetNum);
            System.out.println("quit");
        } catch (RuntimeException | IOException e) {
            throw new OpenCaptureServiceException("can not get pipe input of tshark");
        } finally {
            process.destroy();
            System.out.println("process exit value : {} " +  process.exitValue());
        }
    }


    @Test
    public void string_test(){
        String s = "";
        String s1 = "";
        assert s1 == s;
        assert s1.equals(s);
    }

}
