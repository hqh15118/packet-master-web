import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class CommonTest {

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
}
