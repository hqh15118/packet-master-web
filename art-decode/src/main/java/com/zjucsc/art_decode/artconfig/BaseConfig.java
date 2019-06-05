package com.zjucsc.art_decode.artconfig;

import java.io.Serializable;

public class BaseConfig implements Serializable {
    public String protocol;
    public String artName;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }
}
