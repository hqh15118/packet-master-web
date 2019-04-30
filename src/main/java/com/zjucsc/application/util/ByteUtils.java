package com.zjucsc.application.util;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class ByteUtils {

    private static ThreadLocal<ByteBuffer> byteToLongByteBuffer = ThreadLocal.withInitial(() -> ByteBuffer.allocate(Long.BYTES));
    private static ThreadLocal<ByteBuffer> byteToShortByteBuffer = ThreadLocal.withInitial(() -> ByteBuffer.allocate(Short.BYTES));

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = byteToLongByteBuffer.get();
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static byte[] shortToByte(short s){
        ByteBuffer buffer = byteToShortByteBuffer.get();
        buffer.putShort(0,s);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes,int offset,int length) {
        ByteBuffer buffer = byteToLongByteBuffer.get();
        buffer.put(bytes, offset, length);
        buffer.flip();//need flip
        long result =  buffer.getLong();
        buffer.clear();
        return result;
    }

    public static short bytesToShort(byte[] bytes,int offset,int length) {
        ByteBuffer buffer = byteToShortByteBuffer.get();
        buffer.put(bytes, offset, length);
        buffer.flip();//need flip
        short result =  buffer.getShort();
        buffer.clear();
        return result;
    }

    /**
     * 每一个bytes应该都是满的
     * @param allLength bytes的总长度必须等于allLength
     * @param bytes
     * @return
     */
    public static byte[] contractBytes(int allLength,byte[]... bytes){
         byte[] newBytes = new byte[allLength];
         return contractByte(newBytes, Arrays.asList(bytes));
    }

    public static byte[] contractByte(byte[] newBytes, List<byte[]> bytes){
        int offset = 0;
        for (byte[] bytes1 : bytes){
            System.arraycopy(bytes1,0,newBytes,offset,bytes1.length);
            offset+=bytes1.length;
        }
        return newBytes;
    }

    public static byte[] contractByteByWrapper(int allLength,WrapperData... datas){
        byte[] newBytes = new byte[allLength];
        int offset = 0;
        for (WrapperData data : datas){
            System.arraycopy(data.realData,0,newBytes,offset,data.availableLength);
            offset+=data.availableLength;
        }
        return newBytes;
    }

    public static class WrapperData{
        public byte[] realData;
        public int availableLength;
        public WrapperData(byte[] realData,int availableLength){
            this.realData = realData;
            this.availableLength = availableLength;
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHex(byte[]... data){
        StringBuilder sb = new StringBuilder();
        for (byte[] b : data){
            for (byte var : b){
                sb.append(Integer.toHexString(Byte.toUnsignedInt(var)));
            }
        }
        return sb.toString();
    }

    public static String byteArrayToHex(List<byte[]> data){
        StringBuilder sb = new StringBuilder();
        for (byte[] b : data){
            for (byte var : b){
                sb.append(Integer.toHexString(Byte.toUnsignedInt(var)));
            }
        }
        return sb.toString();
    }
}
