package com.zjucsc.application.services;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.domain.bean.FvDimensionWrapper;
import com.zjucsc.application.system.service.common_iservice.IKafkaService;
import com.zjucsc.kafka.KafkaProducerCreator;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class KafkaServiceTest {

    @Autowired private IKafkaService iKafkaService;


    @Test
    public void sendFVDimensionTest(){
        FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
        fvDimensionLayer.frame_protocols = new String[]{"s7comm"};
        fvDimensionLayer.ip_dst = new String[]{"192.168.0.121"};
        fvDimensionLayer.dst_port = new String[]{"9090"};
        fvDimensionLayer.src_port = new String[]{"8989"};
        fvDimensionLayer.eth_dst = new String[]{"11:22:22:33:44:55"};
        fvDimensionLayer.eth_src = new String[]{"11:22:22:33:44:66"};
        fvDimensionLayer.ip_src = new String[]{"192.168.0.122"};
        fvDimensionLayer.frame_cap_len = new String[]{"10"};

        String trailer = "00:03:0d:0d:fc:6b:07:e4:ae:78:63:b0:fc:6b:07:e4:ae:78:64:20";
        fvDimensionLayer.eth_trailer = new String[]{trailer};
        String fcs = "0x00000067";
        fvDimensionLayer.eth_fcs = new String[]{fcs};
        FvDimensionWrapper fvDimensionWrapper = FvDimensionWrapper.builder()
                .collectorId(1)
                .delay(100)
                .layer(fvDimensionLayer).build();
        iKafkaService.sendAllPacket(fvDimensionWrapper);
    }

    @Test
    public void sendFVDimensionTest2() throws ExecutionException, InterruptedException {
        FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
        fvDimensionLayer.frame_protocols = new String[]{"s7comm"};
        fvDimensionLayer.ip_dst = new String[]{"192.168.0.121"};
        fvDimensionLayer.dst_port = new String[]{"9090"};
        fvDimensionLayer.src_port = new String[]{"8989"};
        fvDimensionLayer.eth_dst = new String[]{"11:22:22:33:44:55"};
        fvDimensionLayer.eth_src = new String[]{"11:22:22:33:44:66"};
        fvDimensionLayer.ip_src = new String[]{"192.168.0.122"};
        fvDimensionLayer.frame_cap_len = new String[]{"10"};

        String trailer = "00:03:0d:0d:fc:6b:07:e4:ae:78:63:b0:fc:6b:07:e4:ae:78:64:20";
        fvDimensionLayer.eth_trailer = new String[]{trailer};
        String fcs = "0x00000067";
        fvDimensionLayer.eth_fcs = new String[]{fcs};
        KafkaProducer<String,String> producer = KafkaProducerCreator.getProducer("demo",String.class,String.class);
        FvDimensionWrapper fvDimensionWrapper = FvDimensionWrapper.builder()
                .collectorId(1)
                .delay(100)
                .layer(fvDimensionLayer).build();
        long time1 = System.currentTimeMillis();
        String str = JSON.toJSONString(fvDimensionWrapper);
        for (int i = 0; i < 10000; i++) {
            producer.send(new ProducerRecord<>("fv_dimension", str));
        }
        System.out.println(System.currentTimeMillis() - time1);
    }

}