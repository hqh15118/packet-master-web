package com.zjucsc.attack.common;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.RedisConfigNotFoundException;
import com.zjucsc.attack.util.IObservable;
import com.zjucsc.attack.util.Observer;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:00
 */
public class Common {
    private static Observer observer = new Observer();

    private static ExecutorService ATTACK_MAIN_SERVICE = Executors.newFixedThreadPool(
            5,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("attack-service-");
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.err.println("error in attack-service-thread " + e);
                    }
                });
                return thread;
            }
    );

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

    public static  void registerObservable(Collection<IObservable<AttackBean>> observables){
        observer.register(observables);
    }

    public static void updateAll(AttackBean attackBean){
        observer.updateAll(attackBean);
    }

    static Jedis getJedisClient(){
        return JEDIS_HOLDER.jedisPool.getResource();
    }

    /**
     * 添加五元组进行分析
     * @param layer
     */
    public static void appendFvDimension(final FvDimensionLayer layer){
        ATTACK_MAIN_SERVICE.execute(new AnalyzeTask(layer));
    }
}
