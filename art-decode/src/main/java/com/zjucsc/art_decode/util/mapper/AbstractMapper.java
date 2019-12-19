package com.zjucsc.art_decode.util.mapper;

import java.io.*;

public abstract class AbstractMapper implements Mapper {
    private String mapperFilePath;
    public AbstractMapper(String mapperFilePath){
        this.mapperFilePath = mapperFilePath;
    }

    @Override
    public String getIDByIPAndIOA(String ip, String ioa) {
        throw new InvalidMapperOperation("dnp mapper，无效操作");
    }

    @Override
    public String getIDByIPAndTypeAndPointIndex(String ip, String type, String pointIndex) {
        throw new InvalidMapperOperation("iec104 mapper，无效操作");
    }

    @Override
    public void createMapper() {
        File file = new File(mapperFilePath);
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            String line;
            String ip = null;
            for (;;){
                line = bf.readLine();
                if (line == null){
                    break;
                }
                line = line.trim();
                if (line.startsWith("#") || line.equals("") || line.equals("\n")){
                    continue;
                }
                if (line.trim().startsWith("-")){
                    //ip
                    ip = line.replace("-","").trim();
                    processValidLine(null,ip);
                }else{
                    processValidLine(line,ip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 有效数据
     * @param validLine
     */
    public abstract void processValidLine(String validLine,String ip);
}
