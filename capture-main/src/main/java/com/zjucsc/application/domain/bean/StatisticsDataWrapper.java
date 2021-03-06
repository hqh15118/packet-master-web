package com.zjucsc.application.domain.bean;

import com.zjucsc.application.domain.non_hessian.Top5Statistic;
import lombok.Data;

import java.util.Map;

@Data
public class StatisticsDataWrapper {

    private long packetCount;
    private long attackCount;
    //private long exceptionCount;
    private int currentPacketCount;
    private Map<String, Integer> packetIn;
    private Map<String, Integer> packetOut;
    private Map<String, Integer> attackByDevice;
    //private Map<String, Integer> exceptionByDevice;
    private Map<String,Integer> collectorDelays;
    private Top5Statistic top5Statistic;
    private int deviceCount;
    private int attackedDeviceCount;

    public static class Builder{

        private StatisticsDataWrapper statisticsDataWrapper;

        public Builder(){
            statisticsDataWrapper = new StatisticsDataWrapper();
        }
        public Builder setNumber(long number) {
            statisticsDataWrapper.packetCount = number;
            return this;
        }

        public Builder setAttackCount(long attackCount) {
            statisticsDataWrapper.attackCount = attackCount;
            return this;
        }

//        public Builder setExceptionCount(long exceptionCount) {
//            statisticsDataWrapper.exceptionCount = exceptionCount;
//            return this;
//        }

        public Builder setNumberByDeviceIn(Map<String, Integer> numberByDeviceIn) {
            statisticsDataWrapper.packetIn = numberByDeviceIn;
            return this;
        }

        public Builder setNumberByDeviceOut(Map<String, Integer> numberByDeviceOut) {
            statisticsDataWrapper.packetOut = numberByDeviceOut;
            return this;
        }

        public Builder setAttackByDevice(Map<String, Integer> attackByDevice) {
            statisticsDataWrapper.attackByDevice = attackByDevice;
            return this;
        }

//        public Builder setExceptionByDevice(Map<String, Integer> exceptionByDevice) {
//            statisticsDataWrapper.exceptionByDevice = exceptionByDevice;
//            return this;
//        }

        public Builder setCollectorDelay(Map<String,Integer> collectorDelays){
            statisticsDataWrapper.collectorDelays = collectorDelays;
            return this;
        }

        public Builder setCurrentPacketCount(int count){
            statisticsDataWrapper.currentPacketCount = count;
            return this;
        }

        public Builder setTop5Statistic(Top5Statistic top5Statistic){
            statisticsDataWrapper.top5Statistic = top5Statistic;
            return this;
        }

        public Builder setDeviceCount(int deviceCount){
            statisticsDataWrapper.deviceCount = deviceCount;
            return this;
        }

        public Builder setAttackedDeviceCount(int attackedDeviceCount){
            statisticsDataWrapper.attackedDeviceCount = attackedDeviceCount;
            return this;
        }

        public StatisticsDataWrapper build(){
            return statisticsDataWrapper;
        }
    }
}
