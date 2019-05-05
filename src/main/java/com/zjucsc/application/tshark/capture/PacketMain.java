package com.zjucsc.application.tshark.capture;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.system.service.impl.PacketAnalyzeService;
import com.zjucsc.application.tshark.decode.PipeLine;
import com.zjucsc.application.tshark.handler.*;
import com.zjucsc.application.tshark.decode.DefaultPipeLine;
import com.zjucsc.base.util.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Pattern;

import static com.zjucsc.application.config.Common.COMMON_THREAD_EXCEPTION_HANDLER;

public class PacketMain {
    private static Pattern pattern = Pattern.compile("\\d{1,}");
    private static Logger logger = LoggerFactory.getLogger(PacketMain.class);
    private static TsharkConfiguraionClass tsharkConfiguraion = null;
    private static File tsharkConfigurationFile = null;

    private static DefaultPipeLine pipeLine = null;

    //<class 'list'>: ['/usr/local/bin/tshark', '-l', '-n', '-T', 'pdml', '-r', '/Users/hongqianhui/Desktop/2019_4_24_pyshark_test.pcapng']
    static void main1(String[] args) throws IOException {
        if (tsharkConfiguraion == null){
            System.out.println("loading...");
            String configuationFilePath = PacketMain.class.getResource("/").getPath() + "/tsharkConfigurationFile.json";
            tsharkConfigurationFile = new File(configuationFilePath);
            //init application command configuraion
            if (!tsharkConfigurationFile.exists()){
                if (tsharkConfigurationFile.createNewFile()){
                    logger.debug("create tshark configuration file successfully");
                }else{
                    logger.debug("create tshark configuration file failed");
                }
                tsharkConfiguraion = new TsharkConfiguraionClass();
            }else{
                //file exits
                String commandConfiguration = getTsharkCommandConfiguration(tsharkConfigurationFile);
                tsharkConfiguraion = JSON.parseObject(commandConfiguration,TsharkConfiguraionClass.class);
                //防止创建了，但是配置文件内容为空的情况
                if (tsharkConfiguraion == null){
                    tsharkConfiguraion = new TsharkConfiguraionClass();
                }
            }
        }
        String command = "";
        if(args==null || args.length > 0) {
            StringBuilder sb = new StringBuilder();
            //find custom command
            for (String arg : args) {
                sb.append(arg);
            }
            command = sb.toString();
            logger.debug("find command in 'args' >> {}", command);
            Process process = runTargetCommand(command);
            if (process !=null ){
                //run successfully
                doProcess(process);
                tsharkConfiguraion.addHistoryCommand(command);
                return;
            }
        }
        System.out.println("////////////////////////////////////////////// \n");
        int i = 0;
        StringBuilder tmp = new StringBuilder();
        tmp.append("* -1 exit \n");
        for (String historyCommand : tsharkConfiguraion.getHissoryCommand()){
            tmp.append("* historyCommand ").append(">> ").append(i).append(" ").append(historyCommand).append("\n");
            i++;
        }
        System.out.println(tmp.toString());
        System.out.println("//////////////////////////////////////////////");
        //can not run args command or args command is null
        Scanner scanner = new Scanner(System.in);
        Process process = null;
        for (;;){
            System.out.println("input your new command >> ");
            command = scanner.nextLine();
            if (pattern.matcher(command).matches()){
                int index = Integer.parseInt(command);
                if (index == -1){
                    break;
                }
                if (index <= tsharkConfiguraion.getHissoryCommand().size()){
                    command = tsharkConfiguraion.getHissoryCommand().get(index);
                }
            }
            System.out.println("run command >> " + command);
            process = runTargetCommand(command);
            if (process == null){
                continue;
            }
            doProcess(process);
            break;
        }
        tsharkConfiguraion.addHistoryCommand(command);
    }

    private static void saveConfiguration(TsharkConfiguraionClass tsharkConfiguraion,File configurationFile) {
        try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(configurationFile))) {
            outputStreamWriter.write(JSON.toJSONString(tsharkConfiguraion, SerializerFeature.PrettyFormat));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTsharkCommandConfiguration(File file) throws IOException {
        try(BufferedReader bfreader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            StringBuilder sb = new StringBuilder();
            String var = "";
            while((var = bfreader.readLine())!=null){
                sb.append(var);
            }
            return sb.toString();
        }
    }

    @Data
    public static class TsharkConfiguraionClass{
        public String modityTime;
        public LinkedList<String> hissoryCommand;

        public TsharkConfiguraionClass() {
            this.hissoryCommand = new LinkedList<>();
        }

        public void addHistoryCommand(String command){
            if (hissoryCommand.size() >= 5){
                hissoryCommand.removeLast();
            }
            for (String s : hissoryCommand) {
                if (s.equals(command)){
                    return;
                }
            }
            hissoryCommand.addFirst(command);
        }
    }

    public static Process runTargetCommand(String command){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        }catch (IOException e){
            logger.error("can not exec command >> {} << \nerror msg : " , command , e);
            throw new OpenCaptureServiceException("can not run command");
        }
        return process;
    }

    public static DefaultPipeLine pcapPipeLine;

    public static DefaultPipeLine getDefaultPipeLine(){
        DefaultPipeLine pipeLine = new DefaultPipeLine("main pipe line");
        pcapPipeLine = new DefaultPipeLine("pcap pipe line");
        /*
         * main pipeLine handler
         */
        BasePacketHandler basePacketHandler = new BasePacketHandler(Executors.newSingleThreadExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("-base-packet-handler-");
                        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                        return thread;
                    }
                }
        ));

        PacketDecodeHandler packetDecodeHandler = new PacketDecodeHandler(Executors.newFixedThreadPool(5,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("-packet-decoder-handler-");
                        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                        return thread;
                    }
                }));

        PacketSendHandler packetSendHandler = new PacketSendHandler(Executors.newSingleThreadExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("-packet-sender-handler-");
                        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                        return thread;
                    }
                }
        ));
        /*
         * packet statistics pipe line
         */
        DefaultPipeLine packetStatisticsPipeLine = new DefaultPipeLine("packet statistics pipeLine");
        packetStatisticsPipeLine.addLast(new PacketStatisticsHandler(new PacketAnalyzeService() , Executors.newSingleThreadExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("-packet-statistics-handler-");
                        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                        return thread;
                    }
                }
        )));
        /*
         * bad packet pipe line
         */
        DefaultPipeLine badPacketAnalysisPipeLine = new DefaultPipeLine("bad packet pipeLine");
        badPacketAnalysisPipeLine.addLast(new BadPacketAnalyzeHandler(Executors.newSingleThreadExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("-bad-packet-analyze-");
                        thread.setUncaughtExceptionHandler(COMMON_THREAD_EXCEPTION_HANDLER);
                        return thread;
                    }
                }
        )));
        /*
         * connect pipelines
         */
        packetDecodeHandler.addPipeLine(packetStatisticsPipeLine);
        packetDecodeHandler.addPipeLine(badPacketAnalysisPipeLine);

        /*
         * pcap pipelines
         */
        pcapPipeLine.addLast(packetDecodeHandler);

        pipeLine.addLast(basePacketHandler);
        pipeLine.addLast(packetDecodeHandler);
        pipeLine.addLast(packetSendHandler);
        return pipeLine;
    }

    //run command successfully and get process
    public static void doProcess(Process process) throws IOException {
        //init handlers
        if (pipeLine.pipeLineSize() == 0){
            pipeLine = (DefaultPipeLine) getDefaultPipeLine();
        }
        System.out.println(pipeLine);
        System.out.println(System.currentTimeMillis());
        try (InputStream is = process.getInputStream();BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            for (; ; ) {
                String str = "";
                if ((str = reader.readLine()) != null) {
                    System.out.println(str);
                    if (str.length() > 100)
                        pipeLine.pushDataAtHead(str);
                } else {
                    break;
                }
            }
            System.out.println("quit");
            System.out.println(System.currentTimeMillis());
        } catch (RuntimeException e) {
            logger.debug("error while do processs : " ,  e);
            throw new OpenCaptureServiceException("can not get pipe input of tshark");
        } finally {
            process.destroy();
            System.out.println("process exit value : {} " +  process.exitValue());
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String info = "* 0 run application \n" +
                "* 1 exit application";
        main1(args);
        for (;;){
            System.out.println("////////////////////////////////////////////// \n");
            System.out.println(info);
            System.out.println("//////////////////////////////////////////////");

            if (scanner.nextInt() == 0){
                main1(args);
            }else{
                break;
            }
        }
        System.out.println("saving configuration...");
        saveConfiguration(tsharkConfiguraion,tsharkConfigurationFile);
        System.out.println("saving configuration successfully");
    }
}
