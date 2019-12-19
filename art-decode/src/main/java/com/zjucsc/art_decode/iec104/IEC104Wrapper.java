package com.zjucsc.art_decode.iec104;

public class IEC104Wrapper {
    private String idOrIOA;
    private float value;
    public IEC104Wrapper(String idOrIOA, float value) {
        this.idOrIOA = idOrIOA;
        this.value = value;
    }

    public String getIdOrIOA() {
        return idOrIOA;
    }

    public void setIdOrIOA(String idOrIOA) {
        this.idOrIOA = idOrIOA;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IEC104Wrapper{" +
                "idOrIOA='" + idOrIOA + '\'' +
                ", value=" + value +
                '}';
    }
}
