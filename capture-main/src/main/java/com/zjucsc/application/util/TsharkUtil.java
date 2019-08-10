package com.zjucsc.application.util;

import org.pcap4j.core.*;
import org.pcap4j.packet.namednumber.DataLinkType;

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

    public static String analyzeRawData(byte[] rawData) throws PcapNativeException, IOException, NotOpenException {
        File file = new File("temp.pcap");
        file.deleteOnExit();
        file.createNewFile();
        String filePath = file.getAbsolutePath();
        PcapHandle handle = Pcaps.openDead(DataLinkType.EN10MB, 65536);
        PcapDumper pcapDumper = handle.dumpOpen(filePath);
        pcapDumper.dumpRaw(rawData);
        String res =  tsharkAnalyzeTempFile(filePath);
        pcapDumper.close();
        handle.close();
        return res;
    }

    private static String tsharkAnalyzeTempFile(String filePath) throws IOException {
        String command = "tshark -T ek -r " + filePath;
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader bfReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        bfReader.readLine();
        String res = bfReader.readLine();
        bfReader.close();
        process.destroyForcibly();
        return res;
    }
}
