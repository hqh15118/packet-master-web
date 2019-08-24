package com.zjucsc.art_decode.goose;

import com.zjucsc.common.common_util.ByteUtil;

import java.io.*;

public class GooseTestMain {

    public static void main(String[] args) throws IOException {
        File file = new File("goose_pdu");
        BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        String data;
        while ((data = bfReader.readLine()) != null){
            sb.append(data);
        }
        byte[] goosePdu = ByteUtil.hexStringToByteArray(sb.toString());
        //GooseNode gooseNode = GooseDecode.decode(goosePdu);
    }
}
