package com.zjucsc.application.system.art;

import java.util.Map;

public interface ICommonArtDecode {
    Map<String,Float> decode(Map<String,Float> globalMap,byte[] payload,String protocol);
}
