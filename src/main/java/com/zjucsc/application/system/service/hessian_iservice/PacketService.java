package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.NetworkInterface;

import java.net.SocketException;
import java.util.List;

public interface PacketService {
    List<NetworkInterface> getAllNetworkInterface() throws SocketException;
    List<NetworkInterface> getAllNetworkInterfaceFlush() throws SocketException;
}
