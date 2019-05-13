package com.zjucsc.application.tshark.pre_processor;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.handler.ThreadExceptionHandler;
import com.zjucsc.application.tshark.decode.PipeLine;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
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
 *
 * 信息流向：
 *                     JSON                                 FvDimensionLayer
 * xxxPreProcessor ------------> decodeThreadPool[xxxPacket] ------------> fvDimensionHandler -->
 *                                                                |        五元组发送 + 报文统计
 *                                                                |
 *                                                                |
 *                                                                |---------------> badpacketanalyzeHandler
 *  输出JSON字符串                将JSON字符串解析为具体的Packet实体对象                       恶意报文分析
 *                              这部分还会将传送上来的协议替换为本地的协议
 */
@Slf4j
public abstract class BasePreProcessor<P> implements PreProcessor<P> {

    private String filePath = null;
    private volatile boolean processRunning = false;
    private PipeLine pipeLine;
    private ExecutorService decodeThreadPool = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName(BasePreProcessor.this.getClass().getName() + " -pre process thread");
        thread.setUncaughtExceptionHandler(new ThreadExceptionHandler());
        return thread;
    });

    @Override
    public void execCommand() {
        assert pipeLine!=null;
        List<String> fieldList = filterFields();
        appendBaseCommand(fieldList);    //init fv dimension packet format
        pcapFilePath(2);            //init pcap file path

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
        commandBuilder.append(" ").append(protocolFilterField());
        String command = commandBuilder.toString();
        log.info("*****************");
        log.info("run command : {} " , command);
        log.info("*****************");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            processRunning = true;
            try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                for (;processRunning;) {
                    String packetInJSON = bfReader.readLine();
                    if (packetInJSON != null) {
                        if (packetInJSON.length() > 85) {
                            decodeThreadPool.execute(() -> {
                                pipeLine.pushDataAtHead(decode(JSON.parseObject(packetInJSON,decodeType())));
                            });
                        }
                    }else{
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
        return tsharkMacPath;
    }

    @Override
    public void pcapFilePath(int limit) {
        if (limit < 0)
            filePath =  " -r  " + pcapFilePathForMac;
        else
            filePath = " -c " + limit + " -r " +  pcapFilePathForMac;
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

    public abstract FvDimensionLayer decode(P packetInstance);

    private String tsharkMacPath = " tshark ";
    private String tsharkWinPath = " C:\\Users\\Administrator\\Desktop\\tshark_min_win\\tshark.exe";
    private String pcapFilePathForMac = " /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/text.pcap ";
    private String pcapFilePathForWin = " C:\\Users\\Administrator\\IdeaProjects\\packet-master-web\\src\\main\\resources\\pcap\\text.pcap";
}
