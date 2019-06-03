package com.zjucsc.tshark.pre_processor;

import com.zjucsc.tshark.Common;
import com.zjucsc.tshark.CommonTsharkUtil;
import com.zjucsc.tshark.handler.PipeLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 19:19
 *
 * 信息流向：
 *                     JSON                                    FvDimensionLayer                    FvDimensionLayer
 * xxxPreProcessor1 ------------> decodeThreadPool1[xxxPacket1] ------------> fvDimensionHandler --------------------> badpacketanalyzeHandler
 *                                                                    ↑        五元组发送 + 报文统计                             恶意报文分析
 *  输出JSON字符串                将JSON字符串解析为具体的Packet实体对象     ↑          单线程，无限队列                              多线程，无限队列
 *                              这部分还会将传送上来的协议替换为本地的协议    ↑
 *                                                                    ↑
 * xxxPreProcessor2 ------------> decodeThreadPool2[xxxPacket2] -----→J
 *                                                                    ↑
 * xxxPreProcessor3 ------------> decodeThreadPool3[xxxPacket3] -----→J
 */
//@Slf4j
public abstract class BasePreProcessor implements PreProcessor {

    private static String chosenDeviceMac = null;
    private static String captureDeviceName = null;
    private CommandBuildFinishCallback commandBuildFinishCallback;
    private String filePath = null;
    private volatile boolean processRunning = false;
    private static final Semaphore semaphore = new Semaphore(1);
    protected PipeLine pipeLine;
    protected ExecutorService decodeThreadPool = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName(BasePreProcessor.this.getClass().getName() + " -pre_process_thread");
        thread.setUncaughtExceptionHandler(Common.uncaughtExceptionHandler);
        return thread;
    });

    static{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hook start working ... ");
                CommonTsharkUtil.shotDownAllRunningTsharkProcess();
                System.out.println("hook finish working ... ");
            }
        });
        Runtime.getRuntime().addShutdownHook(thread);
    }

    @Override
    public void execCommand(int type , int limit) {
        assert pipeLine!=null;
        StringBuilder commandBuilder = new StringBuilder();
        /**
         * command builder
         */
        List<String> fieldList = filterFields();
        appendBaseCommand(fieldList);       //init fv dimension packet format add all fv dimension into field list

        commandBuilder.append(tsharkPath()).append(" ")
                .append("-l -n").append(" ");           //tshark -l -n

        if (type == 0) {
            pcapFilePath(limit);                        //init pcap file path
        }else{
            assert chosenDeviceMac!=null;
            assert captureDeviceName!=null;
            commandBuilder.append(" -i ").append(captureDeviceName).append(" ");    // -i capture_service
        }

        commandBuilder.append("-T ek").append(" ");       // -T ek
        for (String field : fieldList) {
            commandBuilder.append("-e ").append(field).append(" "); // -e xxx ... 根据需要的协议设置 + 五元组
        }

        if (!protocolFilterField()[0].contains("not")){
            CommonTsharkUtil.addCaptureProtocol(protocolFilterField());
        }

        if (type == 0) {
            if (filePath!=null && filePath.length() > 0) {
                commandBuilder.append(filePath);          //-r pcap file
            }
        }else{
            if(limit > 0){
                commandBuilder.append(" -c ").append(limit);
            }
            // -f "xxx not mac"
            commandBuilder.append(" -f ").append("\"").append(filter())
                    .append(" and not ether src ").append(chosenDeviceMac).append("\"");
        }
        commandBuilder.append(" -Y ").append("\"");
        for (String s : protocolFilterField()) {
            commandBuilder.append(s).append(" ");
        }
        commandBuilder.append("\"");   // 最后的部分 + s7comm/...用于过滤
        commandBuilder.append(" -M 10000");    //设置十万条之后重置回话
        String command = commandBuilder.toString();
        if (commandBuildFinishCallback!=null){
            commandBuildFinishCallback.commandBuildFinish();
        }
        try {
            /**
             * 这里必须要保证一次只能有一个线程运行tshark程序
             * 【如果有多个tshark文件，那么可以同时开多个不同的tshark】
             */
            semaphore.acquire();
        } catch (InterruptedException e) {
            return;
        }
        Process process = null;
        try {
            //TODO ADD LOG HERE
            //log.info("***************** {} ==> run command : {} " , this.getClass().getName() , command);

            //process = Runtime.getRuntime().exec(new String[]{"bash","-c",command});
            process = Runtime.getRuntime().exec(command);
            CommonTsharkUtil.addTsharkProcess(process);
            doWithErrorStream(process.getErrorStream() , command);
            //log.info("start running --------------------> now ");//TODO LOG HERE
            processRunning = true;
            try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                for (;processRunning;) {
                    String packetInJSON = bfReader.readLine();
                    if (packetInJSON != null) {
                        if (packetInJSON.length() > 85) {
                            decodeJSONString(packetInJSON);
                        }
                    }else{
                        if (processRunning) {
                            //TODO LOG HERE
                            //log.info("{} exit by end finishing reading ..", this.getClass().getName());
                        }else {
                            //log.info("{} exit by end quiting capture service..", this.getClass().getName());
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            //log.error("can not run command {} " , commandBuilder.toString()); //LOG HERE
            e.printStackTrace();
        }finally {
            if (process!=null){
                CommonTsharkUtil.removeTsharkProcess(process);
                process.destroy();
                /*
                  Returns the exit value for the subprocess.

                  @return the exit value of the subprocess represented by this
                 *         {@code Process} object.  By convention, the value
                 *         {@code 0} indicates normal termination.
                 * @throws IllegalThreadStateException if the subprocess represented
                 *         by this {@code Process} object has not yet terminated
                 */
                //TODO LOG HERE
                //log.info("{} exit with exit value : {} " , this.getClass().getName()  , process.exitValue());
            }
        }
    }

    private void doWithErrorStream(InputStream errorStream , String command) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
        String str;
        try {
            if ((str = bufferedReader.readLine()) != null) {
                System.out.println("error stream : " + str);
                /**
                 * 当接收到capture on xxx的时候，就表示该tshark进程已经开启完毕了，那么就可以释放
                 * 信号量，让下一个线程打开tshark
                 */
                semaphore.release();
            }
        } catch (IOException e) {
            //log.error("" , e);//TODO LOG HERE
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                //log.error("" , e);//TODO LOG HERE
            }
        }
    }


    @Override
    public String tsharkPath() {
        return tsharkWinPath;
    }

    @Override
    public void pcapFilePath(int limit) {
        System.out.println("*** operation name is : " + Common.OS_NAME);
        if (limit < 0)
            filePath =  " -r  " + pcapFilePathForWin;
        else
            filePath = " -c " + limit + " -r " +  pcapFilePathForWin;
    }

    @Override
    public void stopProcess() {
        CommonTsharkUtil.shotDownAllRunningTsharkProcess();
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
        if (!fields.contains("tcp.payload")){
            fields.add("tcp.payload");
        }
        if (!fields.contains("tcp.flags.syn")){
            fields.add("tcp.flags.syn");
        }
        if (!fields.contains("tcp.flags.ack")){
            fields.add("tcp.flags.ack");
        }
    }

    public abstract void decodeJSONString(String packetJSON);

    private String tsharkMacPath = " tshark ";
    private String tsharkWinPath = " tshark";
    private String pcapFilePathForMac = " /Users/hongqianhui/JavaProjects/packet-master-web/src/main/resources/pcap/104_dnp_packets.pcapng ";
    private String pcapFilePathForWin = " C:\\Users\\Administrator\\IdeaProjects\\packet-master-web\\src\\main\\resources\\pcap\\104_dnp_packets.pcapng";

    public static void setCaptureDeviceNameAndMacAddress(String macAddress,String captureDeviceName){
        BasePreProcessor.captureDeviceName = captureDeviceName;
        BasePreProcessor.chosenDeviceMac = macAddress;
    }

    @Override
    public String filter() {
        return "tcp";
    }

    public void setCommandBuildFinishCallback(CommandBuildFinishCallback commandBuildFinishCallback){
        this.commandBuildFinishCallback = commandBuildFinishCallback;
    }

    public interface CommandBuildFinishCallback{
        void commandBuildFinish();
    }
}