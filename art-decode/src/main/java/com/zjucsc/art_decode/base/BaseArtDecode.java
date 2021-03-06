package com.zjucsc.art_decode.base;

import com.alibaba.fastjson.JSON;
import com.zjucsc.art_decode.ArtDecodeUtil;
import com.zjucsc.art_decode.iec104.IEC104Decode;
import com.zjucsc.common.util.CommonUtil;
import com.zjucsc.common.util.ExceptionSafeRunnable;
import com.zjucsc.common.util.ThreadPoolUtil;
import com.zjucsc.tshark.packets.Dnp3_0Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.IEC104Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

/**
 * 泛型化工艺参数
 * @param <T>
 */

public abstract class BaseArtDecode<T extends BaseConfig> implements IArtDecode<T> , IArtEntry{
    private static Logger logger = LoggerFactory.getLogger(BaseArtDecode.class);

    private ConcurrentSkipListSet<T> configs = new ConcurrentSkipListSet<>();

    protected void callback(String artName,float value,FvDimensionLayer layer,Object...objs){
        if (ArtDecodeUtil.validPacketCallback!=null) {
            ArtDecodeUtil.validPacketCallback.callback(artName, value, layer, objs);
        }else{
            System.err.println("valid packet callback not register!");
        }
    }

    public Set<T> getArtConfigs(){
        return configs;
    }

    private ExecutorService executorService = ThreadPoolUtil.getFixThreadPoolSizeThreadPool(1, 100000, r -> {
                Thread thread = new Thread(r);
                thread.setName("-" + BaseArtDecode.this.getClass().getSimpleName() + "-");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    logger.error("[{}]线程池工艺参数异常--解析异常", this.getClass().getName(), e);
                });
                return thread;
            }, "-" + BaseArtDecode.this.getClass().getSimpleName() + "-thread-pool-");

    /**
     * 添加工艺参数配置
     * @param a 工艺参数配置
     */
    public void addArtConfig(T a){
        configs.remove(a);
        configs.add(a);
    }

    /**
     * 删除工艺参数配置
     * @param t 工艺参数
     */
    public void deleteArtConfig(T t){
        configs.remove(t);
    }

    public void removeAllArtConfig(){
        configs = new ConcurrentSkipListSet<>();
    }

    @Override
    public void doDecode(Map<String, Float> map, byte[] payload, FvDimensionLayer layer,Object... objs) {
        if (configs.size() == 0){
            return;
        }
        executorService.execute(new ExceptionSafeRunnable<FvDimensionLayer>(layer) {
            @Override
            public void run(FvDimensionLayer layer) {
                int configIndex = 0;
                for (T config : configs) {
                    if (layer instanceof IEC104Packet.LayersBean || layer instanceof Dnp3_0Packet.LayersBean){
                        decode(config, map, payload, layer, configIndex);
                        configIndex++;
                        continue;
                    }
                    if (layer.eth_src[0].equals(config.getDeviceMac()) || layer.eth_dst[0].equals(config.getDeviceMac())){
                        decode(config,map,payload,layer,objs);
                    }
                    configIndex++;
                }
            }

            @Override
            protected void exceptionRiseProcess(FvDimensionLayer layer, RuntimeException e) {
                Thread thread = Thread.currentThread();
                logger.error("ERROR Thread [name = {},priority = {},groupName = {}]," +
                                "layer-data = {}" ,
                        thread.getName(),thread.getPriority(),thread.getThreadGroup().getName() ,JSON.toJSON(layer) , e);
            }
        });
    }
}
