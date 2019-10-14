package com.zjucsc.kafka;


import com.zjucsc.common.util.CommonUtil;

import java.util.HashSet;

public class KafkaCommon {
    //监控所有运行中的kafka线程
    private static HashSet<KafkaThread> ALL_REGISTERED_KAFKA_THREAD
            = new HashSet<>();

    static synchronized void register(KafkaThread kafkaThread){
        ALL_REGISTERED_KAFKA_THREAD.add(kafkaThread);
    }

    public static synchronized String getKafkaServiceState(){
        StringBuilder sb = CommonUtil.getGlobalStringBuilder();
        for (KafkaThread kafkaThread : ALL_REGISTERED_KAFKA_THREAD) {
            sb.append(kafkaThread.toString()).append("\n");
        }
        return sb.toString();
    }

    static synchronized void unregister(KafkaThread kafkaThread){
        ALL_REGISTERED_KAFKA_THREAD.remove(kafkaThread);
    }
}
