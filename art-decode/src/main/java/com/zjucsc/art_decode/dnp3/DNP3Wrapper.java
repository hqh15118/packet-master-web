package com.zjucsc.art_decode.dnp3;

public class DNP3Wrapper {

    private String id;
    private float value;

    public DNP3Wrapper(String id, float value) {
        this.id = id;
        this.value = value;
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
                '}';
    }
}
