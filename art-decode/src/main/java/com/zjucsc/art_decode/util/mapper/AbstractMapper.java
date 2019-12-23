package com.zjucsc.art_decode.util.mapper;

import com.zjucsc.art_decode.iec104.IEC104DecodeByTshark;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class AbstractMapper implements Mapper {
    private String mapperFilePath;
    public AbstractMapper(String mapperFilePath){
        this.mapperFilePath = mapperFilePath;
    }
    public AbstractMapper(){
        this(null);
    }

    public void setMapperFilePath(String mapperFilePath){
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
        if (!file.exists()){
            throw new MapperFileNotFoundException("路径{" + file.getAbsolutePath() + "}不存在Mapper文件");
        }
        beforeMapper();
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
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
            errorMapper(e);
        }
        afterMapper();
    }

    /**
     * 有效数据
     * @param validLine
     */
    public abstract void processValidLine(String validLine,String ip);

    public void beforeMapper(){}
    public void afterMapper(){}
    public void errorMapper(Exception e){}

    static class MapperFileNotFoundException extends RuntimeException{
        MapperFileNotFoundException(String msg){
            super(msg);
        }
    }
}
