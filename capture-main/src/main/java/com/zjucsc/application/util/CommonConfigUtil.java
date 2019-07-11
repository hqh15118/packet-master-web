package com.zjucsc.application.util;

import com.zjucsc.application.domain.bean.FuncodeStatement;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

import static com.zjucsc.application.util.CommonCacheUtil.CONFIGURATION_MAP;
import static com.zjucsc.application.util.CommonCacheUtil.convertIdToName;


@Slf4j
public class CommonConfigUtil {

    public static boolean SHOW_LOG = true;

    public static HashMap<Integer,String> getTargetProtocolAllFuncodeMeaning(String protocol) {
        HashMap<Integer,String> map;
        if ((map = CONFIGURATION_MAP.get(protocol)) == null){
//            throw new ProtocolIdNotValidException("can not find  the target protocol " + protocol + " in CONFIGURATION_MAP \n CONFIGURATION_MAP is : " +
//                    CONFIGURATION_MAP.keySet());
            //System.out.println("can not find  the target protocol in in CONFIGURATION_MAP");
            return null;
        }else{
            return map;
        }
    }

    public static HashMap<Integer,String> getTargetProtocolAllFuncodeMeaning(int protocolId) throws ProtocolIdNotValidException {
        return getTargetProtocolAllFuncodeMeaning(convertIdToName(protocolId));
    }

    public static String getTargetProtocolFuncodeMeaning(String protocol,int funcode) throws ProtocolIdNotValidException {
        String funcodeMeaning;
        HashMap<Integer,String> map = getTargetProtocolAllFuncodeMeaning(protocol);
        if (map==null|| (funcodeMeaning = map.get(funcode)) == null){
            funcodeMeaning = "未知功能码操作";
            if (SHOW_LOG) {
                log.info("can not find protocol {} funcode {} meaning in CONFIGURATION_MAP \n CONFIGURATION_MAP is {}"
                        , protocol, funcode, CONFIGURATION_MAP);
            }
        }
        return funcodeMeaning;
    }

    public static void addProtocolFuncodeMeaning(String protocol , int funcode , String optMeaning){
        HashMap<Integer,String> funcodeMeaning;
        if ((funcodeMeaning = CONFIGURATION_MAP.get(protocol)) == null){
            //CONFIGURATION_MAP中不包含该协议，需要添加一个新的map
            funcodeMeaning = new HashMap<>();
            funcodeMeaning.put(funcode,optMeaning);
            CONFIGURATION_MAP.put(protocol , funcodeMeaning);
            if (SHOW_LOG) {
                log.info("add NEW protocol and NEW funcode meaning \n protocol : {} / funcode {} / optMeaning {} " +
                                "/ CONFIGURATION_MAP :  {}",
                        protocol, funcode, optMeaning, CONFIGURATION_MAP);
            }
        }else{
            synchronized (CommonConfigUtil.class){
                //防止多个线程对同一个hash map并发访问
                //由于只是加了一个写锁，所以读的时候可能会出现滞后的问题
                funcodeMeaning.put(funcode,optMeaning);
                if (SHOW_LOG) {
                    log.info("add NEW funcode meaning \n protocol : {} / funcode {} / optMeaning {} /CONFIGURATION_MAP : {}",
                            protocol, funcode, optMeaning, CONFIGURATION_MAP);
                }
            }
        }
    }

    public static void addProtocolFuncodeMeaning(String protocol , List<FuncodeStatement> funcodeStatements){
        HashMap<Integer,String> funcodeMeaning = null;
        if ((funcodeMeaning = CONFIGURATION_MAP.get(protocol)) == null) {
            //CONFIGURATION_MAP中不包含该协议，需要添加一个新的map
            funcodeMeaning = new HashMap<>();
            CONFIGURATION_MAP.put(protocol, funcodeMeaning);
        }
        synchronized (CommonConfigUtil.class){
            //防止多个线程对同一个hashmap并发访问
            //由于只是加了一个写锁，所以读的时候可能会出现滞后的问题
            for (FuncodeStatement funcodeStatement : funcodeStatements) {
                funcodeMeaning.put(funcodeStatement.getId() , funcodeStatement.getLabel());
            }
            if (SHOW_LOG) {
                log.info("add NEW funcode meaning / protocol : {} , funcodes : {} ", protocol, funcodeStatements);
            }
        }
    }

    public static void addProtocolFuncodeMeaning(String protocol , HashMap<Integer,String> funcodeStatements){
        HashMap<Integer,String> funcodeMeaning = null;
        if ((funcodeMeaning = CONFIGURATION_MAP.get(protocol)) != null) {
            //CONFIGURATION_MAP中不包含该协议，需要添加一个新的map
            CONFIGURATION_MAP.put(protocol, funcodeStatements);
            if (SHOW_LOG) {
                log.info("******* overwrite protocol [{}] funcode meaning : \n before : {} after {} ", protocol, funcodeMeaning, funcodeStatements);
            }
        }else {
            CONFIGURATION_MAP.put(protocol, funcodeStatements);
            if (SHOW_LOG) {
                log.info("******* new protocol [{}] funcode meaning : \n  {} ", protocol, funcodeStatements);
            }
        }
    }
}
