package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 21:23
 */

@Data
public class FvDimensionLayer implements Serializable,Comparable<FvDimensionLayer>{
    //用于前端显示的协议
    public String protocol;
    //更细致的协议
    public String[] frame_protocols = {"--"};
    public String[] eth_dst = {"--"};
    public String[] frame_cap_len = {"--"};
    public String[] eth_src = {"--"};
    public String[] eth_dst_re = {"--"};
    public String[] eth_src_re = {"--"};
    public String[] ip_src = {"--"};
    public String[] ip_dst = {"--"};
    public String[] src_port = {"--"};
    public String[] dst_port = {"--"};
    //public String[] eth_trailer = {""};
    //public String[] eth_fcs = {""};
    public String timeStamp = "";
    public String[] tcp_payload = {""};
    @JSONField(deserialize = false)
    public String funCode = "--";
    @JSONField(deserialize = false)
    public String funCodeMeaning = "--";
    //long格式的时间戳 , ns 为单位
    @JSONField(serialize = false)
    public long timeStampInLong = -1;
    public String[] tcp_flags_ack={""};
    public String[] tcp_flags_syn={""};
    //raw data
    public String[] custom_ext_raw_data = {""};
    @JSONField(deserialize = false)
    public int delay;
    @JSONField(deserialize = false)
    public int collectorId;
    @JSONField(serialize = false,deserialize = false)
    public byte[] rawData;
    @JSONField(serialize = false,deserialize = false)
    public String deviceNumber;     //该条报文对应的设备号
    @JSONField(serialize = false,deserialize = false)
    public byte[] tcpPayload;


    @Override
    public String toString() {
        return "FvDimensionLayer{" +
                "frame_protocols=" + Arrays.toString(frame_protocols) +
                ", eth_dst=" + Arrays.toString(eth_dst) +
                ", frame_cap_len=" + Arrays.toString(frame_cap_len) +
                ", eth_src=" + Arrays.toString(eth_src) +
                ", ip_src=" + Arrays.toString(ip_src) +
                ", ip_dst=" + Arrays.toString(ip_dst) +
                ", src_port=" + Arrays.toString(src_port) +
                ", dst_port=" + Arrays.toString(dst_port) +
                ", timeStamp='" + timeStamp + '\'' +
                ", tcp_payload=" + Arrays.toString(tcp_payload) +
                ", funCode=" + funCode +
                ", funCodeMeaning='" + funCodeMeaning + '\'' +
                '}';
    }


    @Override
    public int compareTo(FvDimensionLayer o) {
        if (o.timeStampInLong == timeStampInLong){
            return 0;
        }else {
            return o.timeStampInLong > timeStampInLong ? -1 : 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FvDimensionLayer)) return false;
        FvDimensionLayer layer = (FvDimensionLayer) o;
        assert getTimeStampInLong() > 0 && layer.getTimeStampInLong() > 0;
        return getTimeStampInLong() == layer.getTimeStampInLong();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimeStampInLong());
    }

    /**
     * timestamp : 1568943955164
     * layers : {"frame":{"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{8E98ECE7-BD55-4074-B5B8-2D357F85491A}","frame_frame_encap_type":"1","frame_frame_time":"Sep 20, 2019 09:45:55.164955000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1568943955.164955000","frame_frame_time_delta":"0.000000000","frame_frame_time_delta_displayed":"0.000000000","frame_frame_time_relative":"0.000000000","frame_frame_number":"1","frame_frame_len":"222","frame_frame_cap_len":"222","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:udp:mdns"},"eth":{"eth_eth_dst":"01:00:5e:00:00:fb","eth_dst_eth_dst_resolved":"IPv4mcast_fb","eth_dst_eth_addr":"01:00:5e:00:00:fb","eth_dst_eth_addr_resolved":"IPv4mcast_fb","eth_dst_eth_lg":"0","eth_dst_eth_ig":"1","eth_eth_src":"40:83:1d:ab:5f:63","eth_src_eth_src_resolved":"Apple_ab:5f:63","eth_src_eth_addr":"40:83:1d:ab:5f:63","eth_src_eth_addr_resolved":"Apple_ab:5f:63","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800"},"ip":{"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"208","ip_ip_id":"0x0000a1ad","ip_ip_flags":"0x00000000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"0","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"255","ip_ip_proto":"17","ip_ip_checksum":"0x00007734","ip_ip_checksum_status":"2","ip_ip_src":"192.168.0.151","ip_ip_addr":["192.168.0.151","224.0.0.251"],"ip_ip_src_host":"192.168.0.151","ip_ip_host":["192.168.0.151","224.0.0.251"],"ip_ip_dst":"224.0.0.251","ip_ip_dst_host":"224.0.0.251"},"udp":{"udp_udp_srcport":"5353","udp_udp_dstport":"5353","udp_udp_port":["5353","5353"],"udp_udp_length":"188","udp_udp_checksum":"0x0000f710","udp_udp_checksum_status":"2","udp_udp_stream":"0"},"mdns":{"mdns_dns_id":"0x00000000","mdns_dns_flags":"0x00000000","dns_flags_dns_flags_response":"0","dns_flags_dns_flags_opcode":"0","dns_flags_dns_flags_truncated":"0","dns_flags_dns_flags_recdesired":"0","dns_flags_dns_flags_z":"0","dns_flags_dns_flags_checkdisable":"0","mdns_dns_count_queries":"3","mdns_dns_count_answers":"2","mdns_dns_count_auth_rr":"0","mdns_dns_count_add_rr":"1","mdns_text":["Queries","Answers","Additional records"],"text_text":["_companion-link._tcp.local: type PTR, class IN, \"QM\" question","_homekit._tcp.local: type PTR, class IN, \"QM\" question","_sleep-proxy._udp.local: type PTR, class IN, \"QM\" question","_companion-link._tcp.local: type PTR, class IN, Mayi_MacBookPro._companion-link._tcp.local","_companion-link._tcp.local: type PTR, class IN, \\345\\247\\234\\346\\234\\211\\344\\272\\256\\347\\232\\204MacBook Pro._companion-link._tcp.local","<Root>: type OPT"],"text_dns_qry_name":["_companion-link._tcp.local","_homekit._tcp.local","_sleep-proxy._udp.local"],"text_dns_qry_name_len":["26","19","23"],"text_dns_count_labels":["3","3","3"],"text_dns_qry_type":["12","12","12"],"text_dns_qry_class":["0x00000001","0x00000001","0x00000001"],"text_dns_qry_qu":["0","0","0"],"text_dns_resp_name":["_companion-link._tcp.local","_companion-link._tcp.local","<Root>"],"text_dns_resp_type":["12","12","41"],"text_dns_resp_class":["0x00000001","0x00000001"],"text_dns_resp_cache_flush":["0","0","0"],"text_dns_resp_ttl":["3597","3597"],"text_dns_resp_len":["18","26","18"],"text_dns_ptr_domain_name":["Mayi_MacBookPro._companion-link._tcp.local","\\345\\247\\234\\346\\234\\211\\344\\272\\256\\347\\232\\204MacBook Pro._companion-link._tcp.local"],"text_dns_rr_udp_payload_size":"0x000005a0","text_dns_resp_ext_rcode":"0x00000000","text_dns_resp_edns0_version":"0","text_dns_resp_z":"0x00001194","dns_resp_z_dns_resp_z_do":"0","dns_resp_z_dns_resp_z_reserved":"0x00001194","text_dns_opt":"","dns_opt_dns_opt_code":"4","dns_opt_dns_opt_len":"14","dns_opt_dns_opt_data":"00:67:62:83:1d:ab:5f:63:40:83:1d:ab:5f:63"},"_ws_lua_fake":"","ext":{"ext_custom_ext_raw_data":"01005E0000FB40831DAB5F630800450000D0A1AD0000FF117734C0A80097E00000FB14E914E900BCF7100000000000030002000000010F5F636F6D70616E696F6E2D6C696E6B045F746370056C6F63616C00000C0001085F686F6D656B6974C01C000C00010C5F736C6565702D70726F7879045F756470C021000C0001C00C000C000100000E0D00120F4D6179695F4D6163426F6F6B50726FC00CC00C000C000100000E0D001A17E5A79CE69C89E4BAAEE79A844D6163426F6F6B2050726FC00C00002905A00000119400120004000E006762831DAB5F6340831DAB5F63","ext_pf_info":"nil"}}
     */

    private String timestamp;

    /**
     * layers : {"frame":{"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{8E98ECE7-BD55-4074-B5B8-2D357F85491A}","frame_frame_encap_type":"1","frame_frame_time":"Sep 20, 2019 09:45:55.164955000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1568943955.164955000","frame_frame_time_delta":"0.000000000","frame_frame_time_delta_displayed":"0.000000000","frame_frame_time_relative":"0.000000000","frame_frame_number":"1","frame_frame_len":"222","frame_frame_cap_len":"222","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:udp:mdns"},"eth":{"eth_eth_dst":"01:00:5e:00:00:fb","eth_dst_eth_dst_resolved":"IPv4mcast_fb","eth_dst_eth_addr":"01:00:5e:00:00:fb","eth_dst_eth_addr_resolved":"IPv4mcast_fb","eth_dst_eth_lg":"0","eth_dst_eth_ig":"1","eth_eth_src":"40:83:1d:ab:5f:63","eth_src_eth_src_resolved":"Apple_ab:5f:63","eth_src_eth_addr":"40:83:1d:ab:5f:63","eth_src_eth_addr_resolved":"Apple_ab:5f:63","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800"},"ip":{"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"208","ip_ip_id":"0x0000a1ad","ip_ip_flags":"0x00000000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"0","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"255","ip_ip_proto":"17","ip_ip_checksum":"0x00007734","ip_ip_checksum_status":"2","ip_ip_src":"192.168.0.151","ip_ip_addr":["192.168.0.151","224.0.0.251"],"ip_ip_src_host":"192.168.0.151","ip_ip_host":["192.168.0.151","224.0.0.251"],"ip_ip_dst":"224.0.0.251","ip_ip_dst_host":"224.0.0.251"},"udp":{"udp_udp_srcport":"5353","udp_udp_dstport":"5353","udp_udp_port":["5353","5353"],"udp_udp_length":"188","udp_udp_checksum":"0x0000f710","udp_udp_checksum_status":"2","udp_udp_stream":"0"},"mdns":{"mdns_dns_id":"0x00000000","mdns_dns_flags":"0x00000000","dns_flags_dns_flags_response":"0","dns_flags_dns_flags_opcode":"0","dns_flags_dns_flags_truncated":"0","dns_flags_dns_flags_recdesired":"0","dns_flags_dns_flags_z":"0","dns_flags_dns_flags_checkdisable":"0","mdns_dns_count_queries":"3","mdns_dns_count_answers":"2","mdns_dns_count_auth_rr":"0","mdns_dns_count_add_rr":"1","mdns_text":["Queries","Answers","Additional records"],"text_text":["_companion-link._tcp.local: type PTR, class IN, \"QM\" question","_homekit._tcp.local: type PTR, class IN, \"QM\" question","_sleep-proxy._udp.local: type PTR, class IN, \"QM\" question","_companion-link._tcp.local: type PTR, class IN, Mayi_MacBookPro._companion-link._tcp.local","_companion-link._tcp.local: type PTR, class IN, \\345\\247\\234\\346\\234\\211\\344\\272\\256\\347\\232\\204MacBook Pro._companion-link._tcp.local","<Root>: type OPT"],"text_dns_qry_name":["_companion-link._tcp.local","_homekit._tcp.local","_sleep-proxy._udp.local"],"text_dns_qry_name_len":["26","19","23"],"text_dns_count_labels":["3","3","3"],"text_dns_qry_type":["12","12","12"],"text_dns_qry_class":["0x00000001","0x00000001","0x00000001"],"text_dns_qry_qu":["0","0","0"],"text_dns_resp_name":["_companion-link._tcp.local","_companion-link._tcp.local","<Root>"],"text_dns_resp_type":["12","12","41"],"text_dns_resp_class":["0x00000001","0x00000001"],"text_dns_resp_cache_flush":["0","0","0"],"text_dns_resp_ttl":["3597","3597"],"text_dns_resp_len":["18","26","18"],"text_dns_ptr_domain_name":["Mayi_MacBookPro._companion-link._tcp.local","\\345\\247\\234\\346\\234\\211\\344\\272\\256\\347\\232\\204MacBook Pro._companion-link._tcp.local"],"text_dns_rr_udp_payload_size":"0x000005a0","text_dns_resp_ext_rcode":"0x00000000","text_dns_resp_edns0_version":"0","text_dns_resp_z":"0x00001194","dns_resp_z_dns_resp_z_do":"0","dns_resp_z_dns_resp_z_reserved":"0x00001194","text_dns_opt":"","dns_opt_dns_opt_code":"4","dns_opt_dns_opt_len":"14","dns_opt_dns_opt_data":"00:67:62:83:1d:ab:5f:63:40:83:1d:ab:5f:63"},"_ws_lua_fake":"","ext":{"ext_custom_ext_raw_data":"01005E0000FB40831DAB5F630800450000D0A1AD0000FF117734C0A80097E00000FB14E914E900BCF7100000000000030002000000010F5F636F6D70616E696F6E2D6C696E6B045F746370056C6F63616C00000C0001085F686F6D656B6974C01C000C00010C5F736C6565702D70726F7879045F756470C021000C0001C00C000C000100000E0D00120F4D6179695F4D6163426F6F6B50726FC00CC00C000C000100000E0D001A17E5A79CE69C89E4BAAEE79A844D6163426F6F6B2050726FC00C00002905A00000119400120004000E006762831DAB5F6340831DAB5F63","ext_pf_info":"nil"}}
     */

    private LayersBean layers;

    public LayersBean getLayers() {
        return layers;
    }

    public void setLayers(LayersBean layers) {
        this.layers = layers;
    }

    @Data
    public static class LayersBean {
        /**
         * frame : {"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{8E98ECE7-BD55-4074-B5B8-2D357F85491A}","frame_frame_encap_type":"1","frame_frame_time":"Sep 20, 2019 09:45:55.164955000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1568943955.164955000","frame_frame_time_delta":"0.000000000","frame_frame_time_delta_displayed":"0.000000000","frame_frame_time_relative":"0.000000000","frame_frame_number":"1","frame_frame_len":"222","frame_frame_cap_len":"222","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:udp:mdns"}
         * eth : {"eth_eth_dst":"01:00:5e:00:00:fb","eth_dst_eth_dst_resolved":"IPv4mcast_fb","eth_dst_eth_addr":"01:00:5e:00:00:fb","eth_dst_eth_addr_resolved":"IPv4mcast_fb","eth_dst_eth_lg":"0","eth_dst_eth_ig":"1","eth_eth_src":"40:83:1d:ab:5f:63","eth_src_eth_src_resolved":"Apple_ab:5f:63","eth_src_eth_addr":"40:83:1d:ab:5f:63","eth_src_eth_addr_resolved":"Apple_ab:5f:63","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800"}
         * ip : {"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"208","ip_ip_id":"0x0000a1ad","ip_ip_flags":"0x00000000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"0","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"255","ip_ip_proto":"17","ip_ip_checksum":"0x00007734","ip_ip_checksum_status":"2","ip_ip_src":"192.168.0.151","ip_ip_addr":["192.168.0.151","224.0.0.251"],"ip_ip_src_host":"192.168.0.151","ip_ip_host":["192.168.0.151","224.0.0.251"],"ip_ip_dst":"224.0.0.251","ip_ip_dst_host":"224.0.0.251"}
         * udp : {"udp_udp_srcport":"5353","udp_udp_dstport":"5353","udp_udp_port":["5353","5353"],"udp_udp_length":"188","udp_udp_checksum":"0x0000f710","udp_udp_checksum_status":"2","udp_udp_stream":"0"}
         * mdns : {"mdns_dns_id":"0x00000000","mdns_dns_flags":"0x00000000","dns_flags_dns_flags_response":"0","dns_flags_dns_flags_opcode":"0","dns_flags_dns_flags_truncated":"0","dns_flags_dns_flags_recdesired":"0","dns_flags_dns_flags_z":"0","dns_flags_dns_flags_checkdisable":"0","mdns_dns_count_queries":"3","mdns_dns_count_answers":"2","mdns_dns_count_auth_rr":"0","mdns_dns_count_add_rr":"1","mdns_text":["Queries","Answers","Additional records"],"text_text":["_companion-link._tcp.local: type PTR, class IN, \"QM\" question","_homekit._tcp.local: type PTR, class IN, \"QM\" question","_sleep-proxy._udp.local: type PTR, class IN, \"QM\" question","_companion-link._tcp.local: type PTR, class IN, Mayi_MacBookPro._companion-link._tcp.local","_companion-link._tcp.local: type PTR, class IN, \\345\\247\\234\\346\\234\\211\\344\\272\\256\\347\\232\\204MacBook Pro._companion-link._tcp.local","<Root>: type OPT"],"text_dns_qry_name":["_companion-link._tcp.local","_homekit._tcp.local","_sleep-proxy._udp.local"],"text_dns_qry_name_len":["26","19","23"],"text_dns_count_labels":["3","3","3"],"text_dns_qry_type":["12","12","12"],"text_dns_qry_class":["0x00000001","0x00000001","0x00000001"],"text_dns_qry_qu":["0","0","0"],"text_dns_resp_name":["_companion-link._tcp.local","_companion-link._tcp.local","<Root>"],"text_dns_resp_type":["12","12","41"],"text_dns_resp_class":["0x00000001","0x00000001"],"text_dns_resp_cache_flush":["0","0","0"],"text_dns_resp_ttl":["3597","3597"],"text_dns_resp_len":["18","26","18"],"text_dns_ptr_domain_name":["Mayi_MacBookPro._companion-link._tcp.local","\\345\\247\\234\\346\\234\\211\\344\\272\\256\\347\\232\\204MacBook Pro._companion-link._tcp.local"],"text_dns_rr_udp_payload_size":"0x000005a0","text_dns_resp_ext_rcode":"0x00000000","text_dns_resp_edns0_version":"0","text_dns_resp_z":"0x00001194","dns_resp_z_dns_resp_z_do":"0","dns_resp_z_dns_resp_z_reserved":"0x00001194","text_dns_opt":"","dns_opt_dns_opt_code":"4","dns_opt_dns_opt_len":"14","dns_opt_dns_opt_data":"00:67:62:83:1d:ab:5f:63:40:83:1d:ab:5f:63"}
         * _ws_lua_fake :
         * ext : {"ext_custom_ext_raw_data":"01005E0000FB40831DAB5F630800450000D0A1AD0000FF117734C0A80097E00000FB14E914E900BCF7100000000000030002000000010F5F636F6D70616E696F6E2D6C696E6B045F746370056C6F63616C00000C0001085F686F6D656B6974C01C000C00010C5F736C6565702D70726F7879045F756470C021000C0001C00C000C000100000E0D00120F4D6179695F4D6163426F6F6B50726FC00CC00C000C000100000E0D001A17E5A79CE69C89E4BAAEE79A844D6163426F6F6B2050726FC00C00002905A00000119400120004000E006762831DAB5F6340831DAB5F63","ext_pf_info":"nil"}
         */

        private LayersBean.FrameBean frame;
        private LayersBean.EthBean eth;
        private LayersBean.IpBean ip;
        private LayersBean.UdpBean udp;
        private LayersBean.TcpBean tcp;
        //private MdnsBean mdns;
        private LayersBean.ExtBean ext;
        private LayersBean.Dnp3Bean dnp;
        @JSONField(name = "104apci")
        private LayersBean._104apciBean _104apci;
        private LayersBean.S7commBean s7comm;
        /**
         * timestamp : 1557726389081
         * layers : {"frame":{"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{E489F2AB-C267-46ED-9E13-0AABE468189E}","frame_interface_id_frame_interface_description":"en0","frame_frame_encap_type":"1","frame_frame_time":"May 13, 2019 13:46:29.081213000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1557726389.081213000","frame_frame_time_delta":"0.000000000","frame_frame_time_delta_displayed":"0.000000000","frame_frame_time_relative":"0.000000000","frame_frame_number":"1","frame_frame_len":"84","frame_frame_cap_len":"84","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:tcp"},"eth":{"eth_eth_dst":"14:18:77:57:15:55","eth_dst_eth_dst_resolved":"Dell_57:15:55","eth_dst_eth_addr":"14:18:77:57:15:55","eth_dst_eth_addr_resolved":"Dell_57:15:55","eth_dst_eth_lg":"0","eth_dst_eth_ig":"0","eth_eth_src":"00:1d:51:fc:75:52","eth_src_eth_src_resolved":"BabcockW_fc:75:52","eth_src_eth_addr":"00:1d:51:fc:75:52","eth_src_eth_addr_resolved":"BabcockW_fc:75:52","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800","eth_eth_padding":"00:00:00:00:00:00","eth_eth_trailer":"00:03:0d:0d:00:00:00:99:dc:31:8f:90:00:00:00:99:dc:31:90:00","eth_eth_fcs":"0x0000005d","eth_fcs__ws_expert":{"_ws_expert_eth_fcs_bad":"","_ws_expert__ws_expert_message":"Bad checksum [should be 0x44769767]","_ws_expert__ws_expert_severity":"8388608","_ws_expert__ws_expert_group":"16777216"},"eth_eth_fcs_status":"0"},"ip":{"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"40","ip_ip_id":"0x000082e1","ip_ip_flags":"0x00000000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"0","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"60","ip_ip_proto":"6","ip_ip_checksum":"0x0000d13a","ip_ip_checksum_status":"2","ip_ip_src":"10.30.11.120","ip_ip_addr":["10.30.11.120","10.30.11.1"],"ip_ip_src_host":"10.30.11.120","ip_ip_host":["10.30.11.120","10.30.11.1"],"ip_ip_dst":"10.30.11.1","ip_ip_dst_host":"10.30.11.1"},"tcp":{"tcp_tcp_srcport":"20000","tcp_tcp_dstport":"52168","tcp_tcp_port":["20000","52168"],"tcp_tcp_stream":"0","tcp_tcp_len":"0","tcp_tcp_seq":"1","tcp_tcp_nxtseq":"1","tcp_tcp_ack":"1","tcp_tcp_hdr_len":"20","tcp_tcp_flags":"0x00000010","tcp_flags_tcp_flags_res":"0","tcp_flags_tcp_flags_ns":"0","tcp_flags_tcp_flags_cwr":"0","tcp_flags_tcp_flags_ecn":"0","tcp_flags_tcp_flags_urg":"0","tcp_flags_tcp_flags_ack":"1","tcp_flags_tcp_flags_push":"0","tcp_flags_tcp_flags_reset":"0","tcp_flags_tcp_flags_syn":"0","tcp_flags_tcp_flags_fin":"0","tcp_flags_tcp_flags_str":"Â·Â·Â·Â·Â·Â·Â·AÂ·Â·Â·Â·","tcp_tcp_window_size_value":"4096","tcp_tcp_window_size":"4096","tcp_tcp_window_size_scalefactor":"-1","tcp_tcp_checksum":"0x00004860","tcp_tcp_checksum_status":"2","tcp_tcp_urgent_pointer":"0","tcp_text":"Timestamps","text_tcp_time_relative":"0.000000000","text_tcp_time_delta":"0.000000000"},"_ws_lua_fake":"","ext":{"ext_custom_ext_raw_data":"141877571555001D51FC755208004500002882E100003C06D13A0A1E0B780A1E0B014E20CBC89E8BD32041C85F62501010004860000000000000000000030D0D00000099DC318F9000000099DC3190000000005D","ext_pf_info":"nil"}}
         */
        @Data
        public static class FrameBean {
            /**
             * frame_frame_interface_id : 0
             * frame_interface_id_frame_interface_name : \Device\NPF_{8E98ECE7-BD55-4074-B5B8-2D357F85491A}
             * frame_frame_encap_type : 1
             * frame_frame_time : Sep 20, 2019 09:45:55.164955000 ä¸­å½æ åæ¶é´
             * frame_frame_offset_shift : 0.000000000
             * frame_frame_time_epoch : 1568943955.164955000
             * frame_frame_time_delta : 0.000000000
             * frame_frame_time_delta_displayed : 0.000000000
             * frame_frame_time_relative : 0.000000000
             * frame_frame_number : 1
             * frame_frame_len : 222
             * frame_frame_cap_len : 222
             * frame_frame_marked : 0
             * frame_frame_ignored : 0
             * frame_frame_protocols : eth:ethertype:ip:udp:mdns
             */

//            private String frame_frame_interface_id;
//            private String frame_interface_id_frame_interface_name;
            private String frame_frame_encap_type;
//            private String frame_frame_time;
//            private String frame_frame_offset_shift;
//            private String frame_frame_time_epoch;
//            private String frame_frame_time_delta;
//            private String frame_frame_time_delta_displayed;
//            private String frame_frame_time_relative;
//            private String frame_frame_number;

            private String frame_frame_len;
            private String frame_frame_cap_len;
            //            private String frame_frame_marked;
//            private String frame_frame_ignored;
            private String frame_frame_protocols;
        }

        @Data
        public static class EthBean {
            /**
             * eth_eth_dst : 01:00:5e:00:00:fb
             * eth_dst_eth_dst_resolved : IPv4mcast_fb
             * eth_dst_eth_addr : 01:00:5e:00:00:fb
             * eth_dst_eth_addr_resolved : IPv4mcast_fb
             * eth_dst_eth_lg : 0
             * eth_dst_eth_ig : 1
             * eth_eth_src : 40:83:1d:ab:5f:63
             * eth_src_eth_src_resolved : Apple_ab:5f:63
             * eth_src_eth_addr : 40:83:1d:ab:5f:63
             * eth_src_eth_addr_resolved : Apple_ab:5f:63
             * eth_src_eth_lg : 0
             * eth_src_eth_ig : 0
             * eth_eth_type : 0x00000800
             */

            private String eth_eth_dst;
            //            private String eth_dst_eth_dst_resolved;
//            private String eth_dst_eth_addr;
            private String eth_dst_eth_addr_resolved;
            //            private String eth_dst_eth_lg;
//            private String eth_dst_eth_ig;
            private String eth_eth_src;
            //            private String eth_src_eth_src_resolved;
//            private String eth_src_eth_addr;
            private String eth_src_eth_addr_resolved;
            //            private String eth_src_eth_lg;
//            private String eth_src_eth_ig;
            private String eth_eth_type;
        }

        @Data
        public static class IpBean {
            /**
             * ip_ip_version : 4
             * ip_ip_hdr_len : 20
             * ip_ip_dsfield : 0x00000000
             * ip_dsfield_ip_dsfield_dscp : 0
             * ip_dsfield_ip_dsfield_ecn : 0
             * ip_ip_len : 208
             * ip_ip_id : 0x0000a1ad
             * ip_ip_flags : 0x00000000
             * ip_flags_ip_flags_rb : 0
             * ip_flags_ip_flags_df : 0
             * ip_flags_ip_flags_mf : 0
             * ip_flags_ip_frag_offset : 0
             * ip_ip_ttl : 255
             * ip_ip_proto : 17
             * ip_ip_checksum : 0x00007734
             * ip_ip_checksum_status : 2
             * ip_ip_src : 192.168.0.151
             * ip_ip_addr : ["192.168.0.151","224.0.0.251"]
             * ip_ip_src_host : 192.168.0.151
             * ip_ip_host : ["192.168.0.151","224.0.0.251"]
             * ip_ip_dst : 224.0.0.251
             * ip_ip_dst_host : 224.0.0.251
             */

            private String ip_ip_version;
            //            private String ip_ip_hdr_len;
//            private String ip_ip_dsfield;
//            private String ip_dsfield_ip_dsfield_dscp;
//            private String ip_dsfield_ip_dsfield_ecn;
//            private String ip_ip_len;
//            private String ip_ip_id;
//            private String ip_ip_flags;
//            private String ip_flags_ip_flags_rb;
//            private String ip_flags_ip_flags_df;
//            private String ip_flags_ip_flags_mf;
//            private String ip_flags_ip_frag_offset;
//            private String ip_ip_ttl;
//            private String ip_ip_proto;
//            private String ip_ip_checksum;
//            private String ip_ip_checksum_status;
            private String ip_ip_src;
            //            private String ip_ip_src_host;
            private String ip_ip_dst;
//            private String ip_ip_dst_host;
//            private List<String> ip_ip_addr;
//            private List<String> ip_ip_host;
        }

        @Data
        public static class UdpBean {
            /**
             * udp_udp_srcport : 5353
             * udp_udp_dstport : 5353
             * udp_udp_port : ["5353","5353"]
             * udp_udp_length : 188
             * udp_udp_checksum : 0x0000f710
             * udp_udp_checksum_status : 2
             * udp_udp_stream : 0
             */

            private String udp_udp_srcport;
            private String udp_udp_dstport;
//            private String udp_udp_length;
//            private String udp_udp_checksum;
//            private String udp_udp_checksum_status;
//            private String udp_udp_stream;
//            private List<String> udp_udp_port;
        }

        @Data
        public static class ExtBean {
            /**
             * ext_custom_ext_raw_data : 01005E0000FB40831DAB5F630800450000D0A1AD0000FF117734C0A80097E00000FB14E914E900BCF7100000000000030002000000010F5F636F6D70616E696F6E2D6C696E6B045F746370056C6F63616C00000C0001085F686F6D656B6974C01C000C00010C5F736C6565702D70726F7879045F756470C021000C0001C00C000C000100000E0D00120F4D6179695F4D6163426F6F6B50726FC00CC00C000C000100000E0D001A17E5A79CE69C89E4BAAEE79A844D6163426F6F6B2050726FC00C00002905A00000119400120004000E006762831DAB5F6340831DAB5F63
             * ext_pf_info : nil
             */

            private String ext_custom_ext_raw_data;
        }

        @Data
        public static class TcpBean {
            /**
             * tcp_tcp_srcport : 20000
             * tcp_tcp_dstport : 52168
             * tcp_tcp_port : ["20000","52168"]
             * tcp_tcp_stream : 0
             * tcp_tcp_len : 0
             * tcp_tcp_seq : 1
             * tcp_tcp_nxtseq : 1
             * tcp_tcp_ack : 1
             * tcp_tcp_hdr_len : 20
             * tcp_tcp_flags : 0x00000010
             * tcp_flags_tcp_flags_res : 0
             * tcp_flags_tcp_flags_ns : 0
             * tcp_flags_tcp_flags_cwr : 0
             * tcp_flags_tcp_flags_ecn : 0
             * tcp_flags_tcp_flags_urg : 0
             * tcp_flags_tcp_flags_ack : 1
             * tcp_flags_tcp_flags_push : 0
             * tcp_flags_tcp_flags_reset : 0
             * tcp_flags_tcp_flags_syn : 0
             * tcp_flags_tcp_flags_fin : 0
             * tcp_flags_tcp_flags_str : Â·Â·Â·Â·Â·Â·Â·AÂ·Â·Â·Â·
             * tcp_tcp_window_size_value : 4096
             * tcp_tcp_window_size : 4096
             * tcp_tcp_window_size_scalefactor : -1
             * tcp_tcp_checksum : 0x00004860
             * tcp_tcp_checksum_status : 2
             * tcp_tcp_urgent_pointer : 0
             * tcp_text : Timestamps
             * text_tcp_time_relative : 0.000000000
             * text_tcp_time_delta : 0.000000000
             */

            private String tcp_tcp_srcport;
            private String tcp_tcp_dstport;
            //            private String tcp_tcp_stream;
//            private String tcp_tcp_len;
            private String tcp_tcp_seq;
            //            private String tcp_tcp_nxtseq;
            private String tcp_tcp_ack;
            //            private String tcp_tcp_hdr_len;
            private String tcp_tcp_flags;
            //            private String tcp_flags_tcp_flags_res;
//            private String tcp_flags_tcp_flags_ns;
//            private String tcp_flags_tcp_flags_cwr;
//            private String tcp_flags_tcp_flags_ecn;
//            private String tcp_flags_tcp_flags_urg;
            private String tcp_flags_tcp_flags_ack;
            private String tcp_flags_tcp_flags_push;
            private String tcp_flags_tcp_flags_reset;
            private String tcp_flags_tcp_flags_syn;
            private String tcp_flags_tcp_flags_fin;
            private String tcp_flags_tcp_flags_str;
//            private String tcp_tcp_window_size_value;
//            private String tcp_tcp_window_size;
//            private String tcp_tcp_window_size_scalefactor;
//            private String tcp_tcp_checksum;
//            private String tcp_tcp_checksum_status;
//            private String tcp_tcp_urgent_pointer;
//            private String tcp_text;
//            private String text_tcp_time_relative;
//            private String text_tcp_time_delta;
//            private List<String> tcp_tcp_port;
        }

        @Data
        public static class Dnp3Bean {
            /**
             * dnp3_text : Data Link Layer, Len: 5, From: 21, To: 12, ACK
             * text_dnp3_start : 0x00000564
             * text_dnp3_len : 5
             * text_dnp3_ctl : 0x00000000
             * dnp3_ctl_dnp3_ctl_dir : 0
             * dnp3_ctl_dnp3_ctl_prm : 0
             * dnp3_ctl_dnp3_ctl_dfc : 0
             * dnp3_ctl_dnp3_ctl_secfunc : 0
             * text_dnp3_dst : 12
             * text_dnp3_addr : ["12","21"]
             * text_dnp3_src : 21
             * text_dnp3_hdr_CRC : 0x0000011f
             * text_dnp_hdr_CRC_status : 1
             */

//            private String dnp3_text;
//            private String text_dnp3_start;
//            private String text_dnp3_len;
//            private String text_dnp3_ctl;
//            private String dnp3_ctl_dnp3_ctl_dir;
            private String dnp3_ctl_dnp3_ctl_prm;
            private String dnp3_ctl_dnp3_ctl_dfc;
            private String dnp3_ctl_dnp3_ctl_secfunc;
//            private String text_dnp3_dst;
//            private String text_dnp3_src;
//            private String text_dnp3_hdr_CRC;
//            private String text_dnp_hdr_CRC_status;
//            private List<String> text_dnp3_addr;
        }

        @Data
        public static class _104apciBean {
            /**
             * 104apci_104asdu_start : 0x00000068
             * 104apci_104apci_apdulen : 4
             * 104apci_104apci_type : 0x00000003
             * 104apci_104apci_utype : 0x00000010
             */

            @JSONField(name = "104apci_104asdu_start")
            private String _104apci_104asdu_start;
            @JSONField(name = "104apci_104apci_apdulen")
            private String _104apci_104apci_apdulen;
            @JSONField(name = "104apci_104apci_type")
            private String _104apci_104apci_type;
            @JSONField(name = "104apci_104apci_utype")
            private String _104apci_104apci_utype;
        }


        @Data
        public static class GooseBean {
            /**
             * goose_goose_appid : 0x00001007
             * goose_goose_length : 183
             * goose_goose_reserve1 : 0x00000000
             * goose_goose_reserve2 : 0x00000000
             * goose_goose_goosePdu_element :
             * goose_goosePdu_element_goose_gocbRef : SEL_2411_106CFG/LLN0$GO$GooseDSet13
             * goose_goosePdu_element_goose_timeAllowedtoLive : 2000
             * goose_goosePdu_element_goose_datSet : SEL_2411_106CFG/LLN0$DSet13
             * goose_goosePdu_element_goose_goID : Sub2Bay1
             * goose_goosePdu_element_goose_t : May 13, 2019 13:50:52.228996276 UTC
             * goose_goosePdu_element_goose_stNum : 9
             * goose_goosePdu_element_goose_sqNum : 5262777
             * goose_goosePdu_element_goose_test : 0
             * goose_goosePdu_element_goose_confRev : 1
             * goose_goosePdu_element_goose_ndsCom : 0
             * goose_goosePdu_element_goose_numDatSetEntries : 12
             * goose_goosePdu_element_goose_allData : 12
             * goose_allData_goose_Data : ["3","3","3","3","3","3","3","3","2","2","2","2"]
             * goose_Data_goose_boolean : ["0","1","0","0","0","0","0","0"]
             * goose_Data_goose_structure : ["1","1","1","1"]
             * goose_structure_goose_Data : ["7","7","7","7"]
             * goose_Data_goose_floating_point : ["08:00:00:00:00","08:00:00:00:00","08:00:00:00:00","08:00:00:00:00"]
             */

            private String goose_goose_appid;
            private String goose_goose_length;
            private String goose_goose_reserve1;
            private String goose_goose_reserve2;
            private String goose_goose_goosePdu_element;
            private String goose_goosePdu_element_goose_gocbRef;
            private String goose_goosePdu_element_goose_timeAllowedtoLive;
            private String goose_goosePdu_element_goose_datSet;
            private String goose_goosePdu_element_goose_goID;
            private String goose_goosePdu_element_goose_t;
            private String goose_goosePdu_element_goose_stNum;
            private String goose_goosePdu_element_goose_sqNum;
            private String goose_goosePdu_element_goose_test;
            private String goose_goosePdu_element_goose_confRev;
            private String goose_goosePdu_element_goose_ndsCom;
            private String goose_goosePdu_element_goose_numDatSetEntries;
            private String goose_goosePdu_element_goose_allData;
            private List<String> goose_allData_goose_Data;
            private List<String> goose_Data_goose_boolean;
            private List<String> goose_Data_goose_structure;
            private List<String> goose_structure_goose_Data;
            private List<String> goose_Data_goose_floating_point;
        }

        @Data
        public static class S7commBean {
            /**
             * s7comm_s7comm_header :
             * s7comm_header_s7comm_header_protid : 0x00000032
             * s7comm_header_s7comm_header_rosctr : 1
             * s7comm_header_s7comm_header_redid : 0x00000000
             * s7comm_header_s7comm_header_pduref : 512
             * s7comm_header_s7comm_header_parlg : 8
             * s7comm_header_s7comm_header_datlg : 0
             * s7comm_s7comm_param :
             * s7comm_param_s7comm_param_func : 0x000000f0
             * s7comm_param_s7comm_param_setup_reserved1 : 0x00000000
             * s7comm_param_s7comm_param_maxamq_calling : 1
             * s7comm_param_s7comm_param_maxamq_called : 1
             * s7comm_param_s7comm_param_pdu_length : 480
             */

            private String s7comm_s7comm_header;
            private String s7comm_header_s7comm_header_protid;
            private String s7comm_header_s7comm_header_rosctr;
            private String s7comm_header_s7comm_header_redid;
            private String s7comm_header_s7comm_header_pduref;
            private String s7comm_header_s7comm_header_parlg;
            private String s7comm_header_s7comm_header_datlg;
            private String s7comm_s7comm_param;
            private String s7comm_param_s7comm_param_func;
            private String s7comm_param_s7comm_param_setup_reserved1;
            private String s7comm_param_s7comm_param_maxamq_calling;
            private String s7comm_param_s7comm_param_maxamq_called;
            private String s7comm_param_s7comm_param_pdu_length;
        }
    }

}
