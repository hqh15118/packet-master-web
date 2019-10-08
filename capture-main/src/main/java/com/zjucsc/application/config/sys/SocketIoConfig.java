package com.zjucsc.application.config.sys;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.zjucsc.application.system.service.ScheduledService;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.socket_io.Event;
import com.zjucsc.socket_io.EventHandler;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SocketIoConfig {

    @Autowired private ScheduledService scheduledService;

    @EventHandler
    public List<DataListener> registerDataListener(){
        return new ArrayList<DataListener>(){
            {
                add(new CollectorStateListener());
                add(new D2DPacketListener().setInnerCall(() -> scheduledService.sendDevice2DevicePackets()));
            }
        };
    }

    @Event(eventType = Object.class,event = "collector_state")
    public static class CollectorStateListener implements DataListener<Object>{

        @Override
        public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest) {
            SocketServiceCenter.updateAllClient(SocketIoEvent.COLLECTOR_STATE, PacketDecodeUtil.getCollectorStateMap());
        }
    }

    @Event(eventType = Object.class , event = "d2d_packet")
    public static class D2DPacketListener implements DataListener<Object>{
        private InnerCall innerCall;
        @Override
        public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest) {
            innerCall.innerCall();
        }
        private D2DPacketListener setInnerCall(InnerCall innerCall){
            this.innerCall = innerCall;
            return this;
        }
    }

    private interface InnerCall{
        void innerCall();
    }
}
