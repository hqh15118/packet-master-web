package com.zjucsc.art_decode.base;

import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.common.common_util.CommonUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 泛型化工艺参数
 * @param <T>
 */

public abstract class BaseArtDecode<T extends BaseConfig> implements IArtDecode<T> , IArtEntry{
    private static Logger logger = LoggerFactory.getLogger(BaseArtDecode.class);
    private ConcurrentSkipListSet<T> configs =
            new ConcurrentSkipListSet<>();

    protected void callback(String artName,float value,FvDimensionLayer layer,Object...objs){
        ArtDecodeCommon.validPacketCallback.callback(artName, value, layer,objs);
    }

    public Set<T> getArtConfigs(){
        return configs;
    }

    private ExecutorService executorService = CommonUtil.getSingleThreadPoolSizeThreadPool(10000, r -> {
        Thread thread = new Thread(r);
        thread.setName("-" + BaseArtDecode.this.getClass().getSimpleName() + "-");
        thread.setUncaughtExceptionHandler((t, e) -> {
            logger.error("[{}]线程池工艺参数异常--解析异常",this.getClass().getName(),e);
        });
        return thread;
    },"-" + BaseArtDecode.this.getClass().getSimpleName() + "-thread-pool-");

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
        executorService.execute(() -> {
            for (T config : configs) {
                if (layer.eth_src[0].equals(config.getDeviceMac())
                        || layer.eth_dst[0].equals(config.getDeviceMac())){
                    decode(config,map,payload,layer,objs);
                }
            }
        });
    }
}
