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
}
