package com.zjucsc.art_decode.iec104;

public class IEC104Wrapper {
    private String idOrIOA;
    private float value;
    private String ip;
    private String ioa;
    public IEC104Wrapper(String idOrIOA, float value,
                         String ip,String ioa) {
        this.idOrIOA = idOrIOA;
        this.value = value;
        this.ip = ip;
        this.ioa = ioa;
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
                ", ip='" + ip + '\'' +
                ", ioa='" + ioa + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIoa() {
        return ioa;
    }

    public void setIoa(String ioa) {
        this.ioa = ioa;
    }
}
