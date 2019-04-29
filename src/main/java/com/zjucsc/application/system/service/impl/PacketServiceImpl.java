package com.zjucsc.application.system.service.impl;

import com.zjucsc.application.domain.bean.NetworkInterface;
import com.zjucsc.application.system.service.PacketService;
import com.zjucsc.application.util.NetworkInterfaceUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Service("packet_service")
public class PacketServiceImpl implements PacketService {

    @Override
    public List<NetworkInterface> getAllNetworkInterface() throws SocketException {
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
}
