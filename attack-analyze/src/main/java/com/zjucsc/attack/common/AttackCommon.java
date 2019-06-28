package com.zjucsc.attack.common;

import com.zjucsc.attack.analyze.analyzer_util.CositeDOSAttackAnalyzeList;
import com.zjucsc.attack.analyze.analyzer_util.MultisiteDOSAttackAnalyzeList;
import com.zjucsc.attack.bean.ArtAttackAnalyzeConfig;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.RedisConfigNotFoundException;
import com.zjucsc.attack.util.Observer;
import com.zjucsc.tshark.Entry;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:00
 */

public class AttackCommon {

    private static AttackCallback attackCallback;

    private final static Set<ArtAttackAnalyzeConfig> ART_ATTACK_ANALYZE_CONFIGS
            = new ConcurrentSkipListSet<>();
    private final static List<Entry> ATTACK_ENTRY = new ArrayList<Entry>(){
        {
            add(new CositeDOSAttackAnalyzeList());
            add(new MultisiteDOSAttackAnalyzeList());
        }
    };
    private static ExecutorService ATTACK_MAIN_SERVICE = Executors.newFixedThreadPool(
            5,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-attack-service-");
                thread.setUncaughtExceptionHandler((t, e) -> System.err.println("error in attack-service-thread " + e));
                return thread;
            }
    );

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

    static Jedis getJedisClient(){
        return JEDIS_HOLDER.jedisPool.getResource();
    }

    /**
     * 添加五元组进行分析
     * @param layer 五元组
     */
    public static void appendFvDimension(final FvDimensionLayer layer){
        ATTACK_MAIN_SERVICE.execute(new AnalyzeTask(layer));
    }

    static void doAnalyzeFvDimension(FvDimensionLayer layer){
        for (Entry entry : ATTACK_ENTRY) {
            String description = entry.append(layer);
            if (description!=null){
                attackCallback.artCallback(AttackBean.builder().attackType(AttackTypePro.DOS).attackInfo(description)
                .data(layer).build());
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
    public static void appendArtAnalyze( Map<String,Float> techmap){
        for (ArtAttackAnalyzeConfig artAttackAnalyzeConfig : ART_ATTACK_ANALYZE_CONFIGS) {
            if (artAttackAnalyzeConfig.isEnable()) {
                doAppendArtAnalyze(artAttackAnalyzeConfig.getExpression(), techmap, artAttackAnalyzeConfig.getDescription(),
                        attackCallback);
            }
        }
    }

    private static void doAppendArtAnalyze(List<String> expression , Map<String,Float> techmap , String description,
                                        AttackCallback attackCallback){
        ATTACK_MAIN_SERVICE.execute(new ArtAttackAnalyzeTask(expression, techmap, description)
        .setCallback(attackCallback));
    }
}
