package com.zjucsc.capture_main_distribute.bean;

import com.zjucsc.capture_main_distribute.DangerLevel;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.Data;

@Data
public class BadPacket {
    /**
     * badType -- 具体的协议，还是五元组
     */
    private String badType;
    private FvDimensionLayer layer;
    private int fun_code;
    private String operation;
    private String artifactParam;
    private int badArtifactValue;
    private String comment;
    private DangerLevel dangerLevel;
    private String deviceNumber;



    public String getComment() {
        return comment;
    }

    public static class Builder{

        private BadPacket badPacket;

        public Builder(String badType) {
            this.badPacket = new BadPacket();
            this.badPacket.badType = badType;
        }

        public Builder set_five_Dimension(FvDimensionLayer layer) {
            badPacket.layer = layer;
            return this;
        }

        public Builder setFun_code(int fun_code) {
            badPacket.fun_code = fun_code;
            return this;
        }

        public Builder setOperation(String operation) {
            badPacket.operation = operation;
            return this;
        }

        public Builder setArtifactParam(String artifactParam) {
            badPacket.artifactParam = artifactParam;
            return this;
        }

        public Builder setBadArtifactValue(int badArtifactValue) {
            badPacket.badArtifactValue = badArtifactValue;
            return this;
        }

        public Builder setComment(String comment) {
            badPacket.comment = comment;
            return this;
        }

        public Builder setDangerLevel(DangerLevel dangerLevel) {
            badPacket.dangerLevel = dangerLevel;
            return this;
        }

        public BadPacket build(){
            return badPacket;
        }
    }

}
