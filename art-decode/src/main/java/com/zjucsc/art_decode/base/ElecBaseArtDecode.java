package com.zjucsc.art_decode.base;

import com.zjucsc.art_decode.ArtDecodeUtil;

public abstract class ElecBaseArtDecode<T extends BaseConfig> extends BaseArtDecode<T> {

    public void elecStatusChangeCallback(String eventType,Object obj){
        if (ArtDecodeUtil.elecStatusChangeCallback!=null) {
            ArtDecodeUtil.elecStatusChangeCallback.callback(eventType, obj);
        }else{
            System.err.println("elec status callback not register!");
        }
    }


}
