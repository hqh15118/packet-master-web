package deploy.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DBUtilTest {

    @Test
    public void dbInit(){
        Map map = new HashMap();
        map.put("name","hongqianhui");
        map.put("grade","1");
        String jsonData = JSON.toJSONString(map, SerializerFeature.PrettyFormat);
        System.out.println(jsonData);
        Map map1 = JSON.parseObject(jsonData,HashMap.class);
        System.out.println(map1);
    }
}