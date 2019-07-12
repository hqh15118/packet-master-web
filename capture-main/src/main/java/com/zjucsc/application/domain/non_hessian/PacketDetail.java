package com.zjucsc.application.domain.non_hessian;

public class PacketDetail {


    /**
     * timestamp : 1562903142047
     * layers : {"frame":{"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{06E3803C-6CBD-4AD5-829B-58220E549DE0}","frame_frame_encap_type":"1","frame_frame_time":"Jul 12, 2019 11:45:42.047541000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1562903142.047541000","frame_frame_time_delta":"42.129189000","frame_frame_time_delta_displayed":"0.000000000","frame_frame_time_relative":"45.131804000","frame_frame_number":"5","frame_frame_len":"33","frame_frame_cap_len":"33","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip"},"eth":{"eth_eth_dst":"ff:ff:ff:ff:ff:ff","eth_dst_eth_dst_resolved":"Broadcast","eth_dst_eth_addr":"ff:ff:ff:ff:ff:ff","eth_dst_eth_addr_resolved":"Broadcast","eth_dst_eth_lg":"1","eth_dst_eth_ig":"1","eth_eth_src":"f4:96:34:a4:05:42","eth_src_eth_src_resolved":"IntelCor_a4:05:42","eth_src_eth_addr":"f4:96:34:a4:05:42","eth_src_eth_addr_resolved":"IntelCor_a4:05:42","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800"},"ip":{"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"291","ip_len__ws_expert":{"_ws_expert_ip_bogus_ip_length":"","_ws_expert__ws_expert_message":"IPv4 total length exceeds packet length (19 bytes)","_ws_expert__ws_expert_severity":"8388608","_ws_expert__ws_expert_group":"150994944"},"ip_ip_id":"0x0000443b","ip_ip_flags":"0x00000000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"0","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"128","ip_ip_proto":"17","ip_ip_checksum":"0x00000814","ip_ip_checksum_status":"2","ip_ip_src":"171.118.128.224","ip_ip_addr":"171.118.128.224","ip_ip_src_host":"171.118.128.224","ip_ip_host":"171.118.128.224"},"_ws_malformed":{"_ws_malformed__ws_expert":{"_ws_expert__ws_malformed_expert":"","_ws_expert__ws_expert_message":"Malformed Packet (Exception occurred)","_ws_expert__ws_expert_severity":"8388608","_ws_expert__ws_expert_group":"117440512"},"_ws_malformed__ws_malformed":{}},"_ws_lua_fake":"","ext":{"ext_custom_ext_raw_data":"FFFFFFFFFFFFF49634A40542080045000123443B000080110814AB7680E0AB76FF"}}
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
         * frame : {"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{06E3803C-6CBD-4AD5-829B-58220E549DE0}","frame_frame_encap_type":"1","frame_frame_time":"Jul 12, 2019 11:45:42.047541000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1562903142.047541000","frame_frame_time_delta":"42.129189000","frame_frame_time_delta_displayed":"0.000000000","frame_frame_time_relative":"45.131804000","frame_frame_number":"5","frame_frame_len":"33","frame_frame_cap_len":"33","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip"}
         * eth : {"eth_eth_dst":"ff:ff:ff:ff:ff:ff","eth_dst_eth_dst_resolved":"Broadcast","eth_dst_eth_addr":"ff:ff:ff:ff:ff:ff","eth_dst_eth_addr_resolved":"Broadcast","eth_dst_eth_lg":"1","eth_dst_eth_ig":"1","eth_eth_src":"f4:96:34:a4:05:42","eth_src_eth_src_resolved":"IntelCor_a4:05:42","eth_src_eth_addr":"f4:96:34:a4:05:42","eth_src_eth_addr_resolved":"IntelCor_a4:05:42","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_type":"0x00000800"}
         * ip : {"ip_ip_version":"4","ip_ip_hdr_len":"20","ip_ip_dsfield":"0x00000000","ip_dsfield_ip_dsfield_dscp":"0","ip_dsfield_ip_dsfield_ecn":"0","ip_ip_len":"291","ip_len__ws_expert":{"_ws_expert_ip_bogus_ip_length":"","_ws_expert__ws_expert_message":"IPv4 total length exceeds packet length (19 bytes)","_ws_expert__ws_expert_severity":"8388608","_ws_expert__ws_expert_group":"150994944"},"ip_ip_id":"0x0000443b","ip_ip_flags":"0x00000000","ip_flags_ip_flags_rb":"0","ip_flags_ip_flags_df":"0","ip_flags_ip_flags_mf":"0","ip_flags_ip_frag_offset":"0","ip_ip_ttl":"128","ip_ip_proto":"17","ip_ip_checksum":"0x00000814","ip_ip_checksum_status":"2","ip_ip_src":"171.118.128.224","ip_ip_addr":"171.118.128.224","ip_ip_src_host":"171.118.128.224","ip_ip_host":"171.118.128.224"}
         * _ws_malformed : {"_ws_malformed__ws_expert":{"_ws_expert__ws_malformed_expert":"","_ws_expert__ws_expert_message":"Malformed Packet (Exception occurred)","_ws_expert__ws_expert_severity":"8388608","_ws_expert__ws_expert_group":"117440512"},"_ws_malformed__ws_malformed":{}}
         * _ws_lua_fake :
         * ext : {"ext_custom_ext_raw_data":"FFFFFFFFFFFFF49634A40542080045000123443B000080110814AB7680E0AB76FF"}
         */

//        private FrameBean frame;
//        private EthBean eth;
//        private IpBean ip;
//        private WsMalformedBean _ws_malformed;
//        private String _ws_lua_fake;
        private ExtBean ext;

//        public FrameBean getFrame() {
//            return frame;
//        }
//
//        public void setFrame(FrameBean frame) {
//            this.frame = frame;
//        }
//
//        public EthBean getEth() {
//            return eth;
//        }
//
//        public void setEth(EthBean eth) {
//            this.eth = eth;
//        }
//
//        public IpBean getIp() {
//            return ip;
//        }
//
//        public void setIp(IpBean ip) {
//            this.ip = ip;
//        }
//
//        public WsMalformedBean get_ws_malformed() {
//            return _ws_malformed;
//        }
//
//        public void set_ws_malformed(WsMalformedBean _ws_malformed) {
//            this._ws_malformed = _ws_malformed;
//        }
//
//        public String get_ws_lua_fake() {
//            return _ws_lua_fake;
//        }
//
//        public void set_ws_lua_fake(String _ws_lua_fake) {
//            this._ws_lua_fake = _ws_lua_fake;
//        }

        public ExtBean getExt() {
            return ext;
        }

        public void setExt(ExtBean ext) {
            this.ext = ext;
        }


        public static class FrameBean {
            /**
             * frame_frame_interface_id : 0
             * frame_interface_id_frame_interface_name : \Device\NPF_{06E3803C-6CBD-4AD5-829B-58220E549DE0}
             * frame_frame_encap_type : 1
             * frame_frame_time : Jul 12, 2019 11:45:42.047541000 ä¸­å½æ åæ¶é´
             * frame_frame_offset_shift : 0.000000000
             * frame_frame_time_epoch : 1562903142.047541000
             * frame_frame_time_delta : 42.129189000
             * frame_frame_time_delta_displayed : 0.000000000
             * frame_frame_time_relative : 45.131804000
             * frame_frame_number : 5
             * frame_frame_len : 33
             * frame_frame_cap_len : 33
             * frame_frame_marked : 0
             * frame_frame_ignored : 0
             * frame_frame_protocols : eth:ethertype:ip
             */

            private String frame_frame_interface_id;
            private String frame_interface_id_frame_interface_name;
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

            public String getFrame_frame_interface_id() {
                return frame_frame_interface_id;
            }

            public void setFrame_frame_interface_id(String frame_frame_interface_id) {
                this.frame_frame_interface_id = frame_frame_interface_id;
            }

            public String getFrame_interface_id_frame_interface_name() {
                return frame_interface_id_frame_interface_name;
            }

            public void setFrame_interface_id_frame_interface_name(String frame_interface_id_frame_interface_name) {
                this.frame_interface_id_frame_interface_name = frame_interface_id_frame_interface_name;
            }

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
             * eth_eth_dst : ff:ff:ff:ff:ff:ff
             * eth_dst_eth_dst_resolved : Broadcast
             * eth_dst_eth_addr : ff:ff:ff:ff:ff:ff
             * eth_dst_eth_addr_resolved : Broadcast
             * eth_dst_eth_lg : 1
             * eth_dst_eth_ig : 1
             * eth_eth_src : f4:96:34:a4:05:42
             * eth_src_eth_src_resolved : IntelCor_a4:05:42
             * eth_src_eth_addr : f4:96:34:a4:05:42
             * eth_src_eth_addr_resolved : IntelCor_a4:05:42
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
             * ip_ip_len : 291
             * ip_len__ws_expert : {"_ws_expert_ip_bogus_ip_length":"","_ws_expert__ws_expert_message":"IPv4 total length exceeds packet length (19 bytes)","_ws_expert__ws_expert_severity":"8388608","_ws_expert__ws_expert_group":"150994944"}
             * ip_ip_id : 0x0000443b
             * ip_ip_flags : 0x00000000
             * ip_flags_ip_flags_rb : 0
             * ip_flags_ip_flags_df : 0
             * ip_flags_ip_flags_mf : 0
             * ip_flags_ip_frag_offset : 0
             * ip_ip_ttl : 128
             * ip_ip_proto : 17
             * ip_ip_checksum : 0x00000814
             * ip_ip_checksum_status : 2
             * ip_ip_src : 171.118.128.224
             * ip_ip_addr : 171.118.128.224
             * ip_ip_src_host : 171.118.128.224
             * ip_ip_host : 171.118.128.224
             */

            private String ip_ip_version;
            private String ip_ip_hdr_len;
            private String ip_ip_dsfield;
            private String ip_dsfield_ip_dsfield_dscp;
            private String ip_dsfield_ip_dsfield_ecn;
            private String ip_ip_len;
            private IpLenWsExpertBean ip_len__ws_expert;
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
            private String ip_ip_addr;
            private String ip_ip_src_host;
            private String ip_ip_host;

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

            public IpLenWsExpertBean getIp_len__ws_expert() {
                return ip_len__ws_expert;
            }

            public void setIp_len__ws_expert(IpLenWsExpertBean ip_len__ws_expert) {
                this.ip_len__ws_expert = ip_len__ws_expert;
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

            public String getIp_ip_addr() {
                return ip_ip_addr;
            }

            public void setIp_ip_addr(String ip_ip_addr) {
                this.ip_ip_addr = ip_ip_addr;
            }

            public String getIp_ip_src_host() {
                return ip_ip_src_host;
            }

            public void setIp_ip_src_host(String ip_ip_src_host) {
                this.ip_ip_src_host = ip_ip_src_host;
            }

            public String getIp_ip_host() {
                return ip_ip_host;
            }

            public void setIp_ip_host(String ip_ip_host) {
                this.ip_ip_host = ip_ip_host;
            }

            public static class IpLenWsExpertBean {
                /**
                 * _ws_expert_ip_bogus_ip_length :
                 * _ws_expert__ws_expert_message : IPv4 total length exceeds packet length (19 bytes)
                 * _ws_expert__ws_expert_severity : 8388608
                 * _ws_expert__ws_expert_group : 150994944
                 */

                private String _ws_expert_ip_bogus_ip_length;
                private String _ws_expert__ws_expert_message;
                private String _ws_expert__ws_expert_severity;
                private String _ws_expert__ws_expert_group;

                public String get_ws_expert_ip_bogus_ip_length() {
                    return _ws_expert_ip_bogus_ip_length;
                }

                public void set_ws_expert_ip_bogus_ip_length(String _ws_expert_ip_bogus_ip_length) {
                    this._ws_expert_ip_bogus_ip_length = _ws_expert_ip_bogus_ip_length;
                }

                public String get_ws_expert__ws_expert_message() {
                    return _ws_expert__ws_expert_message;
                }

                public void set_ws_expert__ws_expert_message(String _ws_expert__ws_expert_message) {
                    this._ws_expert__ws_expert_message = _ws_expert__ws_expert_message;
                }

                public String get_ws_expert__ws_expert_severity() {
                    return _ws_expert__ws_expert_severity;
                }

                public void set_ws_expert__ws_expert_severity(String _ws_expert__ws_expert_severity) {
                    this._ws_expert__ws_expert_severity = _ws_expert__ws_expert_severity;
                }

                public String get_ws_expert__ws_expert_group() {
                    return _ws_expert__ws_expert_group;
                }

                public void set_ws_expert__ws_expert_group(String _ws_expert__ws_expert_group) {
                    this._ws_expert__ws_expert_group = _ws_expert__ws_expert_group;
                }
            }
        }

        public static class WsMalformedBean {
            /**
             * _ws_malformed__ws_expert : {"_ws_expert__ws_malformed_expert":"","_ws_expert__ws_expert_message":"Malformed Packet (Exception occurred)","_ws_expert__ws_expert_severity":"8388608","_ws_expert__ws_expert_group":"117440512"}
             * _ws_malformed__ws_malformed : {}
             */

            private WsMalformedWsExpertBean _ws_malformed__ws_expert;
            private WsMalformedWsMalformedBean _ws_malformed__ws_malformed;

            public WsMalformedWsExpertBean get_ws_malformed__ws_expert() {
                return _ws_malformed__ws_expert;
            }

            public void set_ws_malformed__ws_expert(WsMalformedWsExpertBean _ws_malformed__ws_expert) {
                this._ws_malformed__ws_expert = _ws_malformed__ws_expert;
            }

            public WsMalformedWsMalformedBean get_ws_malformed__ws_malformed() {
                return _ws_malformed__ws_malformed;
            }

            public void set_ws_malformed__ws_malformed(WsMalformedWsMalformedBean _ws_malformed__ws_malformed) {
                this._ws_malformed__ws_malformed = _ws_malformed__ws_malformed;
            }

            public static class WsMalformedWsExpertBean {
            }

            public static class WsMalformedWsMalformedBean {
            }
        }

        public static class ExtBean {
            /**
             * ext_custom_ext_raw_data : FFFFFFFFFFFFF49634A40542080045000123443B000080110814AB7680E0AB76FF
             */

            private String ext_custom_ext_raw_data;

            public String getExt_custom_ext_raw_data() {
                return ext_custom_ext_raw_data;
            }

            public void setExt_custom_ext_raw_data(String ext_custom_ext_raw_data) {
                this.ext_custom_ext_raw_data = ext_custom_ext_raw_data;
            }
        }
    }
}
