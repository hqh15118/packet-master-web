package com.zjucsc.common_util;

import java.nio.ByteBuffer;

public class Bytecut {
    private static ThreadLocal<ByteBuffer> byteToIntByteBuffer = ThreadLocal.withInitial(() -> {
        return ByteBuffer.allocate(4);
    });

    public Bytecut() {
    }
    public static byte[] Bytecut( byte[] bytes, int offset,int len){
        ByteBuffer buffer = (ByteBuffer) byteToIntByteBuffer.get();
        buffer.put(bytes, offset, len);
        buffer.flip();
        byte[] result = buffer.array();
        buffer.clear();
        return result;
    }
}