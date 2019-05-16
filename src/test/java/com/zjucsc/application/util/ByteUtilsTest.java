package com.zjucsc.application.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteUtilsTest {

    @Test
    public void bytesToLong(){
        byte[] bytes = new byte[]{1,2,3,4,1,2,3,4};
        System.out.println(ByteUtils.bytesToLong(bytes,0));
    }
}