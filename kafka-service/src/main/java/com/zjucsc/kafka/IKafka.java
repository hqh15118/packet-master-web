package com.zjucsc.kafka;

public interface IKafka<V> {
    /**
     * 发送的消息 value
     * @param v
     */
    void sendMsg(V v);

    void startService();

    void stopService();

    void quitKafka();
}
