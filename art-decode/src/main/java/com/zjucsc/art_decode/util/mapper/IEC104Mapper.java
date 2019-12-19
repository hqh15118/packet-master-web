package com.zjucsc.art_decode.util.mapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IEC104Mapper extends AbstractMapper {

    private ConcurrentHashMap<String,ConcurrentHashMap<String,String>> ipAndIoa2IDMap
            = new ConcurrentHashMap<>();

    public IEC104Mapper(String mapperFilePath) {
        super(mapperFilePath);
    }

    @Override
    public void processValidLine(String validLine,String ip) {
        if (validLine == null){
            //just ip
            ipAndIoa2IDMap.putIfAbsent(ip,new ConcurrentHashMap<>());
            return;
        }
        String[] ioaAndId = validLine.split(":");
        Map<String,String> ioa2IdMap = ipAndIoa2IDMap.get(ip);
        ioa2IdMap.put(ioaAndId[0].trim(),ioaAndId[1].trim());
    }

    @Override
    public String getIDByIPAndIOA(String ip, String ioa) throws MapperFailException{
        Map<String,String> ioa2IdMap = ipAndIoa2IDMap.get(ip);
        if (ioa2IdMap == null){
            throw new MapperFailException("IEC104 mapper文件中找不到对应的IP{" + ip + "}");
        }
        String id = ioa2IdMap.get(ioa);
        if (id == null){
            throw new MapperFailException("IEC104 mapper文件中找不到对应的id:IP:{" + ip + "}" + "ioa:{" + ioa + "}");
        }
        return id;
    }

    public ConcurrentHashMap<String,ConcurrentHashMap<String,String>> getIpAndIoa2IDMap(){
        return ipAndIoa2IDMap;
    }
}
