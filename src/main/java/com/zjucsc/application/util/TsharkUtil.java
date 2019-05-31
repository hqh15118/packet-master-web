package com.zjucsc.application.util;

public class TsharkUtil {

    public static String getTsharkPath(){
            return null;
    }


    public static String checkTsharkValid(){
        String str = System.getenv("PATH");
        String[] allPathVar = null;
        if (str.contains(":")){
            allPathVar = str.split(":");
        }
        assert allPathVar!=null;
        for (String var : allPathVar) {
            if (var.toLowerCase().contains("wireshark")){
                return var;
            }
        }
        return null;
    }
}
