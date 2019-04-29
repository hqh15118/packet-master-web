package com.zjucsc.application.tshark.decode;

public interface PipeLine {
    void addLast(AbstractHandler handler);

    /**
     * 压入第一个数据
     * @param t 压入的数据
     */
    void pushDataAtHead(Object t);
}
