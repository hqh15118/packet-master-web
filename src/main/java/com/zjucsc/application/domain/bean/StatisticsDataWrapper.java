package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class StatisticsDataWrapper {

    private long packetCount;
    private long attackCount;
    private long exceptionCount;
    private Map<String, AtomicLong> packetIn;
    private Map<String, AtomicLong> packetOut;
    private Map<String, AtomicLong> attackByDevice;
    private Map<String, AtomicLong> exceptionByDevice;
    private Map<String,Integer> collectorDelays;

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

        public Builder setExceptionCount(long exceptionCount) {
            statisticsDataWrapper.exceptionCount = exceptionCount;
            return this;
        }

        public Builder setNumberByDeviceIn(Map<String, AtomicLong> numberByDeviceIn) {
            statisticsDataWrapper.packetIn = numberByDeviceIn;
            return this;
        }

        public Builder setNumberByDeviceOut(Map<String, AtomicLong> numberByDeviceOut) {
            statisticsDataWrapper.packetOut = numberByDeviceOut;
            return this;
        }

        public Builder setAttackByDevice(Map<String, AtomicLong> attackByDevice) {
            statisticsDataWrapper.attackByDevice = attackByDevice;
            return this;
        }

        public Builder setExceptionByDevice(Map<String, AtomicLong> exceptionByDevice) {
            statisticsDataWrapper.exceptionByDevice = exceptionByDevice;
            return this;
        }

        public Builder setCollectorDelay(Map<String,Integer> collectorDelays){
            statisticsDataWrapper.collectorDelays = collectorDelays;
            return this;
        }

        public StatisticsDataWrapper build(){
            return statisticsDataWrapper;
        }
    }
}
