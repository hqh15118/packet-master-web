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



