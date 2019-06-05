package com.zjucsc.art_decode.base;

import com.zjucsc.art_decode.artconfig.BaseConfig;

import java.util.Map;

/**
 * 泛型化工艺参数
 * @param <T>
 */
public abstract class BaseArtDecode<A,T extends BaseConfig> implements IArtDecode {
    protected A decoder;

    public BaseArtDecode(A decoder) {
        this.decoder = decoder;
    }

    /**
     * 添加工艺参数配置
     * @param a 工艺参数配置
     */
    public abstract void addArtConfig(T a);

    /**
     * 删除工艺参数配置
     * @param baseConfig 工艺参数
     */
    public abstract void deleteArtConfig(BaseConfig baseConfig);
}
