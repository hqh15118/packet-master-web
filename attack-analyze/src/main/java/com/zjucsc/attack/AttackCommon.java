package com.zjucsc.attack;

import com.sun.javaws.CacheUtil;
import com.zjucsc.attack.analyze.analyzer_util.CositeDosAttackAnalyzeList;
import com.zjucsc.attack.analyze.analyzer_util.MultisiteDosAttackAnalyzeList;
import com.zjucsc.attack.base.AbstractOptCommandAttackEntry;
import com.zjucsc.attack.base.AnalyzePoolEntry;
import com.zjucsc.attack.base.AnalyzePoolEntryImpl;
import com.zjucsc.attack.bean.*;
import com.zjucsc.attack.base.IOptAttackEntry;
import com.zjucsc.attack.common.*;
import com.zjucsc.attack.modbus.ModbusOptAnalyzer;
import com.zjucsc.attack.pn_io.PnioOptDecode;
import com.zjucsc.attack.s7comm.S7OptAnalyzer;
import com.zjucsc.attack.s7comm.S7OptCommandConfig;
import com.zjucsc.attack.s7comm.S7OptName;
import com.zjucsc.attack.s7comm.s7Opdecode;
import com.zjucsc.attack.util.ArtOptAttackUtil;
import com.zjucsc.common.bean.ThreadPoolInfoWrapper;
import com.zjucsc.common.common_util.CommonUtil;
import com.zjucsc.tshark.Entry;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:00
 */

public class AttackCommon {
    private static Logger logger = LoggerFactory.getLogger(AttackCommon.class);
    /**
     * 获取Attack-Analyze服务的线程池任务信息
     * @return 线程池任务信息
     */
    public static List<ThreadPoolInfoWrapper> getAttackMainServiceTaskSize(){
        List<ThreadPoolInfoWrapper> threadPoolInfoWrappers = new ArrayList<>();
        threadPoolInfoWrappers.add(new ThreadPoolInfoWrapper("ART_ATTACK_ANALYZE_SERVICE", ((ThreadPoolExecutor) ART_ATTACK_ANALYZE_SERVICE).getQueue().size()));
        threadPoolInfoWrappers.add(new ThreadPoolInfoWrapper("ART_OPT_ANALYZE_SERVICE", ((ThreadPoolExecutor) ART_OPT_ANALYZE_SERVICE).getQueue().size()));
        return threadPoolInfoWrappers;
    }

    private static AttackCallback attackCallback;
    private static CommandCallback commandCallback;
    /**
     * 【协议名字，该协议对应的公式】
     */
    private final static Map<String,Set<ArtAttackAnalyzeConfig>> ART_ATTACK_ANALYZE_CONFIGS
            = new ConcurrentHashMap<>();

    private static final HashMap<Integer, BaseOptAnalyzer> OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP
            = new HashMap<>(10);

    private static final HashMap<String, AbstractOptCommandAttackEntry> COMMAND_DECODE_HASH_MAP =
            new HashMap<>(10);
    /**
     * 初始化
     */
    public static void init(){
        OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.put(2,new S7OptAnalyzer());
        OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.put(1,new ModbusOptAnalyzer());
        OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.put(23,new PnioOptDecode());

        //init command decode hash_map
        COMMAND_DECODE_HASH_MAP.put("s7comm",new s7Opdecode());
    }

    private static ExecutorService ART_COMMAND_ATTACK_ANALYZE_SERVICE = CommonUtil.getSingleThreadPoolSizeThreadPool(10000,r -> {
        Thread thread = new Thread(r);
        thread.setName("-attack-command-art-analyze-service-");
        thread.setUncaughtExceptionHandler((t, e) -> {
            logger.error("error in -attack-command-art-analyze-service-",e);
        });
        return thread;
    },"ART_COMMAND_ATTACK_ANALYZE_SERVICE");

    private static ExecutorService ART_ATTACK_ANALYZE_SERVICE = CommonUtil.getSingleThreadPoolSizeThreadPool(10000,r -> {
        Thread thread = new Thread(r);
        thread.setName("-attack-art-analyze-service-");
        thread.setUncaughtExceptionHandler((t, e) -> {
            logger.error("error in -attack-art-analyze-service-",e);
        });
        return thread;
    },"ART_ATTACK_ANALYZE_SERVICE");

    public static final ConcurrentHashMap<String, AnalyzePoolEntry> ANALYZE_POOL_ENTRY_CONCURRENT_HASH_MAP =
            new ConcurrentHashMap<>();

    public static boolean addOrUpdateDosAnalyzePoolEntry(String deviceTag, DosConfig dosConfig){
        AnalyzePoolEntry analyzePoolEntry = ANALYZE_POOL_ENTRY_CONCURRENT_HASH_MAP.computeIfAbsent(deviceTag, s -> new AnalyzePoolEntryImpl());
        return analyzePoolEntry.addDosAnalyzer(dosConfig);
    }
    public static void removeDosAnalyzePoolEntry(String deviceTag,String protocol){
        AnalyzePoolEntry analyzePoolEntry = ANALYZE_POOL_ENTRY_CONCURRENT_HASH_MAP.get(deviceTag);
        analyzePoolEntry.removeDosAnalyzer(protocol);
    }
    public static void disableDeviceDosAnalyzePoolEntry(String deviceTag){
        ANALYZE_POOL_ENTRY_CONCURRENT_HASH_MAP.get(deviceTag);
    }
    public static void changeDosConfig(String deviceTag,String protocol,boolean enable){
        AnalyzePoolEntry analyzePoolEntry = ANALYZE_POOL_ENTRY_CONCURRENT_HASH_MAP.get(deviceTag);
        analyzePoolEntry.enableDosAnalyzer(enable,protocol);
    }
    private static ExecutorService DOS_ATTACK_ANALYZE_SERVICE = Executors.newFixedThreadPool(1,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-attack-art-analyze-service-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    logger.error("error in attack-service-thread ",e);
                });
                return thread;
            });

    private static ExecutorService OPT_COMMAND_ANALYZE_SERVICE = Executors.newFixedThreadPool(1,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-opt-command-analyze-service-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    logger.error("error in opt-command-service-thread ",e);
                });
                return thread;
            });

    private static ExecutorService OPT_COMMAND_ATTACK_ANALYZE_SERVICE = Executors.newFixedThreadPool(1,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-opt-command-attack-analyze-service-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    logger.error("error in opt-command-attack-service-thread ",e);
                });
                return thread;
            });

    private static ExecutorService ART_OPT_ANALYZE_SERVICE = CommonUtil.getSingleThreadPoolSizeThreadPool(10000,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-attack-art-opt-analyze-service-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    logger.error("-attack-art-opt-analyze-service-",e);
                });
                return thread;
            },"ART_OPT_ANALYZE_SERVICE");

    public static void registerAttackCallback(AttackCallback attackCallback,CommandCallback commandCallback){
        AttackCommon.attackCallback = attackCallback;
        AttackCommon.commandCallback = commandCallback;
    }

    //懒加载单例模式
    private static final class JEDIS_HOLDER{
        static JedisPool jedisPool;
        static {
            File file = new File("config/attack-redis.properties");
            if (!file.exists()){
                try {
                    throw new RedisConfigNotFoundException("文件：根目录/config/attack-redis.properties不存在");
                } catch (RedisConfigNotFoundException e) {
                    e.printStackTrace();
                }
            }
            Properties properties = new Properties();
            String host = null, port = null;
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                properties.load(is);
                host = properties.getProperty("redis_host");
                if (host == null) {
                    throw new RedisConfigNotFoundException("文件：根目录/config/attack-redis.properties不存在[redis_host]属性");
                }
                port = properties.getProperty("redis_port");
                if (port == null) {
                    throw new RedisConfigNotFoundException("文件：根目录/config/attack-redis.properties不存在[redis_port]属性");
                }
            } catch (IOException | RedisConfigNotFoundException e) {
                e.printStackTrace();
            }finally {
                if (is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (port != null) {
                GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                jedisPool = new JedisPool(poolConfig,host,Integer.valueOf(port));
            }else{
                System.err.println("attack-service jedis未正确初始化，port参数为null");
            }
        }
    }

    public static Jedis getJedisClient(){
        return JEDIS_HOLDER.jedisPool.getResource();
    }

    /**
     * 添加五元组进行分析
     * DOS 2019/6/28
     * @param layer 五元组
     */
    public static void appendDOSAnalyze(final FvDimensionLayer layer , String deviceTag){
        final AnalyzePoolEntry analyzePoolEntry = ANALYZE_POOL_ENTRY_CONCURRENT_HASH_MAP.get(deviceTag);
        if (analyzePoolEntry!=null){
            DOS_ATTACK_ANALYZE_SERVICE.execute(() -> {
                try {
                    String dosMsg = analyzePoolEntry.append(layer);
                    if (dosMsg!=null){
                        AttackBean attackBean = AttackBean.builder().attackInfo(layer.protocol)
                                .fvDimension(layer).build();
                        if (dosMsg.equals("同源DOS攻击")){
                            attackBean.setAttackType(AttackTypePro.COSITE_DOS_ATTACK);
                        }else{
                            attackBean.setAttackType(AttackTypePro.MULTI_DOS_ATTACK);
                        }
                        AttackCommon.appendFvDimensionError(attackBean,layer);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public static void addArtAttackAnalyzeConfig(String protocol,ArtAttackAnalyzeConfig artAttackAnalyzeConfig){
        Set<ArtAttackAnalyzeConfig> configSet = ART_ATTACK_ANALYZE_CONFIGS.putIfAbsent(protocol,new ConcurrentSkipListSet<>());
        configSet = ART_ATTACK_ANALYZE_CONFIGS.get(protocol);
        if (configSet!=null){
            configSet.remove(artAttackAnalyzeConfig);
            configSet.add(artAttackAnalyzeConfig);
        }
    }

    public static void appendOptCommandDecode(FvDimensionLayer layer){
        final Set<S7OptName> s7OptNameSet = ArtOptAttackUtil.getS7OptNameSetByProtocol(layer.protocol);
        if (s7OptNameSet != null && s7OptNameSet.size() > 0){
            AbstractOptCommandAttackEntry abstractOptCommandAttackEntry = COMMAND_DECODE_HASH_MAP.get(layer.protocol);
            if (abstractOptCommandAttackEntry!=null){
                OPT_COMMAND_ANALYZE_SERVICE.execute(()->{
                    for (S7OptName s7OptName : s7OptNameSet) {
                        abstractOptCommandAttackEntry.analyze(layer,s7OptName);
                    }
                });
            }
        }
    }

    public static Map<String,Set<ArtAttackAnalyzeConfig>> getArtExpressionByProtocol(){
        return  ART_ATTACK_ANALYZE_CONFIGS;
    }

    public static void removeAllArtAttackAnalyzeConfig(String protocol){
        Set<ArtAttackAnalyzeConfig> configSet = ART_ATTACK_ANALYZE_CONFIGS.get(protocol);
        if (configSet!=null){
            configSet.removeAll(configSet);
        }
    }

    public static void removeArtAttackAnalyzeConfig(String protocol,ArtAttackAnalyzeConfig artAttackAnalyzeConfig){
        Set<ArtAttackAnalyzeConfig> configSet = ART_ATTACK_ANALYZE_CONFIGS.get(protocol);
        if (configSet!=null){
            configSet.remove(artAttackAnalyzeConfig);
        }
    }


    /**
     * 工艺参数攻击检测
     * @param techmap
     */
    public static void appendArtAnalyze( Map<String,Float> techmap,FvDimensionLayer layer){
        Set<ArtAttackAnalyzeConfig> expressionSet = ART_ATTACK_ANALYZE_CONFIGS.get(layer.protocol);
        if (expressionSet!=null) {
            for (ArtAttackAnalyzeConfig artAttackAnalyzeConfig : expressionSet) {
                if (artAttackAnalyzeConfig.isEnable()) {
                    doAppendArtAnalyze(artAttackAnalyzeConfig.getExpression(),
                            techmap,
                            artAttackAnalyzeConfig.getDescription(),
                            layer);
                }
            }
        }
    }

    /**
     * 工艺参数操作指令公式分析
     * @param layer
     * @param map
     */
    public static void appendArtCommandAnalyze(String opName,FvDimensionLayer layer , Map<String,Float> map){
        S7OptCommandConfig s7OptCommandConfig = ArtOptAttackUtil.getOptConfigByOpName(opName);
        if (s7OptCommandConfig!=null){
            ART_COMMAND_ATTACK_ANALYZE_SERVICE.execute(() -> {
                    String info = ArtAttackAnalyzeUtil.attackDecode(s7OptCommandConfig.getRuleString(),
                            map,s7OptCommandConfig.getDescribe());
                    if (info!=null){
                        appendFvDimensionError(AttackBean.builder().fvDimension(layer)
                        .attackInfo(s7OptCommandConfig.getDescribe()).attackType(AttackTypePro.COMMAND_ATTACK)
                        .build(),layer);
                }
            });
        }
    }

    private static void doAppendArtAnalyze(List<String> expression ,
                                           Map<String,Float> techmap ,
                                           String description,
                                           FvDimensionLayer layer){
        ART_ATTACK_ANALYZE_SERVICE.execute(new ArtAttackAnalyzeTask(expression, techmap, description , layer));
    }

    /**
     * 添加异常，回调
     * @param attackBean
     */
    public static void appendFvDimensionError(AttackBean attackBean,FvDimensionLayer layer){
        attackCallback.artCallback(attackBean,layer);
    }

    public static void appendCommandFvDimensionPacket(String command , FvDimensionLayer layer
    ,Object...objs){
        commandCallback.commandCallback(layer, command, objs);
    }

    /**
     * 操作指令检测识别
     */
    public static void appendOptAnalyze(Map<String,Float> techmap,FvDimensionLayer layer,int protocolId,Object...objs){
        IOptAttackEntry iOptAttackEntry = OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.get(protocolId);
        ART_OPT_ANALYZE_SERVICE.execute(() -> {
            if (iOptAttackEntry!=null){
                AttackBean attackBean = iOptAttackEntry.analyze(layer,techmap);
                if (attackBean!=null){
                    AttackCommon.appendFvDimensionError(attackBean,layer);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void addOptAttackConfig(BaseOptConfig baseOptConfig){
        BaseOptAnalyzer baseOptAnalyzer = OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.get(baseOptConfig.getProtocolId());
        if (baseOptAnalyzer!=null){
            baseOptAnalyzer.addOptAnalyzeConfig(baseOptConfig);
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeArtOptAttackConfig(BaseOptConfig baseOptConfig){
        BaseOptAnalyzer baseOptAnalyzer = OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.get(baseOptConfig.getProtocolId());
        if (baseOptAnalyzer!=null){
            baseOptAnalyzer.deleteOptAnalyzeConfig(baseOptConfig);
        }
    }


    @SuppressWarnings("unchecked")
    public static void updateOptAttackConfig(BaseOptConfig baseOptConfig){
        BaseOptAnalyzer baseOptAnalyzer = OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.get(baseOptConfig.getProtocolId());
        if (baseOptAnalyzer!=null){
            baseOptAnalyzer.updateOptAnalyzeConfig(baseOptConfig);
        }
    }
    @SuppressWarnings("unchecked")
    public static void deleteOptAttackConfig(BaseOptConfig baseOptConfig){
        BaseOptAnalyzer baseOptAnalyzer = OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.get(baseOptConfig.getProtocolId());
        if (baseOptAnalyzer!=null){
            baseOptAnalyzer.deleteOptAnalyzeConfig(baseOptConfig);
        }
    }

}
