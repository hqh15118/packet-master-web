package com.zjucsc.attack.opname;

import com.zjucsc.attack.bean.BaseOpName;

public class IEC104OpNameByTshark extends BaseOpName {
    private String ipAddress;
    private String ioaAddress;
    //description，首页上的描述即可
    private String deviceId;
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIoaAddress() {
        return ioaAddress;
    }

    public void setIoaAddress(String ioaAddress) {
        this.ioaAddress = ioaAddress;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
