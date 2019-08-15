package com.zjucsc.tshark;

import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注意，大的在前面，小的在后面
 */
public class FvDimensionList implements Entry {
    //时间大的（越新的）在尾（tail）上，时间小的（旧的）在头上（head）
    //Queue的remove方法移除的是头部，也就是说移除的是最旧的五元组
    private PriorityQueue<FvDimensionLayer> fvDimensionLayers;
    protected FvDimensionLayer tailLayer;   //时间戳最新的五元组，也就是队列的尾部
    public FvDimensionList(){
        fvDimensionLayers = new PriorityQueue<>(1000);
    }
    /**
     * @param layer 五元组
     * @return 针对新增的五元组是否检测到攻击
     */
    @Override
    public synchronized String append(FvDimensionLayer layer){
        fvDimensionLayers.offer(layer);
        if (tailLayer == null){
            tailLayer = layer;
        }else{
            //如果新到的五元组时间戳大于之前的
            if (layer.timeStampInLong > tailLayer.timeStampInLong){
                tailLayer = layer;
            }
        }
        return analyze(fvDimensionLayers,layer);
    }


    /**
     * @param layer 新插入的五元组
     * @param fvDimensionLayers 新增加的五元组
     * @return 是否检测到攻击，true表示检测到，false表示没有检测到
     */
    protected String analyze( Queue<FvDimensionLayer> fvDimensionLayers, FvDimensionLayer layer){
        return null;
    }

    public int getSize(){
        return fvDimensionLayers.size();
    }

}
