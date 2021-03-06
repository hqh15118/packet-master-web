package com.zjucsc.application.system.service.common_impl;

import com.zjucsc.application.domain.bean.NetworkInterface;
import com.zjucsc.application.system.service.PacketAnalyzeService;
import com.zjucsc.application.system.service.common_iservice.INetworkInterfaceService;
import com.zjucsc.application.util.NetworkInterfaceUtil;
import com.zjucsc.application.util.PcapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Service
public class NetworkInterfaceServiceImpl implements INetworkInterfaceService {

    @Autowired
    public PacketAnalyzeService packetAnalyzeService;

    //@Cacheable("network_interfaces")
    @Override
    public List<NetworkInterface> getAllNetworkInterface() throws SocketException {
        return doGetAllNetworkInterface();
    }
    //@CachePut("network_interfaces")
    @Override
    public List<NetworkInterface> getAllNetworkInterfaceFlush() throws SocketException {
        return doGetAllNetworkInterface();
    }

    public static  List<NetworkInterface> doGetAllNetworkInterface() throws SocketException {
        List<NetworkInterface> all = new ArrayList<>();
        try {
            Enumeration<java.net.NetworkInterface> networkInterfaces  = java.net.NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()){
                boolean validIpAddress = false;
                java.net.NetworkInterface networkInterface  = networkInterfaces.nextElement();
                NetworkInterface anInterface = new NetworkInterface();
                anInterface.setOsSmartName(networkInterface.getName());
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                List<String> ipAddress = new ArrayList<>();
                while(inetAddressEnumeration.hasMoreElements()){
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    if (!StringUtils.isBlank(inetAddress.getHostAddress())){
                        ipAddress.add(inetAddress.getHostAddress());
                        validIpAddress = true;
                    }
                }
                if (!validIpAddress)
                {
                    continue;
                }
                String realName = "";
                for (String address : ipAddress) {
                    if ((realName = PcapUtils.getTargetNetworkInterfaceName(address))!=null){
                        break;
                    }
                }
                anInterface.setDeviceRealName(realName);
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
