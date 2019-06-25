package com.zjucsc.tshark;

import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.LinkedList;

/**
 * 注意，大的在前面，小的在后面
 */
public class FvDimensionList implements Entry {
    private volatile int cap = -1;
    private LinkedList<FvDimensionLayer> fvDimensionLayers;
    private long centerCount = 0;
    private long allCount = 0;
    private int  first = 0;
    private int index;
    public FvDimensionList(){
        fvDimensionLayers = new LinkedList<>();
    }
    public FvDimensionList(int cap){
        fvDimensionLayers = new LinkedList<>();
        this.cap = cap;
    }

    /**
     * @param layer 五元组
     * @return 针对新增的五元组是否检测到攻击
     */
    @Override
    public synchronized boolean append(FvDimensionLayer layer){
        index = 0;
        if (fvDimensionLayers.size() == 0){
            fvDimensionLayers.addLast(layer);
            return false;
        }
        if (fvDimensionLayers.getLast().timeStampInLong < layer.timeStampInLong){
            fvDimensionLayers.addFirst(layer);
        }else{
            index = appendCenter(layer);
            centerCount += 1;
            if (first < 100 && allCount > 1000 && (centerCount / (double)allCount) > 0.1){
                first++;
                System.err.println("***************************\nappend center too much in FvDimensionList!\n***************************");
            }
        }
        allCount += 1;
        cutList();
        return analyze(index,fvDimensionLayers,layer);
    }

    private void cutList(){
        if (cap > 0 && fvDimensionLayers.size() > cap){
            fvDimensionLayers.removeLast();
        }
    }

    private int appendCenter(FvDimensionLayer layer){
        int i = 0;
        FvDimensionLayer next;
        while (i < fvDimensionLayers.size()){
            next = fvDimensionLayers.get(i);
            if (next.timeStampInLong < layer.timeStampInLong){
                fvDimensionLayers.add(i,layer);
                break;
            }
            i++;
        }
        return i;
    }

    public FvDimensionLayer getLayer(int index){
        return fvDimensionLayers.get(index);
    }

    public void setCap(int cap){
        this.cap = cap;
    }

    /**
     * @param layer 新插入的五元组
     * @param index 新插入的五元组所在的索引
     * @param fvDimensionLayers 新增加的五元组
     * @return 是否检测到攻击，true表示检测到，false表示没有检测到
     */
    protected boolean analyze(int index,LinkedList<FvDimensionLayer> fvDimensionLayers,FvDimensionLayer layer){
        return false;
    }

    public int getSize(){
        return fvDimensionLayers.size();
    }

    @Override
    public String toString() {
        return "FvDimensionList{" +
                "cap=" + cap +
                ", fvDimensionLayers=" + fvDimensionLayers +
                '}';
    }
}
