### 如何在程序中添加：
1. 报文协议；
2. 报文分析 --> 解析报文对应协议字段、判断报文是否异常；


> 添加报文协议

1. 在`com.zju.csc.application.config.PACLET_PROTOCOL`中添加协议类型。
比如`S7`，有`S7_ACK_DATA`和`S7_JOB`等[暂定两种]，那么就定义字段为：
```java
public interface PACKET_PROTOCOL{
    //...
    String S7_JOB = "s7comm_job";
    String S7_Ack_data = "s7comm_ack_data";
}
```
> 报文分析

1. 解析报文对应的协议字段
在`com.zjucsc.application.tshark.domain.packet.InitPacket`的`LayersBean`字段中，添加想要得到的协议字段。
*要注意根据tshark解析得到的字段类型来定义*.
比如S7，想要得到S7具体报文协议细节，那么就添加`s7comm_header_rosctr`字段，tshark在解析报文时，会得到该字段的
内容，原始JSON字符串中会有s7comm_header_rosctr[]类型，在`com.zjucsc.application.tshark.handler.BasePacketHandler`中，
会将原始JSON字符串解析为InitPacket实体，之后的分析就可以通过该实体获取到自定义的报文字段。[如果不是该类报文，该字段会返回空]
2. 判断报文是否异常
+ 添加自定义的`报文过滤器`，该过滤器是用于判断一条报文是否正常/异常的准则。比如`com.zjucsc.application.domain.filter.OperationPacketFilter`，
是根据报文的功能码判断该报文是否正常/异常的准则。核心是定义了两个map，用于查询该协议下白名单功能码和黑名单功能码。
```java
public class OperationPacketFilter{
    private HashMap<K,V> whiteMap = new HashMap<>();
    private HashMap<K,V> blackMap = new HashMap<>();
}
```
+ 添加自定义的`报文分析器`，继承`com.zjucsc.application.util.AbstractAnalyzer`，重写`Object analyze(Object...objs)`
方法，因为分析的具体内容是自己决定的，所以该方法传递的参数需要自己决定，并进行强制转换。判断一条报文是否正常，需要根据传入的
``报文过滤器``来判断。也就是说，**报文分析器定义分析逻辑，报文过滤器定义分析规则**。
+ 将自定义好的`报文分析器`添加到`com.zjucsc.application.config.Common`的`BAD_PACKET_FILTER_PRO_1`字段中。
KEY是自定义的协议，VALUE是自定义的`报文分析器<报文过滤器>`。
+ 报文分析，报文分析所有的逻辑都在``com.zjucsc.application.tshark.handler.PacketDecodeHandler``和``com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler``
中，前者的结果会传递给后者，前者主要对报文进行JSON解析，根据``BasePacketHandler``传递的protocol将其转为具体的报文实体如S7Packet，然后将报文实体传递给``BadPacketAnalyzeHandler``进行
恶意报文分析。``BadPacketAnalyzeHandler``又根据传入的协议，调用自定义的``报文分析器``进行报文的分析。

### EXAMPLE --> S7Comm
> 添加协议类型

tshark的protocolstack只能解析到eth:ip:tcp:...:s7comm，无法解析s7comm的具体类别，如JOB/ACK_DATA。
而s7comm是需要根据具体类别来识别功能码的，可以认为s7_job和s7_ack_data是两种不同的协议。
因此，需要在PACKET_PROTOCOL中定义
```java
public interface PACKET_PROTOCOL{
    //...
    String S7_JOB = "s7comm_job";
    String S7_Ack_data = "s7comm_ack_data";
}
```
两个字段。

> 修改InitPacket

在InitPacket中添加字段。``public String[] s7comm_header_rosctr = {""};``表示基础解析中需要解析出这个字段的内容，
来判断当protocolstack解析为s7comm时的具体类型。
修改``com.zjucsc.application.util.PacketDecodeUtil``的``discernPacket``方法，添加逻辑。当接收到某个protoclstack
的报文时，将其转换为我们自定义的报文类型，s7comm[protocolstack] --> S7_JOB或S7_ACK_DATA。

> 添加Decode逻辑

在``com.zjucsc.application.tshark.handler.PacketDecodeHandler``中添加解析逻辑，当检测到自定义协议的报文
时，将其转换为对应的报文实体。

> 添加恶意报文分析代码

在``com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler``中添加逻辑，传入需要的参数到自定义的报文分析器中。

> 添加报文过滤器

根据协议的过滤特点，添加自定义报文过滤器，s7comm的功能码比较适合作为HashMap的key，所以定义两个map，一个白名单一个黑名单。

> 添加报文分析器

重写analyze方法，添加报文分析逻辑。

> 添加service

让前端可以修改报文分析器对应的报文过滤器。

> 修改tshark command

将需要的字段添加到tshark command中，格式为 -e field...。
