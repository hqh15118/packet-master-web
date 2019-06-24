package com.zjucsc.application.config;

import java.util.HashMap;

public class ProtocolCommon {

    private static final HashMap<Integer,String> IEC104_FUNCODE_MAP =
            new HashMap<>();

    public static void init(){
        IEC104_FUNCODE_MAP.put(1,"开启命令");
        IEC104_FUNCODE_MAP.put(2,"开启确认");
        IEC104_FUNCODE_MAP.put(4,"停止命令");
        IEC104_FUNCODE_MAP.put(8,"停止确认");
        IEC104_FUNCODE_MAP.put(16,"测试命令");
        IEC104_FUNCODE_MAP.put(32,"测试确认");
    }

    public static String getIEC104Meaning(int funCode){
        return IEC104_FUNCODE_MAP.get(funCode);
    }
}
