package com.zjucsc.packetmasterweb.spring;
import com.zjucsc.application.config.SocketIoEvent;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.junit.Test;

import java.net.URISyntaxException;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 16:48
 */
public class SocketIOClient {

    private int receivedPacket = 0;

    @Test
    public void getMsgFromServer() throws URISyntaxException, InterruptedException {
        Socket socket = IO.socket("http://localhost:8081");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("connect successfully");
                //socket.emit("foo", "hi");
                //socket.disconnect();
            }

        }).on(SocketIoEvent.BAD_PACKET, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                for (Object arg : args) {
                    System.out.println("bad packet + " + arg);
                }
            }

        }).on(SocketIoEvent.ALL_PACKET, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Object object : objects) {
                    receivedPacket++;
                    System.out.println("all :" + object);
                }
            }
        }).on(SocketIoEvent.STATISTICS_PACKET, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Object object : objects) {
                    System.out.println("statistics " + object);
                }
            }
        }).on(SocketIoEvent.COLLECTOR_STATE, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Object object : objects) {
                    System.out.println("collector state change : " + object);
                }
            }
        }).on(SocketIoEvent.ATTACK_INFO, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for (Object object : objects) {
                    System.out.println("receive attack info " + object);
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("disconnect");
            }

        });
        socket.connect();
        Thread.sleep(100000000);
        socket.disconnect();
    }
}
