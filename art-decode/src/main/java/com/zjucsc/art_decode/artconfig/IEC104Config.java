package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

public class IEC104Config extends BaseConfig {
    private int setIOAAddress;
    private int mvIOAAddress;

    public void setSetIOAAddress(int setIOAAddress) { this.setIOAAddress = setIOAAddress; }

    public void setMVIOAAddress(int mvIOAAddress) { this.mvIOAAddress = mvIOAAddress; }

    public int getSetIOAAddress() { return setIOAAddress; }

    public int getMVIOAAddress() { return mvIOAAddress; }

}
