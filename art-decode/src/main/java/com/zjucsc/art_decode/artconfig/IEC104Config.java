package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

public class IEC104Config extends BaseConfig {
    private int setIOAAddress;
    private int mvIOAAddress;
    private float fullscale;

    public void setSetIOAAddress(int setIOAAddress) { this.setIOAAddress = setIOAAddress; }

    public void setMVIOAAddress(int mvIOAAddress) { this.mvIOAAddress = mvIOAAddress; }

    public void setFullScale(float fullscale) {this.fullscale = fullscale;}

    public int getSetIOAAddress() { return setIOAAddress; }

    public int getMVIOAAddress() { return mvIOAAddress; }

    public float getFullScale() { return fullscale;}

}
