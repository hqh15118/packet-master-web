package com.zjucsc;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.domain.packet.UnknownPacket;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Map;

@Slf4j
public class Other {


    @Test
    public void tsharkTest() throws IOException {
        String command = "tshark -l -n  -i en0 -T ek -e frame.protocols -e eth.dst -e eth.src -e frame.cap_len -e ip.dst -e ip.src -e tcp.srcport -e tcp.dstport -e eth.trailer -e eth.fcs -e tcp.payload -Y \"not s7comm and not modbus and not 104apci\"";
        Process process = Runtime.getRuntime().exec(new String[]{"bash" , "-c" ,command});
        doStream(process.getInputStream() , process.getErrorStream());
    }

    private void doStream(InputStream inputStream,InputStream errorStream) throws IOException {
        BufferedReader bfreader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader bfreader_error = new BufferedReader(new InputStreamReader(errorStream));
        System.out.println(bfreader_error.readLine());
        for (;;){
            String s = bfreader.readLine();
            if (s == null){
                System.out.println("end read");
                break;
            }else{
                System.out.println(s);
            }
        }
    }

    /**
     * 10W 条750ms
     */
    @Test
    public void fastJSONTestOfFvDimensionInstanceToStr(){
        FvDimensionLayer fvDimensionLayer = new FvDimensionLayer();
        fvDimensionLayer.frame_protocols = new String[]{"s7comm"};
        fvDimensionLayer.ip_dst = new String[]{"192.168.0.121"};
        fvDimensionLayer.dst_port = new String[]{"9090"};
        fvDimensionLayer.src_port = new String[]{"8989"};
        fvDimensionLayer.eth_dst = new String[]{"11:22:22:33:44:55"};
        fvDimensionLayer.eth_src = new String[]{"11:22:22:33:44:66"};
        fvDimensionLayer.ip_src = new String[]{"192.168.0.122"};
        fvDimensionLayer.frame_cap_len = new String[]{"10"};


        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            JSON.toJSONString(fvDimensionLayer);
        }
        System.out.println(System.currentTimeMillis() - time1);
    }


    @Test
    public void osEnvironmentTest(){
        System.out.println("Read Specific Enviornment Variable");
        System.out.println("JAVA_HOME Value:- " + System.getenv("JAVA_HOME"));

        System.out.println("\nRead All Variables:-\n");

        Map<String, String> map = System.getenv();
        for (Map.Entry <String, String> entry: map.entrySet()) {
            System.out.println("Variable Name:- " + entry.getKey() + " Value:- " + entry.getValue());
        }

        System.out.println("------------------------");

        String osPath = System.getenv("PATH");
        System.out.println(osPath);
    }


    @Test
    public void beanJSONGenerate() throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        //String path = "E:\\IdeaProjects\\packet-master-web\\target\\classes\\com\\zjucsc\\application\\domain\\bean";
        String path1 = "/Users/hongqianhui/JavaProjects/packet-master-web/target/classes/com/zjucsc/application/domain/bean";
        File file = new File(path1);
        File[] clazzFiles = file.listFiles();
        assert clazzFiles!=null;
        MyClassLoader myClassLoader = new MyClassLoader();
        for (File clazzFile : clazzFiles) {
            String filePath = clazzFile.getAbsolutePath();
            Class<?> clazz = Class.forName(filePath,true,myClassLoader);
            Object obj = clazz.newInstance();
        }
    }


    private static class MyClassLoader extends ClassLoader{

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            File file = new File(name);
            assert file.exists();
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                int size = fis.available();
                byte[] bytes = new byte[size];
                fis.read(bytes,0,size);
                String clazzName = name.replace(".class","").replace("\\",".");
                System.out.println(name);
                return defineClass(clazzName,bytes,0,bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Test
    public void tsharkDecodeTest() throws IOException {
        //-e custom_ext_raw_data
        String command = "tshark -n -l -T ek -e frame.protocols -e eth.dst -e custom_ext_raw_data -i \\Device\\NPF_{1A0E9386-C7CE-4D46-A22B-B4FE974A324E} -M 50000";
        //String command = "tshark -n -l -T ek -e frame.protocols -e eth.dst  -c 2000 -r E:\\IdeaProjects\\packet-master-web\\z-other\\others\\pcap\\104_dnp_packets.pcapng";
        int i = 0;
        Process process = Runtime.getRuntime().exec(command);
        InputStream is = process.getInputStream();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
        long startTime = System.currentTimeMillis();
        for (;;){
            String line = bfr.readLine();
            if (line==null){
                break;
            }else{
                if (line.length() > 90) {
                    i++;
                    //UnknownPacket layer = JSON.parseObject(line,UnknownPacket.class);
                    //byte[] bytes = ByteUtil.hexStringToByteArray(layer.layers.custom_ext_raw_data[0],0);
                    //System.out.println(PacketDecodeUtil.decodeTimeStamp(bytes,20));
                    //log.info("number : {} , info : {} " , i);
                }
            }
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
