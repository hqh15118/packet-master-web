package com.zjucsc.packetmasterweb.tshark_test.demos.pre_processor;

import com.zjucsc.application.tshark.decode.PipeLine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 19:19
 */
@Slf4j
public abstract class BasePreProcessor<P> implements PreProcessor<P> {

    private String filePath = null;
    private volatile boolean processRunning = false;
    private PipeLine pipeLine;
    private long startTime;
    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    public void execCommand() {
        assert pipeLine!=null;
        List<String> fieldList = filterFields();
        appendBaseCommand(fieldList);    //init fv dimension packet format
        pcapFilePath(-1);            //init pcap file path

        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append(tsharkPath()).append(" ")
                .append("-l -n").append(" ");
//        if (protocolFilterField()!=null){
//            commandBuilder.append("-Y ").append(protocolFilterField()).append(" ");
//        }                                               //-Y xxx
        commandBuilder.append("-T ek").append(" ");     //-T ek
        for (String field : fieldList) {
            commandBuilder.append("-e ").append(field).append(" "); // -e xxx ...
        }
        if (StringUtils.isNotBlank(filePath)){
            commandBuilder.append(filePath);          //-r pcap file
        }
        commandBuilder.append(protocolFilterField());
        String command = commandBuilder.toString();
        log.info("*****************");
        log.info("run command : {} " , command);
        log.info("*****************");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            processRunning = true;
            int num = 0;
            startTime = System.currentTimeMillis();
            try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                for (; processRunning; ) {
                    String packetInJSON = bfReader.readLine();
                    if (packetInJSON != null) {
                        if (packetInJSON.length() > 85) {
                            pipeLine.pushDataAtHead(packetInJSON);//ok
                            num++;
                        }
                    }else{
                        System.out.println("num : ");
                        System.out.println(num);
                        System.out.println("delta time : ");
                        System.out.println(System.currentTimeMillis() - startTime);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.error("can not run command {} " , commandBuilder.toString());
            e.printStackTrace();
        }
    }

    @Override
    public String tsharkPath() {
        return "tshark";
    }

    @Override
    public void pcapFilePath(int limit) {
        if (limit < 0)
            filePath =  " -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/pcap_s7_andmosbus.pcap ";
        else
            filePath = " -c " + limit +  " -r /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/pcap_s7_andmosbus.pcap";
    }

    @Override
    public void stopProcess() {
        processRunning = false;
    }

    public void setPipeLine(PipeLine pipeLine) {
        this.pipeLine = pipeLine;
    }

    private void appendBaseCommand(List<String> fields){
        //-e tcp.srcport -e tcp.dstport
        if (!fields.contains("frame.protocols")){
            fields.add("frame.protocols");
        }
        if (!fields.contains("eth.dst")){
            fields.add("eth.dst");
        }
        if (!fields.contains("eth.src")){
            fields.add("eth.src");
        }
        if (!fields.contains("frame.cap_len")){
            fields.add("frame.cap_len");
        }
        if (!fields.contains("ip.dst")){
            fields.add("ip.dst");
        }
        if (!fields.contains("ip.src")){
            fields.add("ip.src");
        }
        if (!fields.contains("tcp.srcport")){
            fields.add("tcp.srcport");
        }
        if (!fields.contains("tcp.dstport")){
            fields.add("tcp.dstport");
        }
        if (!fields.contains("eth.trailer")){
            fields.add("eth.trailer");
        }
        if (!fields.contains("eth.fcs")){
            fields.add("eth.fcs");
        }
    }

}
