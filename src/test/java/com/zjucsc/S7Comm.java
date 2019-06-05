package com.zjucsc;

import com.alibaba.fastjson.annotation.JSONField;

public class S7Comm {


    /**
     * timestamp : 1559635500401
     * layers : {"frame":{"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{1309BFBB-2749-44A5-8082-9F8D7677070E}","frame_frame_encap_type":"1","frame_frame_time":"Jun  4, 2019 16:05:00.401387000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1559635500.401387000","frame_frame_time_delta":"0.017770000","frame_frame_time_delta_displayed":"0.018946000","frame_frame_time_relative":"1.474149000","frame_frame_number":"17","frame_frame_len":"100","frame_frame_cap_len":"100","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:llc:osi:clnp:cotp:s7comm"},"eth":{"eth_eth_dst":"44:87:fc:77:36:9b","eth_dst_eth_dst_resolved":"Elitegro_77:36:9b","eth_dst_eth_addr":"44:87:fc:77:36:9b","eth_dst_eth_addr_resolved":"Elitegro_77:36:9b","eth_dst_eth_lg":"0","eth_dst_eth_ig":"0","eth_eth_src":"00:1b:1b:47:ba:83","eth_src_eth_src_resolved":"Siemens_47:ba:83","eth_src_eth_addr":"00:1b:1b:47:ba:83","eth_src_eth_addr_resolved":"Siemens_47:ba:83","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_len":"86"},"llc":{"llc_llc_dsap":"0x000000fe","llc_dsap_llc_dsap_sap":"127","llc_dsap_llc_dsap_ig":"0","llc_llc_ssap":"0x000000fe","llc_ssap_llc_ssap_sap":"127","llc_ssap_llc_ssap_cr":"0","llc_llc_control":"0x00000003","llc_control_llc_control_u_modifier_cmd":"0x00000000","llc_control_llc_control_ftype":"0x00000003"},"clnp":{"clnp_clnp_nlpi":"0x00000000"},"cotp":{"cotp_cotp_li":"7","cotp_cotp_type":"0x0000000f","cotp_cotp_destref":"0x000000b8","cotp_cotp_tpdu-number":"0x00000001","cotp_cotp_eot":"1"},"s7comm":{"s7comm_s7comm_header":"","s7comm_header_s7comm_header_protid":"0x00000032","s7comm_header_s7comm_header_rosctr":"7","s7comm_header_s7comm_header_redid":"0x00000000","s7comm_header_s7comm_header_pduref":"768","s7comm_header_s7comm_header_parlg":"12","s7comm_header_s7comm_header_datlg":"52","s7comm_s7comm_param":"","s7comm_param_s7comm_param_userdata_head":"0x00000112","s7comm_param_s7comm_param_userdata_length":"8","s7comm_param_s7comm_param_userdata_reqres1":"0x00000012","s7comm_param_s7comm_param_userdata_type":"8","s7comm_param_s7comm_param_userdata_funcgroup":"4","s7comm_param_s7comm_param_userdata_subfunc":"1","s7comm_param_s7comm_param_userdata_seq_num":"4","s7comm_param_s7comm_param_userdata_dataunitref":"0","s7comm_param_s7comm_param_userdata_lastdataunit":"0x00000000","s7comm_param_s7comm_param_errcod":"0","s7comm_s7comm_data":"","s7comm_data_s7comm_data_returncode":"0x000000ff","s7comm_data_s7comm_data_transportsize":"0x00000009","s7comm_data_s7comm_data_length":"48","s7comm_data_s7comm_data_userdata_szl_id":"0x00000132","s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type":"0x00000000","s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex":"306","s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num":"50","s7comm_data_s7comm_data_userdata_szl_index":"0x00000004","s7comm_data_s7comm_data_userdata_szl_id_partlist_len":"40","s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt":"1","s7comm_data_s7comm_data_userdata_szl_data_tree":"","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index":"0x00000004","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key":"1","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param":"0","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real":"1","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch":"2","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst":"0","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res":"00:00:56:56:76:01:76:07:66:91:06:19:02:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00"}}
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
         * frame : {"frame_frame_interface_id":"0","frame_interface_id_frame_interface_name":"\\Device\\NPF_{1309BFBB-2749-44A5-8082-9F8D7677070E}","frame_frame_encap_type":"1","frame_frame_time":"Jun  4, 2019 16:05:00.401387000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"1559635500.401387000","frame_frame_time_delta":"0.017770000","frame_frame_time_delta_displayed":"0.018946000","frame_frame_time_relative":"1.474149000","frame_frame_number":"17","frame_frame_len":"100","frame_frame_cap_len":"100","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:llc:osi:clnp:cotp:s7comm"}
         * eth : {"eth_eth_dst":"44:87:fc:77:36:9b","eth_dst_eth_dst_resolved":"Elitegro_77:36:9b","eth_dst_eth_addr":"44:87:fc:77:36:9b","eth_dst_eth_addr_resolved":"Elitegro_77:36:9b","eth_dst_eth_lg":"0","eth_dst_eth_ig":"0","eth_eth_src":"00:1b:1b:47:ba:83","eth_src_eth_src_resolved":"Siemens_47:ba:83","eth_src_eth_addr":"00:1b:1b:47:ba:83","eth_src_eth_addr_resolved":"Siemens_47:ba:83","eth_src_eth_lg":"0","eth_src_eth_ig":"0","eth_eth_len":"86"}
         * llc : {"llc_llc_dsap":"0x000000fe","llc_dsap_llc_dsap_sap":"127","llc_dsap_llc_dsap_ig":"0","llc_llc_ssap":"0x000000fe","llc_ssap_llc_ssap_sap":"127","llc_ssap_llc_ssap_cr":"0","llc_llc_control":"0x00000003","llc_control_llc_control_u_modifier_cmd":"0x00000000","llc_control_llc_control_ftype":"0x00000003"}
         * clnp : {"clnp_clnp_nlpi":"0x00000000"}
         * cotp : {"cotp_cotp_li":"7","cotp_cotp_type":"0x0000000f","cotp_cotp_destref":"0x000000b8","cotp_cotp_tpdu-number":"0x00000001","cotp_cotp_eot":"1"}
         * s7comm : {"s7comm_s7comm_header":"","s7comm_header_s7comm_header_protid":"0x00000032","s7comm_header_s7comm_header_rosctr":"7","s7comm_header_s7comm_header_redid":"0x00000000","s7comm_header_s7comm_header_pduref":"768","s7comm_header_s7comm_header_parlg":"12","s7comm_header_s7comm_header_datlg":"52","s7comm_s7comm_param":"","s7comm_param_s7comm_param_userdata_head":"0x00000112","s7comm_param_s7comm_param_userdata_length":"8","s7comm_param_s7comm_param_userdata_reqres1":"0x00000012","s7comm_param_s7comm_param_userdata_type":"8","s7comm_param_s7comm_param_userdata_funcgroup":"4","s7comm_param_s7comm_param_userdata_subfunc":"1","s7comm_param_s7comm_param_userdata_seq_num":"4","s7comm_param_s7comm_param_userdata_dataunitref":"0","s7comm_param_s7comm_param_userdata_lastdataunit":"0x00000000","s7comm_param_s7comm_param_errcod":"0","s7comm_s7comm_data":"","s7comm_data_s7comm_data_returncode":"0x000000ff","s7comm_data_s7comm_data_transportsize":"0x00000009","s7comm_data_s7comm_data_length":"48","s7comm_data_s7comm_data_userdata_szl_id":"0x00000132","s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type":"0x00000000","s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex":"306","s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num":"50","s7comm_data_s7comm_data_userdata_szl_index":"0x00000004","s7comm_data_s7comm_data_userdata_szl_id_partlist_len":"40","s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt":"1","s7comm_data_s7comm_data_userdata_szl_data_tree":"","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index":"0x00000004","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key":"1","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param":"0","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real":"1","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch":"2","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst":"0","s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res":"00:00:56:56:76:01:76:07:66:91:06:19:02:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00"}
         */

        private FrameBean frame;
        private EthBean eth;
        private LlcBean llc;
        private ClnpBean clnp;
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

        public LlcBean getLlc() {
            return llc;
        }

        public void setLlc(LlcBean llc) {
            this.llc = llc;
        }

        public ClnpBean getClnp() {
            return clnp;
        }

        public void setClnp(ClnpBean clnp) {
            this.clnp = clnp;
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
             * frame_frame_interface_id : 0
             * frame_interface_id_frame_interface_name : \Device\NPF_{1309BFBB-2749-44A5-8082-9F8D7677070E}
             * frame_frame_encap_type : 1
             * frame_frame_time : Jun  4, 2019 16:05:00.401387000 ä¸­å½æ åæ¶é´
             * frame_frame_offset_shift : 0.000000000
             * frame_frame_time_epoch : 1559635500.401387000
             * frame_frame_time_delta : 0.017770000
             * frame_frame_time_delta_displayed : 0.018946000
             * frame_frame_time_relative : 1.474149000
             * frame_frame_number : 17
             * frame_frame_len : 100
             * frame_frame_cap_len : 100
             * frame_frame_marked : 0
             * frame_frame_ignored : 0
             * frame_frame_protocols : eth:llc:osi:clnp:cotp:s7comm
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
             * eth_eth_dst : 44:87:fc:77:36:9b
             * eth_dst_eth_dst_resolved : Elitegro_77:36:9b
             * eth_dst_eth_addr : 44:87:fc:77:36:9b
             * eth_dst_eth_addr_resolved : Elitegro_77:36:9b
             * eth_dst_eth_lg : 0
             * eth_dst_eth_ig : 0
             * eth_eth_src : 00:1b:1b:47:ba:83
             * eth_src_eth_src_resolved : Siemens_47:ba:83
             * eth_src_eth_addr : 00:1b:1b:47:ba:83
             * eth_src_eth_addr_resolved : Siemens_47:ba:83
             * eth_src_eth_lg : 0
             * eth_src_eth_ig : 0
             * eth_eth_len : 86
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
            private String eth_eth_len;

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

            public String getEth_eth_len() {
                return eth_eth_len;
            }

            public void setEth_eth_len(String eth_eth_len) {
                this.eth_eth_len = eth_eth_len;
            }
        }

        public static class LlcBean {
            /**
             * llc_llc_dsap : 0x000000fe
             * llc_dsap_llc_dsap_sap : 127
             * llc_dsap_llc_dsap_ig : 0
             * llc_llc_ssap : 0x000000fe
             * llc_ssap_llc_ssap_sap : 127
             * llc_ssap_llc_ssap_cr : 0
             * llc_llc_control : 0x00000003
             * llc_control_llc_control_u_modifier_cmd : 0x00000000
             * llc_control_llc_control_ftype : 0x00000003
             */

            private String llc_llc_dsap;
            private String llc_dsap_llc_dsap_sap;
            private String llc_dsap_llc_dsap_ig;
            private String llc_llc_ssap;
            private String llc_ssap_llc_ssap_sap;
            private String llc_ssap_llc_ssap_cr;
            private String llc_llc_control;
            private String llc_control_llc_control_u_modifier_cmd;
            private String llc_control_llc_control_ftype;

            public String getLlc_llc_dsap() {
                return llc_llc_dsap;
            }

            public void setLlc_llc_dsap(String llc_llc_dsap) {
                this.llc_llc_dsap = llc_llc_dsap;
            }

            public String getLlc_dsap_llc_dsap_sap() {
                return llc_dsap_llc_dsap_sap;
            }

            public void setLlc_dsap_llc_dsap_sap(String llc_dsap_llc_dsap_sap) {
                this.llc_dsap_llc_dsap_sap = llc_dsap_llc_dsap_sap;
            }

            public String getLlc_dsap_llc_dsap_ig() {
                return llc_dsap_llc_dsap_ig;
            }

            public void setLlc_dsap_llc_dsap_ig(String llc_dsap_llc_dsap_ig) {
                this.llc_dsap_llc_dsap_ig = llc_dsap_llc_dsap_ig;
            }

            public String getLlc_llc_ssap() {
                return llc_llc_ssap;
            }

            public void setLlc_llc_ssap(String llc_llc_ssap) {
                this.llc_llc_ssap = llc_llc_ssap;
            }

            public String getLlc_ssap_llc_ssap_sap() {
                return llc_ssap_llc_ssap_sap;
            }

            public void setLlc_ssap_llc_ssap_sap(String llc_ssap_llc_ssap_sap) {
                this.llc_ssap_llc_ssap_sap = llc_ssap_llc_ssap_sap;
            }

            public String getLlc_ssap_llc_ssap_cr() {
                return llc_ssap_llc_ssap_cr;
            }

            public void setLlc_ssap_llc_ssap_cr(String llc_ssap_llc_ssap_cr) {
                this.llc_ssap_llc_ssap_cr = llc_ssap_llc_ssap_cr;
            }

            public String getLlc_llc_control() {
                return llc_llc_control;
            }

            public void setLlc_llc_control(String llc_llc_control) {
                this.llc_llc_control = llc_llc_control;
            }

            public String getLlc_control_llc_control_u_modifier_cmd() {
                return llc_control_llc_control_u_modifier_cmd;
            }

            public void setLlc_control_llc_control_u_modifier_cmd(String llc_control_llc_control_u_modifier_cmd) {
                this.llc_control_llc_control_u_modifier_cmd = llc_control_llc_control_u_modifier_cmd;
            }

            public String getLlc_control_llc_control_ftype() {
                return llc_control_llc_control_ftype;
            }

            public void setLlc_control_llc_control_ftype(String llc_control_llc_control_ftype) {
                this.llc_control_llc_control_ftype = llc_control_llc_control_ftype;
            }
        }

        public static class ClnpBean {
            /**
             * clnp_clnp_nlpi : 0x00000000
             */

            private String clnp_clnp_nlpi;

            public String getClnp_clnp_nlpi() {
                return clnp_clnp_nlpi;
            }

            public void setClnp_clnp_nlpi(String clnp_clnp_nlpi) {
                this.clnp_clnp_nlpi = clnp_clnp_nlpi;
            }
        }

        public static class CotpBean {
            /**
             * cotp_cotp_li : 7
             * cotp_cotp_type : 0x0000000f
             * cotp_cotp_destref : 0x000000b8
             * cotp_cotp_tpdu-number : 0x00000001
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
             * s7comm_header_s7comm_header_rosctr : 7
             * s7comm_header_s7comm_header_redid : 0x00000000
             * s7comm_header_s7comm_header_pduref : 768
             * s7comm_header_s7comm_header_parlg : 12
             * s7comm_header_s7comm_header_datlg : 52
             * s7comm_s7comm_param :
             * s7comm_param_s7comm_param_userdata_head : 0x00000112
             * s7comm_param_s7comm_param_userdata_length : 8
             * s7comm_param_s7comm_param_userdata_reqres1 : 0x00000012
             * s7comm_param_s7comm_param_userdata_type : 8
             * s7comm_param_s7comm_param_userdata_funcgroup : 4
             * s7comm_param_s7comm_param_userdata_subfunc : 1
             * s7comm_param_s7comm_param_userdata_seq_num : 4
             * s7comm_param_s7comm_param_userdata_dataunitref : 0
             * s7comm_param_s7comm_param_userdata_lastdataunit : 0x00000000
             * s7comm_param_s7comm_param_errcod : 0
             * s7comm_s7comm_data :
             * s7comm_data_s7comm_data_returncode : 0x000000ff
             * s7comm_data_s7comm_data_transportsize : 0x00000009
             * s7comm_data_s7comm_data_length : 48
             * s7comm_data_s7comm_data_userdata_szl_id : 0x00000132
             * s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type : 0x00000000
             * s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex : 306
             * s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num : 50
             * s7comm_data_s7comm_data_userdata_szl_index : 0x00000004
             * s7comm_data_s7comm_data_userdata_szl_id_partlist_len : 40
             * s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt : 1
             * s7comm_data_s7comm_data_userdata_szl_data_tree :
             * s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index : 0x00000004
             * s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key : 1
             * s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param : 0
             * s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real : 1
             * s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch : 2
             * s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst : 0
             * s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res : 00:00:56:56:76:01:76:07:66:91:06:19:02:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00
             */

            private String s7comm_s7comm_header;
            private String s7comm_header_s7comm_header_protid;
            private String s7comm_header_s7comm_header_rosctr;
            private String s7comm_header_s7comm_header_redid;
            private String s7comm_header_s7comm_header_pduref;
            private String s7comm_header_s7comm_header_parlg;
            private String s7comm_header_s7comm_header_datlg;
            private String s7comm_s7comm_param;
            private String s7comm_param_s7comm_param_userdata_head;
            private String s7comm_param_s7comm_param_userdata_length;
            private String s7comm_param_s7comm_param_userdata_reqres1;
            private String s7comm_param_s7comm_param_userdata_type;
            private String s7comm_param_s7comm_param_userdata_funcgroup;
            private String s7comm_param_s7comm_param_userdata_subfunc;
            private String s7comm_param_s7comm_param_userdata_seq_num;
            private String s7comm_param_s7comm_param_userdata_dataunitref;
            private String s7comm_param_s7comm_param_userdata_lastdataunit;
            private String s7comm_param_s7comm_param_errcod;
            private String s7comm_s7comm_data;
            private String s7comm_data_s7comm_data_returncode;
            private String s7comm_data_s7comm_data_transportsize;
            private String s7comm_data_s7comm_data_length;
            private String s7comm_data_s7comm_data_userdata_szl_id;
            private String s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type;
            private String s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex;
            private String s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num;
            private String s7comm_data_s7comm_data_userdata_szl_index;
            private String s7comm_data_s7comm_data_userdata_szl_id_partlist_len;
            private String s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt;
            private String s7comm_data_s7comm_data_userdata_szl_data_tree;
            private String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index;
            private String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key;
            private String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param;
            private String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real;
            private String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch;
            private String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst;
            private String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res;

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

            public String getS7comm_param_s7comm_param_userdata_head() {
                return s7comm_param_s7comm_param_userdata_head;
            }

            public void setS7comm_param_s7comm_param_userdata_head(String s7comm_param_s7comm_param_userdata_head) {
                this.s7comm_param_s7comm_param_userdata_head = s7comm_param_s7comm_param_userdata_head;
            }

            public String getS7comm_param_s7comm_param_userdata_length() {
                return s7comm_param_s7comm_param_userdata_length;
            }

            public void setS7comm_param_s7comm_param_userdata_length(String s7comm_param_s7comm_param_userdata_length) {
                this.s7comm_param_s7comm_param_userdata_length = s7comm_param_s7comm_param_userdata_length;
            }

            public String getS7comm_param_s7comm_param_userdata_reqres1() {
                return s7comm_param_s7comm_param_userdata_reqres1;
            }

            public void setS7comm_param_s7comm_param_userdata_reqres1(String s7comm_param_s7comm_param_userdata_reqres1) {
                this.s7comm_param_s7comm_param_userdata_reqres1 = s7comm_param_s7comm_param_userdata_reqres1;
            }

            public String getS7comm_param_s7comm_param_userdata_type() {
                return s7comm_param_s7comm_param_userdata_type;
            }

            public void setS7comm_param_s7comm_param_userdata_type(String s7comm_param_s7comm_param_userdata_type) {
                this.s7comm_param_s7comm_param_userdata_type = s7comm_param_s7comm_param_userdata_type;
            }

            public String getS7comm_param_s7comm_param_userdata_funcgroup() {
                return s7comm_param_s7comm_param_userdata_funcgroup;
            }

            public void setS7comm_param_s7comm_param_userdata_funcgroup(String s7comm_param_s7comm_param_userdata_funcgroup) {
                this.s7comm_param_s7comm_param_userdata_funcgroup = s7comm_param_s7comm_param_userdata_funcgroup;
            }

            public String getS7comm_param_s7comm_param_userdata_subfunc() {
                return s7comm_param_s7comm_param_userdata_subfunc;
            }

            public void setS7comm_param_s7comm_param_userdata_subfunc(String s7comm_param_s7comm_param_userdata_subfunc) {
                this.s7comm_param_s7comm_param_userdata_subfunc = s7comm_param_s7comm_param_userdata_subfunc;
            }

            public String getS7comm_param_s7comm_param_userdata_seq_num() {
                return s7comm_param_s7comm_param_userdata_seq_num;
            }

            public void setS7comm_param_s7comm_param_userdata_seq_num(String s7comm_param_s7comm_param_userdata_seq_num) {
                this.s7comm_param_s7comm_param_userdata_seq_num = s7comm_param_s7comm_param_userdata_seq_num;
            }

            public String getS7comm_param_s7comm_param_userdata_dataunitref() {
                return s7comm_param_s7comm_param_userdata_dataunitref;
            }

            public void setS7comm_param_s7comm_param_userdata_dataunitref(String s7comm_param_s7comm_param_userdata_dataunitref) {
                this.s7comm_param_s7comm_param_userdata_dataunitref = s7comm_param_s7comm_param_userdata_dataunitref;
            }

            public String getS7comm_param_s7comm_param_userdata_lastdataunit() {
                return s7comm_param_s7comm_param_userdata_lastdataunit;
            }

            public void setS7comm_param_s7comm_param_userdata_lastdataunit(String s7comm_param_s7comm_param_userdata_lastdataunit) {
                this.s7comm_param_s7comm_param_userdata_lastdataunit = s7comm_param_s7comm_param_userdata_lastdataunit;
            }

            public String getS7comm_param_s7comm_param_errcod() {
                return s7comm_param_s7comm_param_errcod;
            }

            public void setS7comm_param_s7comm_param_errcod(String s7comm_param_s7comm_param_errcod) {
                this.s7comm_param_s7comm_param_errcod = s7comm_param_s7comm_param_errcod;
            }

            public String getS7comm_s7comm_data() {
                return s7comm_s7comm_data;
            }

            public void setS7comm_s7comm_data(String s7comm_s7comm_data) {
                this.s7comm_s7comm_data = s7comm_s7comm_data;
            }

            public String getS7comm_data_s7comm_data_returncode() {
                return s7comm_data_s7comm_data_returncode;
            }

            public void setS7comm_data_s7comm_data_returncode(String s7comm_data_s7comm_data_returncode) {
                this.s7comm_data_s7comm_data_returncode = s7comm_data_s7comm_data_returncode;
            }

            public String getS7comm_data_s7comm_data_transportsize() {
                return s7comm_data_s7comm_data_transportsize;
            }

            public void setS7comm_data_s7comm_data_transportsize(String s7comm_data_s7comm_data_transportsize) {
                this.s7comm_data_s7comm_data_transportsize = s7comm_data_s7comm_data_transportsize;
            }

            public String getS7comm_data_s7comm_data_length() {
                return s7comm_data_s7comm_data_length;
            }

            public void setS7comm_data_s7comm_data_length(String s7comm_data_s7comm_data_length) {
                this.s7comm_data_s7comm_data_length = s7comm_data_s7comm_data_length;
            }

            public String getS7comm_data_s7comm_data_userdata_szl_id() {
                return s7comm_data_s7comm_data_userdata_szl_id;
            }

            public void setS7comm_data_s7comm_data_userdata_szl_id(String s7comm_data_s7comm_data_userdata_szl_id) {
                this.s7comm_data_s7comm_data_userdata_szl_id = s7comm_data_s7comm_data_userdata_szl_id;
            }

            public String getS7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type() {
                return s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type;
            }

            public void setS7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type(String s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type) {
                this.s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type = s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_diag_type;
            }

            public String getS7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex() {
                return s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex;
            }

            public void setS7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex(String s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex) {
                this.s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex = s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_ex;
            }

            public String getS7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num() {
                return s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num;
            }

            public void setS7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num(String s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num) {
                this.s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num = s7comm_data_userdata_szl_id_s7comm_data_userdata_szl_id_partlist_num;
            }

            public String getS7comm_data_s7comm_data_userdata_szl_index() {
                return s7comm_data_s7comm_data_userdata_szl_index;
            }

            public void setS7comm_data_s7comm_data_userdata_szl_index(String s7comm_data_s7comm_data_userdata_szl_index) {
                this.s7comm_data_s7comm_data_userdata_szl_index = s7comm_data_s7comm_data_userdata_szl_index;
            }

            public String getS7comm_data_s7comm_data_userdata_szl_id_partlist_len() {
                return s7comm_data_s7comm_data_userdata_szl_id_partlist_len;
            }

            public void setS7comm_data_s7comm_data_userdata_szl_id_partlist_len(String s7comm_data_s7comm_data_userdata_szl_id_partlist_len) {
                this.s7comm_data_s7comm_data_userdata_szl_id_partlist_len = s7comm_data_s7comm_data_userdata_szl_id_partlist_len;
            }

            public String getS7comm_data_s7comm_data_userdata_szl_id_partlist_cnt() {
                return s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt;
            }

            public void setS7comm_data_s7comm_data_userdata_szl_id_partlist_cnt(String s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt) {
                this.s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt = s7comm_data_s7comm_data_userdata_szl_id_partlist_cnt;
            }

            public String getS7comm_data_s7comm_data_userdata_szl_data_tree() {
                return s7comm_data_s7comm_data_userdata_szl_data_tree;
            }

            public void setS7comm_data_s7comm_data_userdata_szl_data_tree(String s7comm_data_s7comm_data_userdata_szl_data_tree) {
                this.s7comm_data_s7comm_data_userdata_szl_data_tree = s7comm_data_s7comm_data_userdata_szl_data_tree;
            }

            public String getS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index() {
                return s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index;
            }

            public void setS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index(String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index) {
                this.s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index = s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_index;
            }

            public String getS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key() {
                return s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key;
            }

            public void setS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key(String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key) {
                this.s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key = s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_key;
            }

            public String getS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param() {
                return s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param;
            }

            public void setS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param(String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param) {
                this.s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param = s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_param;
            }

            public String getS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real() {
                return s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real;
            }

            public void setS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real(String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real) {
                this.s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real = s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_real;
            }

            public String getS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch() {
                return s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch;
            }

            public void setS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch(String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch) {
                this.s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch = s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_bart_sch;
            }

            public String getS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst() {
                return s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst;
            }

            public void setS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst(String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst) {
                this.s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst = s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_crst_wrst;
            }

            public String getS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res() {
                return s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res;
            }

            public void setS7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res(String s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res) {
                this.s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res = s7comm_data_userdata_szl_data_tree_s7comm_szl_0132_0004_res;
            }
        }
    }
}
