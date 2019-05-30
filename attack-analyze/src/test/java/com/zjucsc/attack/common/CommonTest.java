package com.zjucsc.attack.common;

import com.zjucsc.attack.util.AttackAnalyzeUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 22:42
 */
public class CommonTest {

    @Test
    public void jedisTest(){
        Jedis jedis = Common.getJedisClient();
        System.out.println(jedis.get("test"));//value
        System.out.println(jedis.get("test1"));//null
        System.out.println(jedis.getSet("test","value1"));
        System.out.println(jedis.get("test"));//value1
    }

    @Test
    public void jedisTestZset(){
        Jedis jedis = Common.getJedisClient();
        jedis.del("zset");
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                jedis.zadd("zset",j>>4 + i,String.valueOf(i));
                long index = jedis.zrank("zset",String.valueOf(i));
                Set<String> strings = jedis.zrange("zset",index-1,index+1);
                if (strings.size() == 1){

                }else if(strings.size() == 2){
                    Object[] strings1 = strings.toArray();
                    AttackAnalyzeUtil.
                            timeDifferenceAnalyze((String) strings1[0],(String) strings1[1],10);
                }else if(strings.size() == 3){
                    Object[] strings1 = strings.toArray();
                    AttackAnalyzeUtil.
                            timeDifferenceAnalyze((String) strings1[0],(String) strings1[1],10);
                    AttackAnalyzeUtil.
                            timeDifferenceAnalyze((String) strings1[1],(String) strings1[2],10);
                }
                long size = jedis.zcard("zset");
                if (size >= 10){
                    jedis.zremrangeByRank("zset",0,4);
                }
            }
        }

        System.out.println(System.currentTimeMillis() - time1);

    }



}