package com.zjucsc.application.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExceptionForSelect extends BaseResponse implements Serializable {

    /**
     * list : [{"timeStamp":" 1","protocolName":"1 ","srcMac":"1 ","dstMac":"1 ","srcIp":"1 ","dstIp":"1 ","srcPort":" 1","dstPort":"1 ","funcode":"1 ","length":"1 "}]
     * count : 100
     */

    private int count;
    @JSONField(name = "list")
    private List<SavedExceptionPacket> savedPackets;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SavedExceptionPacket> getSavedPackets() {
        return savedPackets;
    }

    public void setSavedPackets(List<SavedExceptionPacket> savedPackets) {
        this.savedPackets = savedPackets;
    }

    public static class SavedExceptionPacket implements Serializable{
        /**
         * timeStamp :  1
         * protocolName : 1
         * srcMac : 1
         * dstMac : 1
         * srcIp : 1
         * dstIp : 1
         * srcPort :  1
         * dstPort : 1
         * funcode : 1
         * length : 1
         */

        private String timeStamp;
        private String protocolName;
        private String srcMac;
        private String dstMac;
        private String srcIp;
        private String dstIp;
        private String srcPort;
        private String dstPort;
        private int funcode;
        private String length;
        private int badType;
        private int danger;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getProtocolName() {
            return protocolName;
        }

        public void setProtocolName(String protocolName) {
            this.protocolName = protocolName;
        }

        public String getSrcMac() {
            return srcMac;
        }

        public void setSrcMac(String srcMac) {
            this.srcMac = srcMac;
        }

        public String getDstMac() {
            return dstMac;
        }

        public void setDstMac(String dstMac) {
            this.dstMac = dstMac;
        }

        public String getSrcIp() {
            return srcIp;
        }

        public void setSrcIp(String srcIp) {
            this.srcIp = srcIp;
        }

        public String getDstIp() {
            return dstIp;
        }

        public void setDstIp(String dstIp) {
            this.dstIp = dstIp;
        }

        public String getSrcPort() {
            return srcPort;
        }

        public void setSrcPort(String srcPort) {
            this.srcPort = srcPort;
        }

        public String getDstPort() {
            return dstPort;
        }

        public void setDstPort(String dstPort) {
            this.dstPort = dstPort;
        }

        public int getFuncode() {
            return funcode;
        }

        public void setFuncode(int funcode) {
            this.funcode = funcode;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public int getBadType() {
            return badType;
        }

        public void setBadType(int badType) {
            this.badType = badType;
        }

        public int getDanger() {
            return danger;
        }

        public void setDanger(int danger) {
            this.danger = danger;
        }
    }
}
