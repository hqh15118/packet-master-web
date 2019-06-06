> Common

GPLOT_ID        

hasStartedHost

+ CONFIGURATION_MAP   组态配置MAP;
HashMap<String , HashMap<Integer,String>> key是协议，value的key是协议的功能码，value是协议功能码对应的中文含义
1、ServiceLoader中加载


 - CommonCacheUtil OK
 - InitConfigurationService OK
 - 

+ PROTOCOL_STR_TO_INT ： 协议ID和协议字符串之间的转换 --> 对应数据库中的protocol_id表。
BiMap<Integer,String> PROTOCOL_STR_TO_INT = HashBiMap.create();
1、从PACKET_PROTOCOL中加载，STR -> ID，InitConfigurationService初始化时候初始化该MAP；
2、添加新的协议，返回该协议的ID，ConfigurationSettingController
3、删除协议，ConfigurationSettingController
 - CommonCacheUtil OK
 - InitConfigurationService OK
 - ConfigurationSettingController.addNewProtocol OK
 - ConfigurationSettingController.deleteProtocol OK

+ DEVICE_IP_TO_NAME

+ tshark性能测试
```java
public class Test{
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
```
``结论：tshark字符串输出性能有瓶颈，2000条/秒左右【跟输出的字符串长度有关系】，但是tshark会将未处理的任务缓存起来，
-M 10000由于清空已经处理的任务（即已经输出的字符串）``
