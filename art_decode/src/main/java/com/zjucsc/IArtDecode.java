package com.zjucsc;

import java.util.Map;

public interface IArtDecode {
    Map<String,Double> decode(byte[] payload);
    String protocol();
}
