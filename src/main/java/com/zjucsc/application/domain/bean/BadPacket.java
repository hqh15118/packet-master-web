package com.zjucsc.application.domain.bean;

import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacket;
import lombok.Data;
import lombok.ToString;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 23:01
 */

@Data
public class BadPacket {
    /**
     * badType -- 具体的协议，还是五元组？
     */
    private String badType;
    private FiveDimensionPacket fiveDimensionPacket;
    private int fun_code;
    private String operation;
    private String artifactParam;
    private int badArtifactValue;
    private String comment;
    private DangerLevel dangerLevel;
    @ToString.Exclude
    private StringBuilder sb;
    private int deviceId;


    public void addComment(String comment) {
        if (sb == null){
            sb = new StringBuilder();
        }
        sb.append(comment);
    }

    public String getComment() {
        if (sb == null){
            return "";
        }
        return sb.toString();
    }

    public static class Builder{

        private BadPacket badPacket;

        public Builder(String badType) {
            this.badPacket = new BadPacket();
            this.badPacket.badType = badType;
        }

        public Builder set_five_Dimension(FiveDimensionPacket packet) {
            badPacket.fiveDimensionPacket = packet;
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
