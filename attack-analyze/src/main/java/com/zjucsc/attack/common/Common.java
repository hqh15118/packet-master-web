package com.zjucsc.attack.common;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.bean.RedisConfigNotFoundException;
import com.zjucsc.attack.util.IObservable;
import com.zjucsc.attack.util.Observer;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import redis.clients.jedis.Jedis;

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

    private ExecutorService service = Executors.newSingleThreadExecutor(
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
        static Jedis jedis;
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
                jedis = new Jedis(host, Integer.valueOf(port), 10000);
            }else{
                System.err.println("attack-service jedis未正确初始化，port参数为null");
            }
        }
    }

    public static  void registerObservable(Collection<IObservable<AttackBean>> observables){
        observer.register(observables);
    }

    static void updateAll(AttackBean attackBean){
        observer.updateAll(attackBean);
    }

    static Jedis getJedisClient(){
        return JEDIS_HOLDER.jedis;
    }


    public static void addFvDimension(FvDimensionLayer layer){

    }
}
