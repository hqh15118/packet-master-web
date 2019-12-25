package com.zjucsc.art_decode.util.mapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DNP3Mapper extends AbstractMapper {

    private ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<String,String>>>
                ip2Type2PointerIndex2IdMap = new ConcurrentHashMap<>();

    public DNP3Mapper(String mapperFilePath) {
        super(mapperFilePath);
    }

    private String lastType;

    @Override
    public void beforeMapper() {
        ip2Type2PointerIndex2IdMap.clear();
    }

    public DNP3Mapper(){}

    @Override
    public void processValidLine(String validLine, String ip) {
        if (validLine == null){
            //just ip
            ip2Type2PointerIndex2IdMap.putIfAbsent(ip,new ConcurrentHashMap<>());
            return;
        }
        Map<String,ConcurrentHashMap<String,String>> type2PointerIndex2IdMap = ip2Type2PointerIndex2IdMap.get(ip);
        if (validLine.trim().startsWith(">")){
            //type
            String type = validLine.replace(">","").trim();
            lastType = type;
            type2PointerIndex2IdMap.putIfAbsent(type,new ConcurrentHashMap<>());
            return;
        }
        String[] pointerIndex2Id = validLine.split(":");
        assert pointerIndex2Id.length == 2;
        Map<String,String> pointerIndex2IdMap = type2PointerIndex2IdMap.get(lastType);
        pointerIndex2IdMap.put(pointerIndex2Id[0].trim(),pointerIndex2Id[1].trim());
    }

    public ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<String,String>>> getIp2Type2PointerIndex2IdMap(){
        return ip2Type2PointerIndex2IdMap;
    }

    @Override
    public String getIDByIPAndTypeAndPointIndex(String ip, String type, String pointIndex) throws MapperFailException{
        if (ip == null){
            return null;
        }
        Map<String,ConcurrentHashMap<String,String>> type2PointerIndex2IdMap = ip2Type2PointerIndex2IdMap.get(ip);
        if (type2PointerIndex2IdMap == null){
            //throw new MapperFailException("DNP3 mapper文件中找不到对应的IP{" + ip + "}");
            return null;
        }
        ConcurrentHashMap<String,String> pointerIndex2IdMap = type2PointerIndex2IdMap.get(type);
        if (pointerIndex2IdMap == null){
            return null;
            //throw new MapperFailException("DNP3 mapper文件中找不到对应的type:IP:{" + ip + "}" + "type:{" + type + "}");
        }
        String id = pointerIndex2IdMap.get(pointIndex);
        if (id == null){
            return null;
            //throw new MapperFailException("DNP3 mapper文件中找不到对应的id:IP:{" + ip + "}" + "type:{" + type + "}" + "pointIndex:{" + pointIndex + "}");
        }
        return id;
    }
}
