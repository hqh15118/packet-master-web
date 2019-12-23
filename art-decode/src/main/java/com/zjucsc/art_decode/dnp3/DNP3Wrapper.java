package com.zjucsc.art_decode.dnp3;

public class DNP3Wrapper {

    private String id;
    private float value;
    private String type;
    private String pointIndex;
    private String ip;

    public DNP3Wrapper(String id, float value,
                       String type,String pointIndex,
                       String ip) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.pointIndex = pointIndex;
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DNP3Wrapper{" +
                "id='" + id + '\'' +
                ", value=" + value +
                ", type='" + type + '\'' +
                ", pointIndex='" + pointIndex + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
