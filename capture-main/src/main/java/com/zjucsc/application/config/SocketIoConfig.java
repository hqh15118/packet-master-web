package com.zjucsc.application.config;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.socket_io.Event;
import com.zjucsc.socket_io.EventHandler;
import com.zjucsc.socket_io.SocketIoEvent;
import com.zjucsc.socket_io.SocketServiceCenter;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SocketIoConfig {

    @EventHandler
    public List<DataListener> registerDataListener(){
        return new ArrayList<DataListener>(){
            {
                add(new CollectorStateListener());
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
}
