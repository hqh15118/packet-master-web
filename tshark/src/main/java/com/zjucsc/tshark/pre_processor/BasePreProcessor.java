package com.zjucsc.tshark.pre_processor;

import com.zjucsc.tshark.TsharkCommon;
import com.zjucsc.tshark.bean.ProcessWrapper;
import com.zjucsc.tshark.handler.PipeLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
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
    private static Logger logger = LoggerFactory.getLogger(BasePreProcessor.class);
    private CommandBuildFinishCallback commandBuildFinishCallback;
    private String filePath = null;
    private volatile boolean processRunning = false;
    private static final Semaphore semaphore = new Semaphore(1);
    PipeLine pipeLine;
    private String bindCommand;

    public static final int ONLINE_CAPTURE = 1;
    public static final int OFFLINE_CAPTURE = 0;
    private ProcessWrapper oldProcessWrapper;
    private Process process = null;

    static {
        Thread thread = new Thread(() -> {
            System.out.println("hook start working ... ");
            TsharkCommon.shotDownAllRunningTsharkProcess();
            System.out.println("hook finish working ... ");
        });
        Runtime.getRuntime().addShutdownHook(thread);
    }

    /**
     * @param tsharkPath
     * @param macAddress
     * @param interfaceName
     * @param pipeLine
     * @param type
     * @param limit
     */
    @Override
    public void startCapture(String tsharkPath , String macAddress, String interfaceName, PipeLine pipeLine,
                             int type , int limit) {
        execCommand(tsharkPath,macAddress,interfaceName,pipeLine,1,-1);
    }

    private void execCommand(String tsharkPath , String macAddress,
                             String interfaceName, PipeLine pipeLine,
                             int type , int limit) {
        this.pipeLine = pipeLine;
        doExecCommand(buildCommand(tsharkPath, macAddress, interfaceName, type, limit));
    }

    private String buildCommand(String tsharkPath , String macAddress,
                                String interfaceName,
                                int type , int limit) {
        StringBuilder commandBuilder = new StringBuilder();
        /*
         * command builder
         */
        List<String> fieldList = filterFields();
        appendBaseCommand(fieldList);       //init fv dimension packet format add all fv dimension into field list

        commandBuilder.append(tsharkPath).append(" ")
                .append("-l -n").append(" ");           //tshark -l -n

        if (type == 0) {
            pcapFilePath(limit);                        //init pcap file path
        }else{
            commandBuilder.append(" -i ").append(interfaceName).append(" ");    // -i capture_service
        }

        commandBuilder.append("-T ek").append(" ");       // -T ek
        for (String field : fieldList) {
            commandBuilder.append("-e ").append(field).append(" "); // -e xxx ... 根据需要的协议设置 + 五元组
        }

        if (!protocolFilterField()[0].contains("not")){
            TsharkCommon.addCaptureProtocol(protocolFilterField());
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
            if (filter().length() > 0){
                commandBuilder.append(" -f ").append("\"").append(filter())
                        .append(" and not ether src ").append(macAddress).append("\"");
            }else{
                commandBuilder.append(" -f ").append("\"").append(" not ether src ")
                        .append(macAddress).append("\"");
            }
        }
        if (extConfig()!=null && extConfig().length() > 0) {
            commandBuilder.append(" ").append(extConfig());
        }
        commandBuilder.append(" -Y ").append("\"");
        for (int i = 0; i < protocolFilterField().length - 1; i++) {
            commandBuilder.append(protocolFilterField()[i]).append(" or");
        }
        commandBuilder.append(" ").append(protocolFilterField()[protocolFilterField().length - 1]);
        commandBuilder.append("\"");   // 最后的部分 + s7comm/...用于过滤
        commandBuilder.append(" -M ").append(TsharkCommon.sessionReset);    //设置n条之后重置回话
        return commandBuilder.toString();
    }


    @Override
    public void pcapFilePath(int limit) {
        if (limit < 0)
            filePath =  " -r  " + pcapPath();
        else
            filePath = " -c " + limit + " -r " +  pcapPath();
    }

    @Override
    public void stopCapture() {
        processRunning = false; //停止读取数据流
        process.destroy();
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
//        if (!fields.contains("eth.trailer")){
//            fields.add("eth.trailer");
//        }
//        if (!fields.contains("eth.fcs")){
//            fields.add("eth.fcs");
//        }
        if (!fields.contains("tcp.payload")){
            fields.add("tcp.payload");
        }
        if (!fields.contains("tcp.flags.syn")){
            fields.add("tcp.flags.syn");
        }
        if (!fields.contains("tcp.flags.ack")){
            fields.add("tcp.flags.ack");
        }
        if (!fields.contains("-e custom_ext_raw_data")){
            fields.add("custom_ext_raw_data");
        }
    }

    public String pcapPath(){
        return "";
    }

    @Override
    public String filter() {
        return TsharkCommon.filter;
    }

    public void setCommandBuildFinishCallback(CommandBuildFinishCallback commandBuildFinishCallback){
        this.commandBuildFinishCallback = commandBuildFinishCallback;
    }

    public interface CommandBuildFinishCallback{
        void commandBuildFinish();
    }

    public String extConfig() {
        return null;
    }

    private void doExecCommand(String command) {
        bindCommand = command;
        try {
            /**
             * 这里必须要保证一次只能有一个线程运行tshark程序
             * 【如果有多个tshark文件，那么可以同时开多个不同的tshark】
             */
            semaphore.acquire();
        } catch (InterruptedException e) {
            return;
        }

        try {
            process = Runtime.getRuntime().exec(command);
            oldProcessWrapper = new ProcessWrapper(process,command);
            TsharkCommon.addTsharkProcess(oldProcessWrapper);
            //本地离线不需要设置error stream
            doWithErrorStream(process.getErrorStream(), command);
            if (commandBuildFinishCallback!=null){
                commandBuildFinishCallback.commandBuildFinish();
            }
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
                            //System.out.println("tshark process out by finishing read data");
                            logger.info("{} exit by end finishing reading ..", this.getClass().getName());
                        }else {
                            //System.out.println("tshark process out by stop capture");
                            logger.info("{} exit by end quiting capture service..", this.getClass().getName());
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
                TsharkCommon.removeTsharkProcess(oldProcessWrapper);
                process.destroy();
                /*
                  Returns the exit value for the subprocess.

                  @return the exit value of the subprocess represented by this
                 *         {@code Process} object.  By convention, the value
                 *         {@code 0} indicates normal termination.
                 * @throws IllegalThreadStateException if the subprocess represented
                 *         by this {@code Process} object has not yet terminated
                 */
                logger.info("{} exit with exit value : {} " , this.getClass().getName()  , process.exitValue());
            }
        }
    }

    private void doWithErrorStream(InputStream errorStream , String command) {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream))) {
            String str;
            if ((str = bufferedReader.readLine()) != null) {
                System.out.println(">>>>>>>>>>>>>" + str);
                if (!str.startsWith("Capturing")){
                    str = bufferedReader.readLine();
                    if (!"".equals(str)) {
                        System.out.println(">>>>>>>>>>>>>" + str);
                    }
                }
                /*
                 * 当接收到capture on xxx的时候，就表示该tshark进程已经开启完毕了，那么就可以释放
                 * 信号量，让下一个线程打开tshark
                 */
                //统一设置错误流
                //TsharkCommon.handleTsharkErrorStream(command.split("-Y")[1],bufferedReader);
            }
            semaphore.release();
        } catch (IOException e) {
            logger.error("exception is caught while exec [{}]",command,e);
        }
    }

    @Override
    public void restartCapture() {
        TsharkCommon.removeTsharkProcess(oldProcessWrapper);
        oldProcessWrapper.getProcess().destroyForcibly();
        doExecCommand(bindCommand);
    }



    public String getBindCommand(){
        return bindCommand;
    }
}
