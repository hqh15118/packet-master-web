package com.zjucsc.application.system.service.impl;

import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.bean.NetworkInterface;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.PacketService;
import com.zjucsc.application.util.NetworkInterfaceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Service("packet_service")
public class PacketServiceImpl implements PacketService {

    @Autowired
    public PacketAnalyzeService packetAnalyzeService;

    @Cacheable("networt_interfaces")
    @Override
    public List<NetworkInterface> getAllNetworkInterface() throws SocketException {
        return dogetAllNetworkInterface();
    }
    @CachePut("networt_interfaces")
    @Override
    public List<NetworkInterface> getAllNetworkInterfaceFlush() throws SocketException {
        return dogetAllNetworkInterface();
    }

    private List<NetworkInterface> dogetAllNetworkInterface() throws SocketException {
        List<NetworkInterface> all = new ArrayList<>();
        try {
            Enumeration<java.net.NetworkInterface> networkInterfaces  = java.net.NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()){
                java.net.NetworkInterface networkInterface  = networkInterfaces.nextElement();
                NetworkInterface anInterface = new NetworkInterface();
                anInterface.setName(networkInterface.getName());
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                List<String> ipAddress = new ArrayList<>();
                while(inetAddressEnumeration.hasMoreElements()){
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    ipAddress.add(inetAddress.getHostAddress());
                }
                anInterface.setDescription(networkInterface.getDisplayName());
                anInterface.setIsUp(Boolean.toString(networkInterface.isUp()));
                anInterface.setMacAddress(NetworkInterfaceUtil.convertByteToMacAddress(networkInterface.getHardwareAddress()));
                anInterface.setIpAddressed(ipAddress);
                all.add(anInterface);
            }
        } catch (SocketException e) {
            throw new SocketException("can not capture network interface");
        }
        return all;
    }

    /**
     * 10秒钟发送一次统计信息，发送总报文数、总流量
     */
    @Scheduled(fixedRate = 10000)
    public void sendPacketStatisticsMsg(){
        SocketServiceCenter.updateAllClient(SocketIoEvent.STATISTICS_PACKET,new StatisticsDataWrapper(packetAnalyzeService.getRecvPacketNumber()
                ,packetAnalyzeService.getRecvPacketFlow()));
    }

    public static class StatisticsDataWrapper{
        public long number;
        public long flow;

        public StatisticsDataWrapper(long number, long flow) {
            this.number = number;
            this.flow = flow;
        }
    }
}
