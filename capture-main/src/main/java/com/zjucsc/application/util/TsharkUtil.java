package com.zjucsc.application.util;

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
}
