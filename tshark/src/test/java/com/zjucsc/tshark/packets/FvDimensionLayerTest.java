package com.zjucsc.tshark.packets;

import org.junit.Test;

import java.util.PriorityQueue;
import java.util.Random;

public class FvDimensionLayerTest {
    @Test
    public void timeStampInLongTest(){
        PriorityQueue<FvDimensionLayer> fvDimensionLayers = new PriorityQueue<>(10000);
        for (int i = 0; i < 10000; i++) {
            FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
            fvDimensionLayer.setTimeStampInLong(i);
            fvDimensionLayer.setProtocol(String.valueOf(i));
            fvDimensionLayers.offer(fvDimensionLayer);
        }
//        Iterator<FvDimensionLayer> iterator = fvDimensionLayers.iterator();
//        while (iterator.hasNext()){
//            FvDimensionLayer layer = iterator.next();
//            System.out.println(layer.getProtocol() + " -- " + layer.getTimeStampInLong());
//        }
        for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
            System.out.println(fvDimensionLayer.getProtocol() + "--" + fvDimensionLayer.getTimeStampInLong());
        }
        System.out.println("----------------------------------");
        for (;;){
            FvDimensionLayer layer = fvDimensionLayers.poll();// min timeStampInLong
            if (layer == null){
                break;
            }else{
                if (layer.getTimeStampInLong() < 8000){
                    fvDimensionLayers.remove(layer);
                }else{
                    break;
                }
            }
        }
        for (FvDimensionLayer fvDimensionLayer : fvDimensionLayers) {
            System.out.println(fvDimensionLayer.getProtocol() + "--" + fvDimensionLayer.getTimeStampInLong());
        }
    }
}