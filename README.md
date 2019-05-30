### 如何在程序中添加协议解析模块

> tshark

tshark是wireshark的命令行程序，可以用于报文获取 + 报文解析。所有命令查看：[TSHARK命令](https://www.wireshark.org/docs/man-pages/tshark.html)

常用的命令行参数有：
+ -l 设置tshark实时输出标准输出，不缓存
+ -T json|ek|... 设置输出格式：只需要用到json + ek，前者用于查看协议字段，后者用于标准输出
+ -r <file_name> 设置读取的.pcap文件
+ -c <packet_number> 设置读取的报文数量
+ -i <interface_name> 设置捕获接口名`tshark -D`查看本机所有网卡接口


> 添加Packet实体

`com.zjucsc.application.tshark.domain.packet`包下

_**以S7Comm协议报文为例**_

#### 确定协议字段

````
终端敲下面命令：
>> tshark -T json -c 1 -r pcapfile.pcap
tshark : wireshark包下的tshark程序
-T json tshark标准输出格式为JSON
-c 1 设置读取的报文数量，pcap文件中可能有很多其他协议的报文，得先用wireshark知道需要的报文在第几条
-r pcapfile.pcap 指定要读取的离线pcap文件
````
正确的输出格式：
````json
{
    "_index": "packets-2019-05-10",
    "_type": "pcap_file",
    "_score": null,
    "_source": {
      "layers": {
        "frame": {
          "frame.encap_type": "1",
          "frame.time": "May 10, 2019 17:48:53.743097000 CST",
          "frame.offset_shift": "0.000000000",
          "frame.time_epoch": "1557481733.743097000",
          "frame.time_delta": "0.000278000",
          "frame.time_delta_displayed": "0.000278000",
          "frame.time_relative": "0.000278000",
          "frame.number": "2",
          "frame.len": "121",
          "frame.cap_len": "121",
          "frame.marked": "0",
          "frame.ignored": "0",
          "frame.protocols": "eth:ethertype:ip:tcp:tpkt:cotp:s7comm"
        },
        "eth": {
          "eth.dst": "00:0c:29:75:b2:38",
          "eth.dst_tree": {
            "eth.dst_resolved": "Vmware_75:b2:38",
            "eth.addr": "00:0c:29:75:b2:38",
            "eth.addr_resolved": "Vmware_75:b2:38",
            "eth.lg": "0",
            "eth.ig": "0"
          },
          "eth.src": "00:0c:29:49:7e:9f",
          "eth.src_tree": {
            "eth.src_resolved": "Vmware_49:7e:9f",
            "eth.addr": "00:0c:29:49:7e:9f",
            "eth.addr_resolved": "Vmware_49:7e:9f",
            "eth.lg": "0",
            "eth.ig": "0"
          },
          "eth.type": "0x00000800",
          "eth.trailer": "00:02:0d:04:fc:6a:a8:de:fb:9e:59:80:fc:6a:a8:de:fb:9e:5a:00",
          "eth.fcs": "0x00000081",
          "eth.fcs.status": "2"
        },
        "ip": {
          "ip.version": "4",
          "ip.hdr_len": "20",
          "ip.dsfield": "0x00000000",
          "ip.dsfield_tree": {
            "ip.dsfield.dscp": "0",
            "ip.dsfield.ecn": "0"
          },
          "ip.len": "83",
          "ip.id": "0x00000a21",
          "ip.flags": "0x00004000",
          "ip.flags_tree": {
            "ip.flags.rb": "0",
            "ip.flags.df": "1",
            "ip.flags.mf": "0",
            "ip.frag_offset": "0"
          },
          "ip.ttl": "128",
          "ip.proto": "6",
          "ip.checksum": "0x00007289",
          "ip.checksum.status": "2",
          "ip.src": "192.168.254.34",
          "ip.addr": "192.168.254.34",
          "ip.src_host": "192.168.254.34",
          "ip.host": "192.168.254.34",
          "ip.dst": "192.168.254.134",
          "ip.addr": "192.168.254.134",
          "ip.dst_host": "192.168.254.134",
          "ip.host": "192.168.254.134"
        },
        "tcp": {
          "tcp.srcport": "102",
          "tcp.dstport": "1073",
          "tcp.port": "102",
          "tcp.port": "1073",
          "tcp.stream": "1",
          "tcp.len": "43",
          "tcp.seq": "1",
          "tcp.nxtseq": "44",
          "tcp.ack": "1",
          "tcp.hdr_len": "20",
          "tcp.flags": "0x00000018",
          "tcp.flags_tree": {
            "tcp.flags.res": "0",
            "tcp.flags.ns": "0",
            "tcp.flags.cwr": "0",
            "tcp.flags.ecn": "0",
            "tcp.flags.urg": "0",
            "tcp.flags.ack": "1",
            "tcp.flags.push": "1",
            "tcp.flags.reset": "0",
            "tcp.flags.syn": "0",
            "tcp.flags.fin": "0",
            "tcp.flags.str": "·······AP···"
          },
          "tcp.window_size_value": "63682",
          "tcp.window_size": "63682",
          "tcp.window_size_scalefactor": "-1",
          "tcp.checksum": "0x00007b3f",
          "tcp.checksum.status": "2",
          "tcp.urgent_pointer": "0",
          "tcp.analysis": {
            "tcp.analysis.bytes_in_flight": "43",
            "tcp.analysis.push_bytes_sent": "43"
          },
          "Timestamps": {
            "tcp.time_relative": "0.000000000",
            "tcp.time_delta": "0.000000000"
          },
          "tcp.payload": "03:00:00:2b:02:f0:80:32:03:00:00:cc:c1:00:02:00:16:00:00:04:01:ff:04:00:90:00:00:00:00:02:00:37:00:00:00:00:0c:00:00:00:00:2c:41"
        },
        "tpkt": {
          "tpkt.version": "3",
          "tpkt.reserved": "0",
          "tpkt.length": "43"
        },
        "cotp": {
          "cotp.li": "2",
          "cotp.type": "0x0000000f",
          "cotp.destref": "0x00010000",
          "cotp.tpdu-number": "0x00000000",
          "cotp.eot": "1"
        },
        "s7comm": {
          "s7comm.header": {
            "s7comm.header.protid": "0x00000032",
            "s7comm.header.rosctr": "3",
            "s7comm.header.redid": "0x00000000",
            "s7comm.header.pduref": "52417",
            "s7comm.header.parlg": "2",
            "s7comm.header.datlg": "22",
            "s7comm.header.errcls": "0x00000000",
            "s7comm.header.errcod": "0x00000000"
          },
          "s7comm.param": {
            "s7comm.param.func": "0x00000004",
            "s7comm.param.itemcount": "1"
          },
          "s7comm.data": {
            "s7comm.data.item": {
              "s7comm.data.returncode": "0x000000ff",
              "s7comm.data.transportsize": "0x00000004",
              "s7comm.data.length": "18",
              "s7comm.resp.data": "00:00:00:00:02:00:37:00:00:00:00:0c:00:00:00:00:2c:41"
            }
          }
        }
      }
    }
  }
````
五元组部分不需要关注，只需要查看协议的特殊字段即可，S7的就是：
```json
"s7comm": {
          "s7comm.header": {
            "s7comm.header.protid": "0x00000032",
            "s7comm.header.rosctr": "3",
            "s7comm.header.redid": "0x00000000",
            "s7comm.header.pduref": "52417",
            "s7comm.header.parlg": "2",
            "s7comm.header.datlg": "22",
            "s7comm.header.errcls": "0x00000000",
            "s7comm.header.errcod": "0x00000000"
          },
          "s7comm.param": {
            "s7comm.param.func": "0x00000004",
            "s7comm.param.itemcount": "1"
          },
          "s7comm.data": {
            "s7comm.data.item": {
              "s7comm.data.returncode": "0x000000ff",
              "s7comm.data.transportsize": "0x00000004",
              "s7comm.data.length": "18",
              "s7comm.resp.data": "00:00:00:00:02:00:37:00:00:00:00:0c:00:00:00:00:2c:41"
            }
          }
        }
```
确定需要哪些字段，S7的只需要知道`"s7comm.param.func": "0x00000004"`

````
终端敲下面命令：
>> tshark -T ek -e s7comm.param.func -c 1 -r pcapfile.pcap
tshark : wireshark包下的tshark程序
-T ek tshark标准输出格式为EK[这种格式最适合JSON解析]
-c 1 设置读取的报文数量，pcap文件中可能有很多其他协议的报文，得先用wireshark知道需要的报文在第几条
-r pcapfile.pcap 指定要读取的离线pcap文件
-e 设置输出的字段，这里填入的是上面确定好的S7的功能码字段
````
正确的输出格式如下：
```
{"index" : {"_index": "packets-2019-05-17", "_type": "pcap_file"}}
{"timestamp" : "1557481733743", "layers" : {"s7comm_param_func": ["0x00000004"]}}
```
`{"timestamp" : "1557481733743", "layers" : {"s7comm_param_func": ["0x00000004"]}}`就是解析时候需要的JSON字符串

s7comm_param_func就是需要的字段格式。

#### 添加报文实体

类似于`com.zjucsc.application.tshark.domain.packet`包下的格式：
```java
package com.zjucsc.application.tshark.domain.packet;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

public class S7CommPacket{

    public static final String JOB = "1";
    public static final String ACK_DATA = "3";

    @JSONField(name = "layers")
    public LayersBean layersX;

    /**
     * timestamp : 1557129889033
     * layers : {"frame_protocols":["eth:ethertype:ip:tcp:tpkt:cotp:s7comm"],"eth_dst":["00:0c:29:49:7e:9f"],"frame_cap_len":["109"],"eth_src":["00:0c:29:75:b2:38"],"ip_src":["192.168.254.134"],"ip_dst":["192.168.254.34"],"tcp_srcport":["1073"],"tcp_dstport":["102"],"s7comm_param_func":["0x00000004"],"tcp_payload":["0300001f02f08032010000ccc1000e00000401120a10020011000184000020"],"s7comm_header_rosctr":["1"]}
     */

    public static class LayersBean extends FvDimensionLayer {
        public String[] s7comm_param_func={""};
        public String[] s7comm_header_rosctr={""};

        @Override
        public String toString() {
            return "LayersBean{" +
                    "s7comm_param_func=" + Arrays.toString(s7comm_param_func) +
                    ", s7comm_header_rosctr=" + Arrays.toString(s7comm_header_rosctr) +
                    ", frame_protocols=" + Arrays.toString(frame_protocols) +
                    ", eth_dst=" + Arrays.toString(eth_dst) +
                    ", frame_cap_len=" + Arrays.toString(frame_cap_len) +
                    ", eth_src=" + Arrays.toString(eth_src) +
                    ", ip_src=" + Arrays.toString(ip_src) +
                    ", ip_dst=" + Arrays.toString(ip_dst) +
                    ", tcp_srcport=" + Arrays.toString(src_port) +
                    ", tcp_dstport=" + Arrays.toString(dst_port) +
                    ", eth_trailer=" + Arrays.toString(eth_trailer) +
                    ", eth_fcs=" + Arrays.toString(eth_fcs) +
                    ", timeStamp='" + timeStamp + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "S7CommPacket{" +
                "layersX=" + layersX +
                '}';
    }
}
```

格式基本和示例代码中的S7CommPacket差不多。
只需要重新写一下LayersBeans下的字段名字，修改为具体协议需要的字段名。

> 添加报文预处理器

com\zjucsc\application\tshark\pre_processor