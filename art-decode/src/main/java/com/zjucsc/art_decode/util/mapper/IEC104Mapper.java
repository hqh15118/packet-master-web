package com.zjucsc.art_decode.util.mapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IEC104Mapper extends AbstractMapper {

    /*ip - {ioa-id}/{id-ioa}*/
    private ConcurrentHashMap<String, HashBiMap<String,String>> ipAndIoa2IDMap
            = new ConcurrentHashMap<>();

    public IEC104Mapper(String mapperFilePath) {
        super(mapperFilePath);
    }

    public IEC104Mapper(){

    }

    @Override
    public void beforeMapper() {
        ipAndIoa2IDMap.clear();
    }

    @Override
    public void processValidLine(String validLine, String ip) {
        if (validLine == null) {
            //just ip
            ipAndIoa2IDMap.putIfAbsent(ip, HashBiMap.create());
            return;
        }
        String[] ioaAndId = validLine.split(":");
        HashBiMap<String, String> ioa2IdMap = ipAndIoa2IDMap.get(ip);
        ioa2IdMap.forcePut(ioaAndId[0].trim(), ioaAndId[1].trim());
    }

    /**
     * @param ip
     * @param ioa
     * @return
     * @throws MapperFailException
     */
    @Override
    public String getIDByIPAndIOA(String ip, String ioa) throws MapperFailException {
        return getData(ip, ioa,false);
    }

    public String getIOAByIPAndID(String ip,String id){
        return getData(ip, id,true);
    }

    private String getData(String ip, String idOrIoa,boolean inverse) {
        BiMap<String, String> ioa2IdMap = ipAndIoa2IDMap.get(ip);
        if (ioa2IdMap == null) {
            //IP是必须要有的，不然就是错误
            //throw new MapperFailException("IEC104 mapper文件中找不到对应的IP{" + ip + "}");
            return null;
        }
        String data = null;
        if (inverse) {
            data = ioa2IdMap.inverse().get(idOrIoa);
        }else{
            data = ioa2IdMap.get(idOrIoa);
        }
        if (data == null) {
            //ioa，可能是没有配置进去的，因为只会配置开关的地址进去
            //如果是模拟量，可以随便配一个公共的地址统一一下
            //
            //throw new MapperFailException("IEC104 mapper文件中找不到对应的id:IP:{" + ip + "}" + "ioa:{" + ioa + "}");
            return null;
        }
        return data;
    }

    public ConcurrentHashMap<String, HashBiMap<String,String>> getIpAndIoa2IDMap() {
        return ipAndIoa2IDMap;
    }
}
