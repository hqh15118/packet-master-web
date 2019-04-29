package com.zjucsc.application.tshark;

public class BasePacket {

    /**
     * timestamp : 946695632244
     * layers : {"frame":{"frame_frame_encap_type":"1","frame_frame_time":"Jan  1, 2000 11:00:32.244216000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"946695632.244216000","frame_frame_time_delta":"0.009438000","frame_frame_time_delta_displayed":"0.009438000","frame_frame_time_relative":"0.042628000","frame_frame_number":"5","frame_frame_len":"85","frame_frame_cap_len":"85","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:tcp:tpkt:cotp:s7comm"}}
     */

    public String timestamp;
    public LayersBean layers;
    public static class LayersBean {
        /**
         * frame : {"frame_frame_encap_type":"1","frame_frame_time":"Jan  1, 2000 11:00:32.244216000 ä¸­å\u009b½æ \u0087å\u0087\u0086æ\u0097¶é\u0097´","frame_frame_offset_shift":"0.000000000","frame_frame_time_epoch":"946695632.244216000","frame_frame_time_delta":"0.009438000","frame_frame_time_delta_displayed":"0.009438000","frame_frame_time_relative":"0.042628000","frame_frame_number":"5","frame_frame_len":"85","frame_frame_cap_len":"85","frame_frame_marked":"0","frame_frame_ignored":"0","frame_frame_protocols":"eth:ethertype:ip:tcp:tpkt:cotp:s7comm"}
         */
        public FrameBean frame;
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

            public String frame_frame_protocols;
        }
    }
}
