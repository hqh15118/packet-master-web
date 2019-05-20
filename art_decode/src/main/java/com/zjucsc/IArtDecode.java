package com.zjucsc;

import java.util.List;
import java.util.Map;

public interface IArtDecode {
    List<Map<Integer,Double>> decode(byte[] payload);
}
