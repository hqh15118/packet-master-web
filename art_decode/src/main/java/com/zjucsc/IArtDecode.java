package com.zjucsc;

import java.util.Map;

public interface IArtDecode {
    Map<String,Double> decode(Map<String,Double> globalMap,byte[] payload,Object...obj);
    String protocol();

}
