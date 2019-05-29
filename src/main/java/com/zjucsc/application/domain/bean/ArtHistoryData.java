package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;

public class ArtHistoryData implements Serializable {
    private String timeStamp;
    private float artValue;
    private String artName;
    private String timeType;

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

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }


    @Override
    public String toString() {
        return "ArtHistoryData{" +
                "timeStamp='" + timeStamp + '\'' +
                ", artValue=" + artValue +
                ", artName='" + artName + '\'' +
                ", timeType='" + timeType + '\'' +
                '}';
    }
}
