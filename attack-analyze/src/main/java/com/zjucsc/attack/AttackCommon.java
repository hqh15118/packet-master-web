package com.zjucsc.attack;

import com.zjucsc.attack.analyze.analyzer_util.CositeDOSAttackAnalyzeList;
import com.zjucsc.attack.analyze.analyzer_util.MultisiteDOSAttackAnalyzeList;
import com.zjucsc.attack.bean.BaseOptAnalyzer;
import com.zjucsc.attack.bean.BaseOptConfig;
import com.zjucsc.attack.base.IOptAttackEntry;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.RedisConfigNotFoundException;
import com.zjucsc.attack.common.ArtAttackAnalyzeTask;
import com.zjucsc.attack.common.AttackCallback;
import com.zjucsc.attack.common.AttackTypePro;
import com.zjucsc.attack.common.CommonAnalyzeTask;
import com.zjucsc.attack.modbus.ModbusOptAnalyzer;
import com.zjucsc.attack.pn_io.PnioOptDecode;
import com.zjucsc.attack.s7comm.S7OptAnalyzer;
import com.zjucsc.common.bean.ThreadPoolInfoWrapper;
import com.zjucsc.common.common_util.CommonUtil;
import com.zjucsc.tshark.Entry;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:00
 */

public class AttackCommon {

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

    private final static Set<ArtAttackAnalyzeConfig> ART_ATTACK_ANALYZE_CONFIGS
            = new ConcurrentSkipListSet<>();

    private final static List<Entry> ATTACK_ENTRY = new ArrayList<Entry>(){
        {
            add(new CositeDOSAttackAnalyzeList());
            add(new MultisiteDOSAttackAnalyzeList());
        }
    };

    private static final HashMap<Integer, BaseOptAnalyzer> OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP
            = new HashMap<>(10);
    /**
     * 初始化
     */
    public static void init(){
        OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.put(2,new S7OptAnalyzer());
        OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.put(1,new ModbusOptAnalyzer());
        OPT_ATTACK_DECODE_CONCURRENT_HASH_MAP.put(23,new PnioOptDecode());
    }

    private static ExecutorService ART_ATTACK_ANALYZE_SERVICE = CommonUtil.getSingleThreadPoolSizeThreadPool(10000,r -> {
        Thread thread = new Thread(r);
        thread.setName("-attack-art-analyze-service-");
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.err.println("error in -attack-art-analyze-service- " + e);
        });
        return thread;
    });

//    private static ExecutorService DOS_ATTACK_ANALYZE_SERVICE = Executors.newFixedThreadPool(1,
//            r -> {
//                Thread thread = new Thread(r);
//                thread.setName("-attack-art-analyze-service-");
//                thread.setUncaughtExceptionHandler((t, e) -> {
//                    System.err.println("error in attack-service-thread " + e);
//                });
//                return thread;
//            });

    private static ExecutorService ART_OPT_ANALYZE_SERVICE = CommonUtil.getSingleThreadPoolSizeThreadPool(10000,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-attack-art-opt-analyze-service-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    System.err.println("-attack-art-opt-analyze-service-" + e);
                });
                return thread;
            });

    public static void registerAttackCallback(AttackCallback attackCallback){
        AttackCommon.attackCallback = attackCallback;
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
            try {
                properties.load(new FileInputStream(file));
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
//    public static void appendDOSAnalyze(final FvDimensionLayer layer){
//        DOS_ATTACK_ANALYZE_SERVICE.execute(new CommonAnalyzeTask(layer));
//    }

    //五元组分析，给CommonAttackTask用的
    public static void doAnalyzeFvDimension(FvDimensionLayer layer){
        for (Entry entry : ATTACK_ENTRY) {
            String description = entry.append(layer);
            if (description!=null){
                AttackCommon.appendFvDimensionError(AttackBean.builder()
                        .attackType(AttackTypePro.DOS_ATTACK)
                        .attackInfo(description)
                        .fvDimension(layer)
                        .build(),layer);
            }
        }
    }

    public static void addArtAttackAnalyzeConfig(ArtAttackAnalyzeConfig artAttackAnalyzeConfig){
        ART_ATTACK_ANALYZE_CONFIGS.remove(artAttackAnalyzeConfig);
        ART_ATTACK_ANALYZE_CONFIGS.add(artAttackAnalyzeConfig);
    }

    public static void removeArtAttackAnalyzeConfig(ArtAttackAnalyzeConfig artAttackAnalyzeConfig){
        ART_ATTACK_ANALYZE_CONFIGS.remove(artAttackAnalyzeConfig);
    }

    public static void changeArtAttackAnalyzeConfigState(int id,boolean enable){
        for (ArtAttackAnalyzeConfig artAttackAnalyzeConfig : ART_ATTACK_ANALYZE_CONFIGS) {
            if (artAttackAnalyzeConfig.getId() == id){
                artAttackAnalyzeConfig.setEnable(enable);
                System.out.println(String.format("成功修改工艺参数ID:[%s]状态state:[%s]",String.valueOf(id),String.valueOf(enable)));
                break;
            }
        }
    }

    /**
     * 工艺参数攻击检测
     * @param techmap
     */
    public static void appendArtAnalyze( Map<String,Float> techmap,FvDimensionLayer layer){
        for (ArtAttackAnalyzeConfig artAttackAnalyzeConfig : ART_ATTACK_ANALYZE_CONFIGS) {
            if (artAttackAnalyzeConfig.isEnable()) {
                doAppendArtAnalyze(artAttackAnalyzeConfig.getExpression(),
                        techmap,
                        artAttackAnalyzeConfig.getDescription(),
                        layer);
            }
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
