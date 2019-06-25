package com.zjucsc.application.domain.bean;

import java.io.Serializable;

public class ArtHistoryData  extends BaseResponse implements Serializable {
    private String timeStamp;
    private float artValue;
    private String artName;
    private int gplotId;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getArtValue() {
        return artValue;
    }

    public void setArtValue(float artValue) {
        this.artValue = artValue;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public int getGplotId() {
        return gplotId;
    }

    public void setGplotId(int gplotId) {
        this.gplotId = gplotId;
    }

    @Override
    public String toString() {
        return "ArtHistoryData{" +
                "timeStamp='" + timeStamp + '\'' +
                ", artValue=" + artValue +
                ", artName='" + artName + '\'' +
                ", gplotId=" + gplotId +
                '}';
    }
}
