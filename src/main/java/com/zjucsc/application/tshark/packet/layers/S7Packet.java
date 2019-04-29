package com.zjucsc.application.tshark.packet.layers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.application.tshark.packet.FiveDimensionPacket;

import java.util.List;

public class S7Packet {



    public static String decode(String s7json){
        S7Packet s7Packet = JSON.parseObject(s7json, S7Packet.class);
        S7Packet.LayersBean layers = s7Packet.getLayers();
        return JSON.toJSONString(new FiveDimensionPacket(
                layers.getIp().getIp_ip_src(),
                layers.getIp().getIp_ip_dst(),
                layers.getTcp().getTcp_tcp_srcport(),
                layers.getTcp().getTcp_tcp_dstport(),
                layers.getS7comm().getS7comm_param_s7comm_param_func()
        ));
    }

    /**
     * timestamp : 946695632244
     * layers : {"frame":{"frame_frame_encap_type":"1","frame_frame_time":"Jan  1, 2000 11:00:32.244216000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"946695632.244216000","frame_frame_time_delta":"0.009438000","frame_frame_time_delta_displayed":"0.009438000","frame_frame_time_relative":"0.042628000","frame_frame_number":"5","frame_frame_len":"85","frame_frame_cap_len":"85","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:tcp:tpkt:cotp:s7comm"},"eth":{"eth_eth_dst":"00:0c:29:49:7e:9f","eth_dst_eth_dst_resolved":"00:0c:29:49:7e:9f","eth_dst_eth_addr":"00:0c:29:49:7e:9f","eth_dst_eth_addr_resolved":"00:0c:29:49:7e:9f","eth_dst_eth_lg":"0","eth_dst_eth_ig":"0","eth_eth_src":"00:0c:29:75:b2:38","eth_src_eth_src_resolved":"00:0c:29:75:b2:38","eth_src_eth_addr":"00:0c:29:75:b2:38","eth_src_eth_addr_resolved":"00:0c:29:75:b2:38","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800"},"ip":{"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"71","ip_ip_id":"0x000024e9","ip_ip_flags":"0x00004000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"1","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"128","ip_ip_proto":"6","ip_ip_checksum":"0x00000000","ip_ip_checksum_status":"2","ip_ip_src":"192.168.254.134","ip_ip_addr":["192.168.254.134","192.168.254.34"],"ip_ip_src_host":"192.168.254.134","ip_ip_host":["192.168.254.134","192.168.254.34"],"ip_ip_dst":"192.168.254.34","ip_ip_dst_host":"192.168.254.34"},"tcp":{"tcp_tcp_srcport":"1073","tcp_tcp_dstport":"102","tcp_tcp_port":["1073","102"],"tcp_tcp_stream":"1","tcp_tcp_len":"31","tcp_tcp_seq":"1","tcp_tcp_nxtseq":"32","tcp_tcp_ack":"1","tcp_tcp_hdr_len":"20","tcp_tcp_flags":"0x00000018","tcp_flags_tcp_flags_res":"0","tcp_flags_tcp_flags_ns":"0","tcp_flags_tcp_flags_cwr":"0","tcp_flags_tcp_flags_ecn":"0","tcp_flags_tcp_flags_urg":"0","tcp_flags_tcp_flags_ack":"1","tcp_flags_tcp_flags_push":"1","tcp_flags_tcp_flags_reset":"0","tcp_flags_tcp_flags_syn":"0","tcp_flags_tcp_flags_fin":"0","tcp_flags_tcp_flags_str":"Â·Â·Â·Â·Â·Â·Â·APÂ·Â·Â·","tcp_tcp_window_size_value":"64154","tcp_tcp_window_size":"64154","tcp_tcp_window_size_scalefactor":"-1","tcp_tcp_checksum":"0x00007e34","tcp_tcp_checksum_status":"2","tcp_tcp_urgent_pointer":"0","tcp_tcp_analysis":"","tcp_analysis_tcp_analysis_bytes_in_flight":"31","tcp_analysis_tcp_analysis_push_bytes_sent":"31","tcp_text":"Timestamps","text_tcp_time_relative":"0.000000000","text_tcp_time_delta":"0.000000000","tcp_tcp_payload":"03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20"},"tpkt":{"tpkt_tpkt_version":"3","tpkt_tpkt_reserved":"0","tpkt_tpkt_length":"31"},"cotp":{"cotp_cotp_li":"2","cotp_cotp_type":"0x0000000f","cotp_cotp_destref":"0x00010000","cotp_cotp_tpdu-number":"0x00000000","cotp_cotp_eot":"1"},"s7comm":{"s7comm_s7comm_header":"","s7comm_header_s7comm_header_protid":"0x00000032","s7comm_header_s7comm_header_rosctr":"1","s7comm_header_s7comm_header_redid":"0x00000000","s7comm_header_s7comm_header_pduref":"52417","s7comm_header_s7comm_header_parlg":"14","s7comm_header_s7comm_header_datlg":"0","s7comm_s7comm_param":"","s7comm_param_s7comm_param_func":"0x00000004","s7comm_param_s7comm_param_itemcount":"1","s7comm_param_s7comm_param_item":"","s7comm_param_item_s7comm_param_item_varspec":"0x00000012","s7comm_param_item_s7comm_param_item_varspec_length":"10","s7comm_param_item_s7comm_param_item_syntaxid":"0x00000010","s7comm_param_item_s7comm_param_item_transp_size":"2","s7comm_param_item_s7comm_param_item_length":"17","s7comm_param_item_s7comm_param_item_db":"1","s7comm_param_item_s7comm_param_item_area":"0x00000084","s7comm_param_item_s7comm_param_item_address":"0x00000020","s7comm_param_item_address_s7comm_param_item_address_byte":"4","s7comm_param_item_address_s7comm_param_item_address_bit":"0"}}
     */

    private String timestamp;
    private LayersBean layers;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public LayersBean getLayers() {
        return layers;
    }

    public void setLayers(LayersBean layers) {
        this.layers = layers;
    }

    public static class LayersBean {
        /**
         * frame : {"frame_frame_encap_type":"1","frame_frame_time":"Jan  1, 2000 11:00:32.244216000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"946695632.244216000","frame_frame_time_delta":"0.009438000","frame_frame_time_delta_displayed":"0.009438000","frame_frame_time_relative":"0.042628000","frame_frame_number":"5","frame_frame_len":"85","frame_frame_cap_len":"85","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:tcp:tpkt:cotp:s7comm"}
         * eth : {"eth_eth_dst":"00:0c:29:49:7e:9f","eth_dst_eth_dst_resolved":"00:0c:29:49:7e:9f","eth_dst_eth_addr":"00:0c:29:49:7e:9f","eth_dst_eth_addr_resolved":"00:0c:29:49:7e:9f","eth_dst_eth_lg":"0","eth_dst_eth_ig":"0","eth_eth_src":"00:0c:29:75:b2:38","eth_src_eth_src_resolved":"00:0c:29:75:b2:38","eth_src_eth_addr":"00:0c:29:75:b2:38","eth_src_eth_addr_resolved":"00:0c:29:75:b2:38","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800"}
         * ip : {"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"71","ip_ip_id":"0x000024e9","ip_ip_flags":"0x00004000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"1","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"128","ip_ip_proto":"6","ip_ip_checksum":"0x00000000","ip_ip_checksum_status":"2","ip_ip_src":"192.168.254.134","ip_ip_addr":["192.168.254.134","192.168.254.34"],"ip_ip_src_host":"192.168.254.134","ip_ip_host":["192.168.254.134","192.168.254.34"],"ip_ip_dst":"192.168.254.34","ip_ip_dst_host":"192.168.254.34"}
         * tcp : {"tcp_tcp_srcport":"1073","tcp_tcp_dstport":"102","tcp_tcp_port":["1073","102"],"tcp_tcp_stream":"1","tcp_tcp_len":"31","tcp_tcp_seq":"1","tcp_tcp_nxtseq":"32","tcp_tcp_ack":"1","tcp_tcp_hdr_len":"20","tcp_tcp_flags":"0x00000018","tcp_flags_tcp_flags_res":"0","tcp_flags_tcp_flags_ns":"0","tcp_flags_tcp_flags_cwr":"0","tcp_flags_tcp_flags_ecn":"0","tcp_flags_tcp_flags_urg":"0","tcp_flags_tcp_flags_ack":"1","tcp_flags_tcp_flags_push":"1","tcp_flags_tcp_flags_reset":"0","tcp_flags_tcp_flags_syn":"0","tcp_flags_tcp_flags_fin":"0","tcp_flags_tcp_flags_str":"Â·Â·Â·Â·Â·Â·Â·APÂ·Â·Â·","tcp_tcp_window_size_value":"64154","tcp_tcp_window_size":"64154","tcp_tcp_window_size_scalefactor":"-1","tcp_tcp_checksum":"0x00007e34","tcp_tcp_checksum_status":"2","tcp_tcp_urgent_pointer":"0","tcp_tcp_analysis":"","tcp_analysis_tcp_analysis_bytes_in_flight":"31","tcp_analysis_tcp_analysis_push_bytes_sent":"31","tcp_text":"Timestamps","text_tcp_time_relative":"0.000000000","text_tcp_time_delta":"0.000000000","tcp_tcp_payload":"03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20"}
         * tpkt : {"tpkt_tpkt_version":"3","tpkt_tpkt_reserved":"0","tpkt_tpkt_length":"31"}
         * cotp : {"cotp_cotp_li":"2","cotp_cotp_type":"0x0000000f","cotp_cotp_destref":"0x00010000","cotp_cotp_tpdu-number":"0x00000000","cotp_cotp_eot":"1"}
         * s7comm : {"s7comm_s7comm_header":"","s7comm_header_s7comm_header_protid":"0x00000032","s7comm_header_s7comm_header_rosctr":"1","s7comm_header_s7comm_header_redid":"0x00000000","s7comm_header_s7comm_header_pduref":"52417","s7comm_header_s7comm_header_parlg":"14","s7comm_header_s7comm_header_datlg":"0","s7comm_s7comm_param":"","s7comm_param_s7comm_param_func":"0x00000004","s7comm_param_s7comm_param_itemcount":"1","s7comm_param_s7comm_param_item":"","s7comm_param_item_s7comm_param_item_varspec":"0x00000012","s7comm_param_item_s7comm_param_item_varspec_length":"10","s7comm_param_item_s7comm_param_item_syntaxid":"0x00000010","s7comm_param_item_s7comm_param_item_transp_size":"2","s7comm_param_item_s7comm_param_item_length":"17","s7comm_param_item_s7comm_param_item_db":"1","s7comm_param_item_s7comm_param_item_area":"0x00000084","s7comm_param_item_s7comm_param_item_address":"0x00000020","s7comm_param_item_address_s7comm_param_item_address_byte":"4","s7comm_param_item_address_s7comm_param_item_address_bit":"0"}
         */

        private FrameBean frame;
        private EthBean eth;
        private IpBean ip;
        private TcpBean tcp;
        private TpktBean tpkt;
        private CotpBean cotp;
        private S7commBean s7comm;

        public FrameBean getFrame() {
            return frame;
        }

        public void setFrame(FrameBean frame) {
            this.frame = frame;
        }

        public EthBean getEth() {
            return eth;
        }

        public void setEth(EthBean eth) {
            this.eth = eth;
        }

        public IpBean getIp() {
            return ip;
        }

        public void setIp(IpBean ip) {
            this.ip = ip;
        }

        public TcpBean getTcp() {
            return tcp;
        }

        public void setTcp(TcpBean tcp) {
            this.tcp = tcp;
        }

        public TpktBean getTpkt() {
            return tpkt;
        }

        public void setTpkt(TpktBean tpkt) {
            this.tpkt = tpkt;
        }

        public CotpBean getCotp() {
            return cotp;
        }

        public void setCotp(CotpBean cotp) {
            this.cotp = cotp;
        }

        public S7commBean getS7comm() {
            return s7comm;
        }

        public void setS7comm(S7commBean s7comm) {
            this.s7comm = s7comm;
        }

        public static class FrameBean {
            /**
             * frame_frame_encap_type : 1
             * frame_frame_time : Jan  1, 2000 11:00:32.244216000 ä¸­å½æ åæ¶é´
             * frame_frame_offset_shift : 0.000000000
             * frame_frame_time_epoch : 946695632.244216000
             * frame_frame_time_delta : 0.009438000
             * frame_frame_time_delta_displayed : 0.009438000
             * frame_frame_time_relative : 0.042628000
             * frame_frame_number : 5
             * frame_frame_len : 85
             * frame_frame_cap_len : 85
             * frame_frame_marked : 0
             * frame_frame_ignored : 0
             * frame_frame_protocols : eth:ethertype:ip:tcp:tpkt:cotp:s7comm
             */

            private String frame_frame_encap_type;
            private String frame_frame_time;
            private String frame_frame_offset_shift;
            private String frame_frame_time_epoch;
            private String frame_frame_time_delta;
            private String frame_frame_time_delta_displayed;
            private String frame_frame_time_relative;
            private String frame_frame_number;
            private String frame_frame_len;
            private String frame_frame_cap_len;
            private String frame_frame_marked;
            private String frame_frame_ignored;
            private String frame_frame_protocols;

            public String getFrame_frame_encap_type() {
                return frame_frame_encap_type;
            }

            public void setFrame_frame_encap_type(String frame_frame_encap_type) {
                this.frame_frame_encap_type = frame_frame_encap_type;
            }

            public String getFrame_frame_time() {
                return frame_frame_time;
            }

            public void setFrame_frame_time(String frame_frame_time) {
                this.frame_frame_time = frame_frame_time;
            }

            public String getFrame_frame_offset_shift() {
                return frame_frame_offset_shift;
            }

            public void setFrame_frame_offset_shift(String frame_frame_offset_shift) {
                this.frame_frame_offset_shift = frame_frame_offset_shift;
            }

            public String getFrame_frame_time_epoch() {
                return frame_frame_time_epoch;
            }

            public void setFrame_frame_time_epoch(String frame_frame_time_epoch) {
                this.frame_frame_time_epoch = frame_frame_time_epoch;
            }

            public String getFrame_frame_time_delta() {
                return frame_frame_time_delta;
            }

            public void setFrame_frame_time_delta(String frame_frame_time_delta) {
                this.frame_frame_time_delta = frame_frame_time_delta;
            }

            public String getFrame_frame_time_delta_displayed() {
                return frame_frame_time_delta_displayed;
            }

            public void setFrame_frame_time_delta_displayed(String frame_frame_time_delta_displayed) {
                this.frame_frame_time_delta_displayed = frame_frame_time_delta_displayed;
            }

            public String getFrame_frame_time_relative() {
                return frame_frame_time_relative;
            }

            public void setFrame_frame_time_relative(String frame_frame_time_relative) {
                this.frame_frame_time_relative = frame_frame_time_relative;
            }

            public String getFrame_frame_number() {
                return frame_frame_number;
            }

            public void setFrame_frame_number(String frame_frame_number) {
                this.frame_frame_number = frame_frame_number;
            }

            public String getFrame_frame_len() {
                return frame_frame_len;
            }

            public void setFrame_frame_len(String frame_frame_len) {
                this.frame_frame_len = frame_frame_len;
            }

            public String getFrame_frame_cap_len() {
                return frame_frame_cap_len;
            }

            public void setFrame_frame_cap_len(String frame_frame_cap_len) {
                this.frame_frame_cap_len = frame_frame_cap_len;
            }

            public String getFrame_frame_marked() {
                return frame_frame_marked;
            }

            public void setFrame_frame_marked(String frame_frame_marked) {
                this.frame_frame_marked = frame_frame_marked;
            }

            public String getFrame_frame_ignored() {
                return frame_frame_ignored;
            }

            public void setFrame_frame_ignored(String frame_frame_ignored) {
                this.frame_frame_ignored = frame_frame_ignored;
            }

            public String getFrame_frame_protocols() {
                return frame_frame_protocols;
            }

            public void setFrame_frame_protocols(String frame_frame_protocols) {
                this.frame_frame_protocols = frame_frame_protocols;
            }
        }

        public static class EthBean {
            /**
             * eth_eth_dst : 00:0c:29:49:7e:9f
             * eth_dst_eth_dst_resolved : 00:0c:29:49:7e:9f
             * eth_dst_eth_addr : 00:0c:29:49:7e:9f
             * eth_dst_eth_addr_resolved : 00:0c:29:49:7e:9f
             * eth_dst_eth_lg : 0
             * eth_dst_eth_ig : 0
             * eth_eth_src : 00:0c:29:75:b2:38
             * eth_src_eth_src_resolved : 00:0c:29:75:b2:38
             * eth_src_eth_addr : 00:0c:29:75:b2:38
             * eth_src_eth_addr_resolved : 00:0c:29:75:b2:38
             * eth_src_eth_lg : 0
             * eth_src_eth_ig : 0
             * eth_eth_type : 0x00000800
             */

            private String eth_eth_dst;
            private String eth_dst_eth_dst_resolved;
            private String eth_dst_eth_addr;
            private String eth_dst_eth_addr_resolved;
            private String eth_dst_eth_lg;
            private String eth_dst_eth_ig;
            private String eth_eth_src;
            private String eth_src_eth_src_resolved;
            private String eth_src_eth_addr;
            private String eth_src_eth_addr_resolved;
            private String eth_src_eth_lg;
            private String eth_src_eth_ig;
            private String eth_eth_type;

            public String getEth_eth_dst() {
                return eth_eth_dst;
            }

            public void setEth_eth_dst(String eth_eth_dst) {
                this.eth_eth_dst = eth_eth_dst;
            }

            public String getEth_dst_eth_dst_resolved() {
                return eth_dst_eth_dst_resolved;
            }

            public void setEth_dst_eth_dst_resolved(String eth_dst_eth_dst_resolved) {
                this.eth_dst_eth_dst_resolved = eth_dst_eth_dst_resolved;
            }

            public String getEth_dst_eth_addr() {
                return eth_dst_eth_addr;
            }

            public void setEth_dst_eth_addr(String eth_dst_eth_addr) {
                this.eth_dst_eth_addr = eth_dst_eth_addr;
            }

            public String getEth_dst_eth_addr_resolved() {
                return eth_dst_eth_addr_resolved;
            }

            public void setEth_dst_eth_addr_resolved(String eth_dst_eth_addr_resolved) {
                this.eth_dst_eth_addr_resolved = eth_dst_eth_addr_resolved;
            }

            public String getEth_dst_eth_lg() {
                return eth_dst_eth_lg;
            }

            public void setEth_dst_eth_lg(String eth_dst_eth_lg) {
                this.eth_dst_eth_lg = eth_dst_eth_lg;
            }

            public String getEth_dst_eth_ig() {
                return eth_dst_eth_ig;
            }

            public void setEth_dst_eth_ig(String eth_dst_eth_ig) {
                this.eth_dst_eth_ig = eth_dst_eth_ig;
            }

            public String getEth_eth_src() {
                return eth_eth_src;
            }

            public void setEth_eth_src(String eth_eth_src) {
                this.eth_eth_src = eth_eth_src;
            }

            public String getEth_src_eth_src_resolved() {
                return eth_src_eth_src_resolved;
            }

            public void setEth_src_eth_src_resolved(String eth_src_eth_src_resolved) {
                this.eth_src_eth_src_resolved = eth_src_eth_src_resolved;
            }

            public String getEth_src_eth_addr() {
                return eth_src_eth_addr;
            }

            public void setEth_src_eth_addr(String eth_src_eth_addr) {
                this.eth_src_eth_addr = eth_src_eth_addr;
            }

            public String getEth_src_eth_addr_resolved() {
                return eth_src_eth_addr_resolved;
            }

            public void setEth_src_eth_addr_resolved(String eth_src_eth_addr_resolved) {
                this.eth_src_eth_addr_resolved = eth_src_eth_addr_resolved;
            }

            public String getEth_src_eth_lg() {
                return eth_src_eth_lg;
            }

            public void setEth_src_eth_lg(String eth_src_eth_lg) {
                this.eth_src_eth_lg = eth_src_eth_lg;
            }

            public String getEth_src_eth_ig() {
                return eth_src_eth_ig;
            }

            public void setEth_src_eth_ig(String eth_src_eth_ig) {
                this.eth_src_eth_ig = eth_src_eth_ig;
            }

            public String getEth_eth_type() {
                return eth_eth_type;
            }

            public void setEth_eth_type(String eth_eth_type) {
                this.eth_eth_type = eth_eth_type;
            }
        }

        public static class IpBean {
            /**
             * ip_ip_version : 4
             * ip_ip_hdr_len : 20
             * ip_ip_dsfield : 0x00000000
             * ip_dsfield_ip_dsfield_dscp : 0
             * ip_dsfield_ip_dsfield_ecn : 0
             * ip_ip_len : 71
             * ip_ip_id : 0x000024e9
             * ip_ip_flags : 0x00004000
             * ip_flags_ip_flags_rb : 0
             * ip_flags_ip_flags_df : 1
             * ip_flags_ip_flags_mf : 0
             * ip_flags_ip_frag_offset : 0
             * ip_ip_ttl : 128
             * ip_ip_proto : 6
             * ip_ip_checksum : 0x00000000
             * ip_ip_checksum_status : 2
             * ip_ip_src : 192.168.254.134
             * ip_ip_addr : ["192.168.254.134","192.168.254.34"]
             * ip_ip_src_host : 192.168.254.134
             * ip_ip_host : ["192.168.254.134","192.168.254.34"]
             * ip_ip_dst : 192.168.254.34
             * ip_ip_dst_host : 192.168.254.34
             */

            private String ip_ip_version;
            private String ip_ip_hdr_len;
            private String ip_ip_dsfield;
            private String ip_dsfield_ip_dsfield_dscp;
            private String ip_dsfield_ip_dsfield_ecn;
            private String ip_ip_len;
            private String ip_ip_id;
            private String ip_ip_flags;
            private String ip_flags_ip_flags_rb;
            private String ip_flags_ip_flags_df;
            private String ip_flags_ip_flags_mf;
            private String ip_flags_ip_frag_offset;
            private String ip_ip_ttl;
            private String ip_ip_proto;
            private String ip_ip_checksum;
            private String ip_ip_checksum_status;
            private String ip_ip_src;
            private String ip_ip_src_host;
            private String ip_ip_dst;
            private String ip_ip_dst_host;
            private List<String> ip_ip_addr;
            private List<String> ip_ip_host;

            public String getIp_ip_version() {
                return ip_ip_version;
            }

            public void setIp_ip_version(String ip_ip_version) {
                this.ip_ip_version = ip_ip_version;
            }

            public String getIp_ip_hdr_len() {
                return ip_ip_hdr_len;
            }

            public void setIp_ip_hdr_len(String ip_ip_hdr_len) {
                this.ip_ip_hdr_len = ip_ip_hdr_len;
            }

            public String getIp_ip_dsfield() {
                return ip_ip_dsfield;
            }

            public void setIp_ip_dsfield(String ip_ip_dsfield) {
                this.ip_ip_dsfield = ip_ip_dsfield;
            }

            public String getIp_dsfield_ip_dsfield_dscp() {
                return ip_dsfield_ip_dsfield_dscp;
            }

            public void setIp_dsfield_ip_dsfield_dscp(String ip_dsfield_ip_dsfield_dscp) {
                this.ip_dsfield_ip_dsfield_dscp = ip_dsfield_ip_dsfield_dscp;
            }

            public String getIp_dsfield_ip_dsfield_ecn() {
                return ip_dsfield_ip_dsfield_ecn;
            }

            public void setIp_dsfield_ip_dsfield_ecn(String ip_dsfield_ip_dsfield_ecn) {
                this.ip_dsfield_ip_dsfield_ecn = ip_dsfield_ip_dsfield_ecn;
            }

            public String getIp_ip_len() {
                return ip_ip_len;
            }

            public void setIp_ip_len(String ip_ip_len) {
                this.ip_ip_len = ip_ip_len;
            }

            public String getIp_ip_id() {
                return ip_ip_id;
            }

            public void setIp_ip_id(String ip_ip_id) {
                this.ip_ip_id = ip_ip_id;
            }

            public String getIp_ip_flags() {
                return ip_ip_flags;
            }

            public void setIp_ip_flags(String ip_ip_flags) {
                this.ip_ip_flags = ip_ip_flags;
            }

            public String getIp_flags_ip_flags_rb() {
                return ip_flags_ip_flags_rb;
            }

            public void setIp_flags_ip_flags_rb(String ip_flags_ip_flags_rb) {
                this.ip_flags_ip_flags_rb = ip_flags_ip_flags_rb;
            }

            public String getIp_flags_ip_flags_df() {
                return ip_flags_ip_flags_df;
            }

            public void setIp_flags_ip_flags_df(String ip_flags_ip_flags_df) {
                this.ip_flags_ip_flags_df = ip_flags_ip_flags_df;
            }

            public String getIp_flags_ip_flags_mf() {
                return ip_flags_ip_flags_mf;
            }

            public void setIp_flags_ip_flags_mf(String ip_flags_ip_flags_mf) {
                this.ip_flags_ip_flags_mf = ip_flags_ip_flags_mf;
            }

            public String getIp_flags_ip_frag_offset() {
                return ip_flags_ip_frag_offset;
            }

            public void setIp_flags_ip_frag_offset(String ip_flags_ip_frag_offset) {
                this.ip_flags_ip_frag_offset = ip_flags_ip_frag_offset;
            }

            public String getIp_ip_ttl() {
                return ip_ip_ttl;
            }

            public void setIp_ip_ttl(String ip_ip_ttl) {
                this.ip_ip_ttl = ip_ip_ttl;
            }

            public String getIp_ip_proto() {
                return ip_ip_proto;
            }

            public void setIp_ip_proto(String ip_ip_proto) {
                this.ip_ip_proto = ip_ip_proto;
            }

            public String getIp_ip_checksum() {
                return ip_ip_checksum;
            }

            public void setIp_ip_checksum(String ip_ip_checksum) {
                this.ip_ip_checksum = ip_ip_checksum;
            }

            public String getIp_ip_checksum_status() {
                return ip_ip_checksum_status;
            }

            public void setIp_ip_checksum_status(String ip_ip_checksum_status) {
                this.ip_ip_checksum_status = ip_ip_checksum_status;
            }

            public String getIp_ip_src() {
                return ip_ip_src;
            }

            public void setIp_ip_src(String ip_ip_src) {
                this.ip_ip_src = ip_ip_src;
            }

            public String getIp_ip_src_host() {
                return ip_ip_src_host;
            }

            public void setIp_ip_src_host(String ip_ip_src_host) {
                this.ip_ip_src_host = ip_ip_src_host;
            }

            public String getIp_ip_dst() {
                return ip_ip_dst;
            }

            public void setIp_ip_dst(String ip_ip_dst) {
                this.ip_ip_dst = ip_ip_dst;
            }

            public String getIp_ip_dst_host() {
                return ip_ip_dst_host;
            }

            public void setIp_ip_dst_host(String ip_ip_dst_host) {
                this.ip_ip_dst_host = ip_ip_dst_host;
            }

            public List<String> getIp_ip_addr() {
                return ip_ip_addr;
            }

            public void setIp_ip_addr(List<String> ip_ip_addr) {
                this.ip_ip_addr = ip_ip_addr;
            }

            public List<String> getIp_ip_host() {
                return ip_ip_host;
            }

            public void setIp_ip_host(List<String> ip_ip_host) {
                this.ip_ip_host = ip_ip_host;
            }
        }

        public static class TcpBean {
            /**
             * tcp_tcp_srcport : 1073
             * tcp_tcp_dstport : 102
             * tcp_tcp_port : ["1073","102"]
             * tcp_tcp_stream : 1
             * tcp_tcp_len : 31
             * tcp_tcp_seq : 1
             * tcp_tcp_nxtseq : 32
             * tcp_tcp_ack : 1
             * tcp_tcp_hdr_len : 20
             * tcp_tcp_flags : 0x00000018
             * tcp_flags_tcp_flags_res : 0
             * tcp_flags_tcp_flags_ns : 0
             * tcp_flags_tcp_flags_cwr : 0
             * tcp_flags_tcp_flags_ecn : 0
             * tcp_flags_tcp_flags_urg : 0
             * tcp_flags_tcp_flags_ack : 1
             * tcp_flags_tcp_flags_push : 1
             * tcp_flags_tcp_flags_reset : 0
             * tcp_flags_tcp_flags_syn : 0
             * tcp_flags_tcp_flags_fin : 0
             * tcp_flags_tcp_flags_str : Â·Â·Â·Â·Â·Â·Â·APÂ·Â·Â·
             * tcp_tcp_window_size_value : 64154
             * tcp_tcp_window_size : 64154
             * tcp_tcp_window_size_scalefactor : -1
             * tcp_tcp_checksum : 0x00007e34
             * tcp_tcp_checksum_status : 2
             * tcp_tcp_urgent_pointer : 0
             * tcp_tcp_analysis :
             * tcp_analysis_tcp_analysis_bytes_in_flight : 31
             * tcp_analysis_tcp_analysis_push_bytes_sent : 31
             * tcp_text : Timestamps
             * text_tcp_time_relative : 0.000000000
             * text_tcp_time_delta : 0.000000000
             * tcp_tcp_payload : 03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20
             */

            private String tcp_tcp_srcport;
            private String tcp_tcp_dstport;
            private String tcp_tcp_stream;
            private String tcp_tcp_len;
            private String tcp_tcp_seq;
            private String tcp_tcp_nxtseq;
            private String tcp_tcp_ack;
            private String tcp_tcp_hdr_len;
            private String tcp_tcp_flags;
            private String tcp_flags_tcp_flags_res;
            private String tcp_flags_tcp_flags_ns;
            private String tcp_flags_tcp_flags_cwr;
            private String tcp_flags_tcp_flags_ecn;
            private String tcp_flags_tcp_flags_urg;
            private String tcp_flags_tcp_flags_ack;
            private String tcp_flags_tcp_flags_push;
            private String tcp_flags_tcp_flags_reset;
            private String tcp_flags_tcp_flags_syn;
            private String tcp_flags_tcp_flags_fin;
            private String tcp_flags_tcp_flags_str;
            private String tcp_tcp_window_size_value;
            private String tcp_tcp_window_size;
            private String tcp_tcp_window_size_scalefactor;
            private String tcp_tcp_checksum;
            private String tcp_tcp_checksum_status;
            private String tcp_tcp_urgent_pointer;
            private String tcp_tcp_analysis;
            private String tcp_analysis_tcp_analysis_bytes_in_flight;
            private String tcp_analysis_tcp_analysis_push_bytes_sent;
            private String tcp_text;
            private String text_tcp_time_relative;
            private String text_tcp_time_delta;
            private String tcp_tcp_payload;
            private List<String> tcp_tcp_port;

            public String getTcp_tcp_srcport() {
                return tcp_tcp_srcport;
            }

            public void setTcp_tcp_srcport(String tcp_tcp_srcport) {
                this.tcp_tcp_srcport = tcp_tcp_srcport;
            }

            public String getTcp_tcp_dstport() {
                return tcp_tcp_dstport;
            }

            public void setTcp_tcp_dstport(String tcp_tcp_dstport) {
                this.tcp_tcp_dstport = tcp_tcp_dstport;
            }

            public String getTcp_tcp_stream() {
                return tcp_tcp_stream;
            }

            public void setTcp_tcp_stream(String tcp_tcp_stream) {
                this.tcp_tcp_stream = tcp_tcp_stream;
            }

            public String getTcp_tcp_len() {
                return tcp_tcp_len;
            }

            public void setTcp_tcp_len(String tcp_tcp_len) {
                this.tcp_tcp_len = tcp_tcp_len;
            }

            public String getTcp_tcp_seq() {
                return tcp_tcp_seq;
            }

            public void setTcp_tcp_seq(String tcp_tcp_seq) {
                this.tcp_tcp_seq = tcp_tcp_seq;
            }

            public String getTcp_tcp_nxtseq() {
                return tcp_tcp_nxtseq;
            }

            public void setTcp_tcp_nxtseq(String tcp_tcp_nxtseq) {
                this.tcp_tcp_nxtseq = tcp_tcp_nxtseq;
            }

            public String getTcp_tcp_ack() {
                return tcp_tcp_ack;
            }

            public void setTcp_tcp_ack(String tcp_tcp_ack) {
                this.tcp_tcp_ack = tcp_tcp_ack;
            }

            public String getTcp_tcp_hdr_len() {
                return tcp_tcp_hdr_len;
            }

            public void setTcp_tcp_hdr_len(String tcp_tcp_hdr_len) {
                this.tcp_tcp_hdr_len = tcp_tcp_hdr_len;
            }

            public String getTcp_tcp_flags() {
                return tcp_tcp_flags;
            }

            public void setTcp_tcp_flags(String tcp_tcp_flags) {
                this.tcp_tcp_flags = tcp_tcp_flags;
            }

            public String getTcp_flags_tcp_flags_res() {
                return tcp_flags_tcp_flags_res;
            }

            public void setTcp_flags_tcp_flags_res(String tcp_flags_tcp_flags_res) {
                this.tcp_flags_tcp_flags_res = tcp_flags_tcp_flags_res;
            }

            public String getTcp_flags_tcp_flags_ns() {
                return tcp_flags_tcp_flags_ns;
            }

            public void setTcp_flags_tcp_flags_ns(String tcp_flags_tcp_flags_ns) {
                this.tcp_flags_tcp_flags_ns = tcp_flags_tcp_flags_ns;
            }

            public String getTcp_flags_tcp_flags_cwr() {
                return tcp_flags_tcp_flags_cwr;
            }

            public void setTcp_flags_tcp_flags_cwr(String tcp_flags_tcp_flags_cwr) {
                this.tcp_flags_tcp_flags_cwr = tcp_flags_tcp_flags_cwr;
            }

            public String getTcp_flags_tcp_flags_ecn() {
                return tcp_flags_tcp_flags_ecn;
            }

            public void setTcp_flags_tcp_flags_ecn(String tcp_flags_tcp_flags_ecn) {
                this.tcp_flags_tcp_flags_ecn = tcp_flags_tcp_flags_ecn;
            }

            public String getTcp_flags_tcp_flags_urg() {
                return tcp_flags_tcp_flags_urg;
            }

            public void setTcp_flags_tcp_flags_urg(String tcp_flags_tcp_flags_urg) {
                this.tcp_flags_tcp_flags_urg = tcp_flags_tcp_flags_urg;
            }

            public String getTcp_flags_tcp_flags_ack() {
                return tcp_flags_tcp_flags_ack;
            }

            public void setTcp_flags_tcp_flags_ack(String tcp_flags_tcp_flags_ack) {
                this.tcp_flags_tcp_flags_ack = tcp_flags_tcp_flags_ack;
            }

            public String getTcp_flags_tcp_flags_push() {
                return tcp_flags_tcp_flags_push;
            }

            public void setTcp_flags_tcp_flags_push(String tcp_flags_tcp_flags_push) {
                this.tcp_flags_tcp_flags_push = tcp_flags_tcp_flags_push;
            }

            public String getTcp_flags_tcp_flags_reset() {
                return tcp_flags_tcp_flags_reset;
            }

            public void setTcp_flags_tcp_flags_reset(String tcp_flags_tcp_flags_reset) {
                this.tcp_flags_tcp_flags_reset = tcp_flags_tcp_flags_reset;
            }

            public String getTcp_flags_tcp_flags_syn() {
                return tcp_flags_tcp_flags_syn;
            }

            public void setTcp_flags_tcp_flags_syn(String tcp_flags_tcp_flags_syn) {
                this.tcp_flags_tcp_flags_syn = tcp_flags_tcp_flags_syn;
            }

            public String getTcp_flags_tcp_flags_fin() {
                return tcp_flags_tcp_flags_fin;
            }

            public void setTcp_flags_tcp_flags_fin(String tcp_flags_tcp_flags_fin) {
                this.tcp_flags_tcp_flags_fin = tcp_flags_tcp_flags_fin;
            }

            public String getTcp_flags_tcp_flags_str() {
                return tcp_flags_tcp_flags_str;
            }

            public void setTcp_flags_tcp_flags_str(String tcp_flags_tcp_flags_str) {
                this.tcp_flags_tcp_flags_str = tcp_flags_tcp_flags_str;
            }

            public String getTcp_tcp_window_size_value() {
                return tcp_tcp_window_size_value;
            }

            public void setTcp_tcp_window_size_value(String tcp_tcp_window_size_value) {
                this.tcp_tcp_window_size_value = tcp_tcp_window_size_value;
            }

            public String getTcp_tcp_window_size() {
                return tcp_tcp_window_size;
            }

            public void setTcp_tcp_window_size(String tcp_tcp_window_size) {
                this.tcp_tcp_window_size = tcp_tcp_window_size;
            }

            public String getTcp_tcp_window_size_scalefactor() {
                return tcp_tcp_window_size_scalefactor;
            }

            public void setTcp_tcp_window_size_scalefactor(String tcp_tcp_window_size_scalefactor) {
                this.tcp_tcp_window_size_scalefactor = tcp_tcp_window_size_scalefactor;
            }

            public String getTcp_tcp_checksum() {
                return tcp_tcp_checksum;
            }

            public void setTcp_tcp_checksum(String tcp_tcp_checksum) {
                this.tcp_tcp_checksum = tcp_tcp_checksum;
            }

            public String getTcp_tcp_checksum_status() {
                return tcp_tcp_checksum_status;
            }

            public void setTcp_tcp_checksum_status(String tcp_tcp_checksum_status) {
                this.tcp_tcp_checksum_status = tcp_tcp_checksum_status;
            }

            public String getTcp_tcp_urgent_pointer() {
                return tcp_tcp_urgent_pointer;
            }

            public void setTcp_tcp_urgent_pointer(String tcp_tcp_urgent_pointer) {
                this.tcp_tcp_urgent_pointer = tcp_tcp_urgent_pointer;
            }

            public String getTcp_tcp_analysis() {
                return tcp_tcp_analysis;
            }

            public void setTcp_tcp_analysis(String tcp_tcp_analysis) {
                this.tcp_tcp_analysis = tcp_tcp_analysis;
            }

            public String getTcp_analysis_tcp_analysis_bytes_in_flight() {
                return tcp_analysis_tcp_analysis_bytes_in_flight;
            }

            public void setTcp_analysis_tcp_analysis_bytes_in_flight(String tcp_analysis_tcp_analysis_bytes_in_flight) {
                this.tcp_analysis_tcp_analysis_bytes_in_flight = tcp_analysis_tcp_analysis_bytes_in_flight;
            }

            public String getTcp_analysis_tcp_analysis_push_bytes_sent() {
                return tcp_analysis_tcp_analysis_push_bytes_sent;
            }

            public void setTcp_analysis_tcp_analysis_push_bytes_sent(String tcp_analysis_tcp_analysis_push_bytes_sent) {
                this.tcp_analysis_tcp_analysis_push_bytes_sent = tcp_analysis_tcp_analysis_push_bytes_sent;
            }

            public String getTcp_text() {
                return tcp_text;
            }

            public void setTcp_text(String tcp_text) {
                this.tcp_text = tcp_text;
            }

            public String getText_tcp_time_relative() {
                return text_tcp_time_relative;
            }

            public void setText_tcp_time_relative(String text_tcp_time_relative) {
                this.text_tcp_time_relative = text_tcp_time_relative;
            }

            public String getText_tcp_time_delta() {
                return text_tcp_time_delta;
            }

            public void setText_tcp_time_delta(String text_tcp_time_delta) {
                this.text_tcp_time_delta = text_tcp_time_delta;
            }

            public String getTcp_tcp_payload() {
                return tcp_tcp_payload;
            }

            public void setTcp_tcp_payload(String tcp_tcp_payload) {
                this.tcp_tcp_payload = tcp_tcp_payload;
            }

            public List<String> getTcp_tcp_port() {
                return tcp_tcp_port;
            }

            public void setTcp_tcp_port(List<String> tcp_tcp_port) {
                this.tcp_tcp_port = tcp_tcp_port;
            }
        }

        public static class TpktBean {
            /**
             * tpkt_tpkt_version : 3
             * tpkt_tpkt_reserved : 0
             * tpkt_tpkt_length : 31
             */

            private String tpkt_tpkt_version;
            private String tpkt_tpkt_reserved;
            private String tpkt_tpkt_length;

            public String getTpkt_tpkt_version() {
                return tpkt_tpkt_version;
            }

            public void setTpkt_tpkt_version(String tpkt_tpkt_version) {
                this.tpkt_tpkt_version = tpkt_tpkt_version;
            }

            public String getTpkt_tpkt_reserved() {
                return tpkt_tpkt_reserved;
            }

            public void setTpkt_tpkt_reserved(String tpkt_tpkt_reserved) {
                this.tpkt_tpkt_reserved = tpkt_tpkt_reserved;
            }

            public String getTpkt_tpkt_length() {
                return tpkt_tpkt_length;
            }

            public void setTpkt_tpkt_length(String tpkt_tpkt_length) {
                this.tpkt_tpkt_length = tpkt_tpkt_length;
            }
        }

        public static class CotpBean {
            /**
             * cotp_cotp_li : 2
             * cotp_cotp_type : 0x0000000f
             * cotp_cotp_destref : 0x00010000
             * cotp_cotp_tpdu-number : 0x00000000
             * cotp_cotp_eot : 1
             */

            private String cotp_cotp_li;
            private String cotp_cotp_type;
            private String cotp_cotp_destref;
            @JSONField(name = "cotp_cotp_tpdu-number")
            private String cotp_cotp_tpdunumber;
            private String cotp_cotp_eot;

            public String getCotp_cotp_li() {
                return cotp_cotp_li;
            }

            public void setCotp_cotp_li(String cotp_cotp_li) {
                this.cotp_cotp_li = cotp_cotp_li;
            }

            public String getCotp_cotp_type() {
                return cotp_cotp_type;
            }

            public void setCotp_cotp_type(String cotp_cotp_type) {
                this.cotp_cotp_type = cotp_cotp_type;
            }

            public String getCotp_cotp_destref() {
                return cotp_cotp_destref;
            }

            public void setCotp_cotp_destref(String cotp_cotp_destref) {
                this.cotp_cotp_destref = cotp_cotp_destref;
            }

            public String getCotp_cotp_tpdunumber() {
                return cotp_cotp_tpdunumber;
            }

            public void setCotp_cotp_tpdunumber(String cotp_cotp_tpdunumber) {
                this.cotp_cotp_tpdunumber = cotp_cotp_tpdunumber;
            }

            public String getCotp_cotp_eot() {
                return cotp_cotp_eot;
            }

            public void setCotp_cotp_eot(String cotp_cotp_eot) {
                this.cotp_cotp_eot = cotp_cotp_eot;
            }
        }

        public static class S7commBean {
            /**
             * s7comm_s7comm_header :
             * s7comm_header_s7comm_header_protid : 0x00000032
             * s7comm_header_s7comm_header_rosctr : 1
             * s7comm_header_s7comm_header_redid : 0x00000000
             * s7comm_header_s7comm_header_pduref : 52417
             * s7comm_header_s7comm_header_parlg : 14
             * s7comm_header_s7comm_header_datlg : 0
             * s7comm_s7comm_param :
             * s7comm_param_s7comm_param_func : 0x00000004
             * s7comm_param_s7comm_param_itemcount : 1
             * s7comm_param_s7comm_param_item :
             * s7comm_param_item_s7comm_param_item_varspec : 0x00000012
             * s7comm_param_item_s7comm_param_item_varspec_length : 10
             * s7comm_param_item_s7comm_param_item_syntaxid : 0x00000010
             * s7comm_param_item_s7comm_param_item_transp_size : 2
             * s7comm_param_item_s7comm_param_item_length : 17
             * s7comm_param_item_s7comm_param_item_db : 1
             * s7comm_param_item_s7comm_param_item_area : 0x00000084
             * s7comm_param_item_s7comm_param_item_address : 0x00000020
             * s7comm_param_item_address_s7comm_param_item_address_byte : 4
             * s7comm_param_item_address_s7comm_param_item_address_bit : 0
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
            private String s7comm_param_s7comm_param_itemcount;
            private String s7comm_param_s7comm_param_item;
            private String s7comm_param_item_s7comm_param_item_varspec;
            private String s7comm_param_item_s7comm_param_item_varspec_length;
            private String s7comm_param_item_s7comm_param_item_syntaxid;
            private String s7comm_param_item_s7comm_param_item_transp_size;
            private String s7comm_param_item_s7comm_param_item_length;
            private String s7comm_param_item_s7comm_param_item_db;
            private String s7comm_param_item_s7comm_param_item_area;
            private String s7comm_param_item_s7comm_param_item_address;
            private String s7comm_param_item_address_s7comm_param_item_address_byte;
            private String s7comm_param_item_address_s7comm_param_item_address_bit;

            public String getS7comm_s7comm_header() {
                return s7comm_s7comm_header;
            }

            public void setS7comm_s7comm_header(String s7comm_s7comm_header) {
                this.s7comm_s7comm_header = s7comm_s7comm_header;
            }

            public String getS7comm_header_s7comm_header_protid() {
                return s7comm_header_s7comm_header_protid;
            }

            public void setS7comm_header_s7comm_header_protid(String s7comm_header_s7comm_header_protid) {
                this.s7comm_header_s7comm_header_protid = s7comm_header_s7comm_header_protid;
            }

            public String getS7comm_header_s7comm_header_rosctr() {
                return s7comm_header_s7comm_header_rosctr;
            }

            public void setS7comm_header_s7comm_header_rosctr(String s7comm_header_s7comm_header_rosctr) {
                this.s7comm_header_s7comm_header_rosctr = s7comm_header_s7comm_header_rosctr;
            }

            public String getS7comm_header_s7comm_header_redid() {
                return s7comm_header_s7comm_header_redid;
            }

            public void setS7comm_header_s7comm_header_redid(String s7comm_header_s7comm_header_redid) {
                this.s7comm_header_s7comm_header_redid = s7comm_header_s7comm_header_redid;
            }

            public String getS7comm_header_s7comm_header_pduref() {
                return s7comm_header_s7comm_header_pduref;
            }

            public void setS7comm_header_s7comm_header_pduref(String s7comm_header_s7comm_header_pduref) {
                this.s7comm_header_s7comm_header_pduref = s7comm_header_s7comm_header_pduref;
            }

            public String getS7comm_header_s7comm_header_parlg() {
                return s7comm_header_s7comm_header_parlg;
            }

            public void setS7comm_header_s7comm_header_parlg(String s7comm_header_s7comm_header_parlg) {
                this.s7comm_header_s7comm_header_parlg = s7comm_header_s7comm_header_parlg;
            }

            public String getS7comm_header_s7comm_header_datlg() {
                return s7comm_header_s7comm_header_datlg;
            }

            public void setS7comm_header_s7comm_header_datlg(String s7comm_header_s7comm_header_datlg) {
                this.s7comm_header_s7comm_header_datlg = s7comm_header_s7comm_header_datlg;
            }

            public String getS7comm_s7comm_param() {
                return s7comm_s7comm_param;
            }

            public void setS7comm_s7comm_param(String s7comm_s7comm_param) {
                this.s7comm_s7comm_param = s7comm_s7comm_param;
            }

            public String getS7comm_param_s7comm_param_func() {
                return s7comm_param_s7comm_param_func;
            }

            public void setS7comm_param_s7comm_param_func(String s7comm_param_s7comm_param_func) {
                this.s7comm_param_s7comm_param_func = s7comm_param_s7comm_param_func;
            }

            public String getS7comm_param_s7comm_param_itemcount() {
                return s7comm_param_s7comm_param_itemcount;
            }

            public void setS7comm_param_s7comm_param_itemcount(String s7comm_param_s7comm_param_itemcount) {
                this.s7comm_param_s7comm_param_itemcount = s7comm_param_s7comm_param_itemcount;
            }

            public String getS7comm_param_s7comm_param_item() {
                return s7comm_param_s7comm_param_item;
            }

            public void setS7comm_param_s7comm_param_item(String s7comm_param_s7comm_param_item) {
                this.s7comm_param_s7comm_param_item = s7comm_param_s7comm_param_item;
            }

            public String getS7comm_param_item_s7comm_param_item_varspec() {
                return s7comm_param_item_s7comm_param_item_varspec;
            }

            public void setS7comm_param_item_s7comm_param_item_varspec(String s7comm_param_item_s7comm_param_item_varspec) {
                this.s7comm_param_item_s7comm_param_item_varspec = s7comm_param_item_s7comm_param_item_varspec;
            }

            public String getS7comm_param_item_s7comm_param_item_varspec_length() {
                return s7comm_param_item_s7comm_param_item_varspec_length;
            }

            public void setS7comm_param_item_s7comm_param_item_varspec_length(String s7comm_param_item_s7comm_param_item_varspec_length) {
                this.s7comm_param_item_s7comm_param_item_varspec_length = s7comm_param_item_s7comm_param_item_varspec_length;
            }

            public String getS7comm_param_item_s7comm_param_item_syntaxid() {
                return s7comm_param_item_s7comm_param_item_syntaxid;
            }

            public void setS7comm_param_item_s7comm_param_item_syntaxid(String s7comm_param_item_s7comm_param_item_syntaxid) {
                this.s7comm_param_item_s7comm_param_item_syntaxid = s7comm_param_item_s7comm_param_item_syntaxid;
            }

            public String getS7comm_param_item_s7comm_param_item_transp_size() {
                return s7comm_param_item_s7comm_param_item_transp_size;
            }

            public void setS7comm_param_item_s7comm_param_item_transp_size(String s7comm_param_item_s7comm_param_item_transp_size) {
                this.s7comm_param_item_s7comm_param_item_transp_size = s7comm_param_item_s7comm_param_item_transp_size;
            }

            public String getS7comm_param_item_s7comm_param_item_length() {
                return s7comm_param_item_s7comm_param_item_length;
            }

            public void setS7comm_param_item_s7comm_param_item_length(String s7comm_param_item_s7comm_param_item_length) {
                this.s7comm_param_item_s7comm_param_item_length = s7comm_param_item_s7comm_param_item_length;
            }

            public String getS7comm_param_item_s7comm_param_item_db() {
                return s7comm_param_item_s7comm_param_item_db;
            }

            public void setS7comm_param_item_s7comm_param_item_db(String s7comm_param_item_s7comm_param_item_db) {
                this.s7comm_param_item_s7comm_param_item_db = s7comm_param_item_s7comm_param_item_db;
            }

            public String getS7comm_param_item_s7comm_param_item_area() {
                return s7comm_param_item_s7comm_param_item_area;
            }

            public void setS7comm_param_item_s7comm_param_item_area(String s7comm_param_item_s7comm_param_item_area) {
                this.s7comm_param_item_s7comm_param_item_area = s7comm_param_item_s7comm_param_item_area;
            }

            public String getS7comm_param_item_s7comm_param_item_address() {
                return s7comm_param_item_s7comm_param_item_address;
            }

            public void setS7comm_param_item_s7comm_param_item_address(String s7comm_param_item_s7comm_param_item_address) {
                this.s7comm_param_item_s7comm_param_item_address = s7comm_param_item_s7comm_param_item_address;
            }

            public String getS7comm_param_item_address_s7comm_param_item_address_byte() {
                return s7comm_param_item_address_s7comm_param_item_address_byte;
            }

            public void setS7comm_param_item_address_s7comm_param_item_address_byte(String s7comm_param_item_address_s7comm_param_item_address_byte) {
                this.s7comm_param_item_address_s7comm_param_item_address_byte = s7comm_param_item_address_s7comm_param_item_address_byte;
            }

            public String getS7comm_param_item_address_s7comm_param_item_address_bit() {
                return s7comm_param_item_address_s7comm_param_item_address_bit;
            }

            public void setS7comm_param_item_address_s7comm_param_item_address_bit(String s7comm_param_item_address_s7comm_param_item_address_bit) {
                this.s7comm_param_item_address_s7comm_param_item_address_bit = s7comm_param_item_address_s7comm_param_item_address_bit;
            }
        }
    }
}
