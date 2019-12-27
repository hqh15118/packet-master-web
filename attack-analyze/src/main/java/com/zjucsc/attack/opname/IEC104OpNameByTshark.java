package com.zjucsc.attack.opname;

import com.zjucsc.attack.bean.BaseOpName;

public class IEC104OpNameByTshark extends BaseOpName {
    private String ipAddress;
    private String ioaAddress;
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
}
