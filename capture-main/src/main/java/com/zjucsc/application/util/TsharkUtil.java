package com.zjucsc.application.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjucsc.application.domain.non_hessian.CustomPacket;
import com.zjucsc.application.domain.non_hessian.PacketDetail;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.pcap4j.core.*;

import java.io.*;

public class TsharkUtil {

    private static String tsharkPath = null;
    public static void setTsharkPath(String path){
        tsharkPath = path;
    }
    private static String getTsharkPath(){
            return tsharkPath;
    }

    public static String checkTsharkValid(){
        String str = System.getenv("PATH");
        String[] allPathVar = null;
        if (str.contains(";")){
            allPathVar = str.split(";");
        }
        assert allPathVar!=null;
        for (String var : allPathVar) {
            if (var.toLowerCase().contains("wireshark")){
                return var;
            }
        }
        return null;
    }

    public static boolean addTsharkPlugin() throws IOException {
        String pluginPath = getTsharkPath() + "\\plugins\\mywireshark_plugin.lua";
        File file = new File(pluginPath);
        if (!file.exists()){
            if(file.createNewFile()){
                System.out.println("未检测到tshark【mywireshark_plugin.lua】插件，已自动创建");
            }else{
                System.err.println("未检测到tshark【mywireshark_plugin.lua】插件，创建失败，检查权限");
                return false;
            }
            InputStream is = TsharkUtil.class.getResourceAsStream("/mywireshark_plugin.lua");
            int length = is.available();
            byte[] data = new byte[length];
            int readLength = is.read(data);
            assert readLength == length;
            OutputStream os = new FileOutputStream(file);
            os.write(data);
            os.flush();
        }else{
            System.out.println("检测到tshark插件>>");
        }
        return true;
    }

    private static TsharkThread tsharkThread = null;
    private static PcapHandle pcapHandle = null;
    public synchronized static void startHistoryPacketAnalyzeThread(String command , String virtualName) throws PcapNativeException {
        if (tsharkThread == null){
            tsharkThread = new TsharkThread(command);
            tsharkThread.start();
        }
        if (pcapHandle == null){
            PcapNetworkInterface pcapNetworkInterface = Pcaps.getDevByName(virtualName);
            pcapHandle = pcapNetworkInterface.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS
                    ,1000);
        }
    }

    public static void analyzeHistoryPacket(CustomPacket customPacket, TsharkThread.HistoryPacketCallback historyPacketCallback) throws PcapNativeException, NotOpenException {
        pcapHandle.sendPacket(customPacket);
        tsharkThread.setHistoryPacketCallback(historyPacketCallback);
    }

    public static class TsharkThread extends Thread{
        private String command;
        private HistoryPacketCallback historyPacketCallback;
        TsharkThread(String command) {
            this.command = command;
        }

        @Override
        public void run() {
            BufferedReader bfReader = null;
            try {
                Process process = Runtime.getRuntime().exec(command);
                bfReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                for (;;){
                    String content = bfReader.readLine();
                    if (content.length() > 90){
                        PacketDetail detail = JSON.parseObject(content,PacketDetail.class);
                        if (historyPacketCallback!=null) {
                            historyPacketCallback.callback(detail.getLayers().getExt().getExt_custom_ext_raw_data(), content);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (bfReader != null) {
                    try {
                        bfReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setHistoryPacketCallback(HistoryPacketCallback historyPacketCallback){
            this.historyPacketCallback = historyPacketCallback;
        }

        public interface HistoryPacketCallback{
            void callback(String rawData,String content);
        }
    }

}
