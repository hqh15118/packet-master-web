import com.zjucsc.tshark.util.TsharkUtil;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class PacketsTest {


    @Test
    public void s7Comm(){
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(String.valueOf(i),String.valueOf(i));
        }
        long time1 = System.currentTimeMillis();
        map.forEach((s, s2) -> {});
        System.out.println(System.currentTimeMillis() - time1);
    }

    @Test
    public void test(){
        String testString = "03:00:00:63:02:f0:80:32:07:00:00:00:00:00:0c:00:46:00:01:12:08:12:02:01:0a:00:00:00:00:ff:09:00:42:00:03:ff:04:00:80:40:46:55:55:3e:31:c7:1d:3d:b7:b4:26:3c:ed:09:7b:ff:04:00:40:00:00:00:00:00:00:00:00:ff:04:00:e0:3d:c7:1c:72:3e:c7:1c:72:3e:95:55:55:00:00:00:00:3e:80:97:b4:00:00:00:00:00:00:00:00";
        System.out.println(testString.contains(":"));
        byte[] payload = TsharkUtil.hexStringToByteArray(testString);
    }


}
