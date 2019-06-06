import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class TsharkCommonTest {

    @Test
    public void FvDimensionCompareTest(){
        PriorityQueue<FvDimensionLayer> fvDimensionLayerPriorityQueue = new PriorityQueue<>(new Comparator<FvDimensionLayer>() {
            @Override
            public int compare(FvDimensionLayer o1, FvDimensionLayer o2) {
                return (int) (o1.timeStampInLong - o2.timeStampInLong);
            }
        });
        Random random = new Random();
        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
            fvDimensionLayer.timeStampInLong = random.nextInt(100000);
            fvDimensionLayerPriorityQueue.offer(fvDimensionLayer);
            if (fvDimensionLayerPriorityQueue.size() >= 10){
                fvDimensionLayerPriorityQueue.remove();
            }
        }
        FvDimensionLayer layer = null;
        System.out.println(System.currentTimeMillis() - timeStart);
        while ((layer = fvDimensionLayerPriorityQueue.poll())!=null){
            System.out.println(layer.timeStampInLong);
        }
    }

    @Test
    public void FvDimensionListCompareTest(){
        FvDimensionList fvDimensionList = new FvDimensionList(10);
        Random random = new Random();
        long timeStart = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
            fvDimensionLayer.timeStampInLong = random.nextInt(100000);
            fvDimensionList.append(fvDimensionLayer);
        }
        FvDimensionLayer layer = null;
        System.out.println("time" + (System.currentTimeMillis() - timeStart));
    }

    @Test
    public void tsharkSpeedTest() throws IOException {
        String command = "tshark -T ek -e frame.protocols -e eth.dst -e ext_raw_data -c 10000 -r E:\\IdeaProjects\\packet-master-web\\z-other\\others\\pcap\\104_dnp_packets.pcapng";
        Process process = Runtime.getRuntime().exec(command);
        InputStream is = process.getInputStream();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
        long startTime = System.currentTimeMillis();
        for (;;){
            String line = bfr.readLine();
            if (line==null){
                break;
            }
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
