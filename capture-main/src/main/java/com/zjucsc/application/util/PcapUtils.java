package com.zjucsc.application.util;

import org.apache.commons.lang3.StringUtils;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.LinkLayerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 1、列出所有的设备名称；
 * 2、根据设备名称找到设备的mac地址、ip地址等信息(bean)；
 * 3、打开一个设备开始抓包；
 */
public class PcapUtils {

    private static WeakReference<List<PcapNetworkInterface>> weakReference = null;

    private static Logger logger = LoggerFactory.getLogger(PcapUtils.class);
    /**
     * cachedOpenDevices [key,value] -> [ip,pcaphandler] and [name,pcaphandler]
     */
    private static volatile ConcurrentHashMap<String, PcapHandle> cachedOpenedDevices = new ConcurrentHashMap<>(15);

    private static final int INTERFACE_IP = 0,INTERFACE_NAME = 1;

    //lock Semaphore
    private static final byte[] lockObject = new byte[1];

    //lock AtomicInteger
    private static final byte[] lockObject1 = new byte[1];

    private static volatile HashMap<String,Semaphore> getTargetNetworkInterfaceIp4 = new HashMap<>(10);

    private static HashMap<String,AtomicInteger> atomicDeviceLocalMap = new HashMap<>(10);

    private static Pattern ipPattern = Pattern.compile("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}");

    private static Pattern macPattern = Pattern.compile("[0-9a-zA-Z]{2}:[0-9a-zA-Z]{2}:[0-9a-zA-Z]{2}:[0-9a-zA-Z]{2}");

/*


  ###   #     # ####### #######     #     # #######   ###   #        #####
   #    ##    # #       #     #     #     #    #       #    #       #     #
   #    # #   # #       #     #     #     #    #       #    #       #
   #    #  #  # #####   #     #     #     #    #       #    #        #####
   #    #   # # #       #     #     #     #    #       #    #             #
   #    #    ## #       #     #     #     #    #       #    #       #     #
  ###   #     # #       #######      #####     #      ###   #######  #####


*/



    public static List<String> getAllNetworkInterfaceDetailInfo(){
        return doGetAllNetworkInterfaceDetailInfo();
    }

    public static List<String> getAllNetWorkInterfaceName(){
        List<PcapNetworkInterface> temp = doGetAllNetworkInterfaces();
        List<String> interfacesInfos = new ArrayList<>(temp.size());
        int size = temp.size();
        for (PcapNetworkInterface pcapNetworkInterface : temp){
            interfacesInfos.add(pcapNetworkInterface.getName());
        }
        return interfacesInfos;
    }

    //用all info实现的，可以改善
    public static List<String> getAllNetWorkInterfaceIPAddress(){
        Pattern ipPattern = Pattern.compile("\\[/\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}");
        List<String> temp = doGetAllNetworkInterfaceDetailInfo();
        List<String> ipAddressed = new ArrayList<>(temp.size());
        int size = temp.size();
        for (int i = 0; i < size; i++) {
            String target = temp.get(i);
            try {
                Matcher matcher = ipPattern.matcher(target);
                if(matcher.find()) {
                    String tmp = matcher.group();
                    //[/192.168.1.104, [/127.0.0.0
                    ipAddressed.add(ipAddressed.size(),tmp.substring(2));
                }
            }catch (IllegalStateException e){
                logger.warn(e.getMessage() + "{}",e);
            }
        }
        return ipAddressed;
    }

    public static List<PcapNetworkInterface> getAllUpNetworkInterfaces(){
        return getAllTargetStateNetworkInterfaces(1);
    }

    public static List<PcapNetworkInterface> getAllRunningNetworkInterfaces(){
        return getAllTargetStateNetworkInterfaces(0);
    }

    /**
     * @param state running = 0,up = 1,
     * @return
     */
    private static List<PcapNetworkInterface> getAllTargetStateNetworkInterfaces(int state){
        List<PcapNetworkInterface> list = doGetAllNetworkInterfaces();
        List<PcapNetworkInterface> allUpInterface = new ArrayList<>();
        switch (state){
            case 0 :
                for (PcapNetworkInterface pcapNetworkInterface : list){
                    if (pcapNetworkInterface.isRunning()){
                        allUpInterface.add(pcapNetworkInterface);
                    }
                }
                break;
            case 1:
                for (PcapNetworkInterface pcapNetworkInterface : list){
                    if (pcapNetworkInterface.isUp()){
                        allUpInterface.add(pcapNetworkInterface);
                    }
                }
                break;
        }
        return allUpInterface;
    }


    /**
     * @param info IP or Name
     * @return
     */
    public static String getTargetNetworkInterfaceMacAddress(String info){
        PcapNetworkInterface targetNetworkInterface = getTargetNetworkInterface(info);
        if (targetNetworkInterface == null){
            logger.error("can not find the device by {}" , info);
            return null;
        }
        return getTargetNetInterfaceMacAddress(targetNetworkInterface);
    }

    public static PcapNetworkInterface getTargetNetworkInterface(String info){
        PcapNetworkInterface targetNetworkInterface;
        if (checkIp(info)){
            targetNetworkInterface = getTargetNetworkInterfaceByIp4(info);
            if (null == targetNetworkInterface){
                logger.warn("can not find the network interface for {} ip address , " , info);
                return null;
            }
        }else{
            targetNetworkInterface = getTargetNetworkInterfaceByName(info);
            if (null == targetNetworkInterface){
                logger.warn("can not find the network interface by {} [network interface name], " , info);
                return null;
            }
        }
        return targetNetworkInterface;
    }

    public static PcapHandle openDevice(String ipOrName,int snapLength,int timeOut){
        PcapHandle openPcapHandler = null;
        // if input type is ip
        if (checkIp(ipOrName)){
            openPcapHandler = openDeviceByIp(ipOrName,snapLength,timeOut);
        }else {
            openPcapHandler = openDeviceByName(ipOrName,snapLength,timeOut);
        }
        return openPcapHandler;
    }

    public static synchronized void closeDevice(String info){
        if (cachedOpenedDevices.containsKey(info)){
            PcapHandle pcapHandle = cachedOpenedDevices.get(info);
            if (pcapHandle!=null) {
                try {
                    pcapHandle.breakLoop();
                } catch (NotOpenException e) {
                    logger.warn("{} is not open");
                }
                pcapHandle.close();
                logger.info("{} close now",info);
                //清楚map中的缓存
                clearOpenedDevice(info,pcapHandle);
            }else{
                logger.error("can not close {} ,because it is not exist in cache",info);
            }
        }else{
            logger.warn("can not close the target device : {} , cause it is not cached , check it's state",info);
        }
    }

    public static String getTargetNetworkInterfaceIp(String interfaceName){
        if(interfaceName == null ) {
            logger.error("interface name is null");
            return null;
        }
        for (PcapNetworkInterface pcapNetworkInterface : doGetAllNetworkInterfaces()){
            if (pcapNetworkInterface.getName().equals(interfaceName)){
                //0 means ipv6 ; 1 means ipv4
                for (PcapAddress pcapAddress : pcapNetworkInterface.getAddresses()) {
                    if (pcapAddress instanceof PcapIpV4Address) {
                        return pcapAddress.getAddress().getHostAddress();
                    }
                }
            }
        }
        logger.warn("can not find the target device [IP4] of {} , maybe it is null or your input IP is invalid",interfaceName);
        return null;
    }

    public static String getTargetNetworkInterfaceName(String targetIp){
        if (targetIp == null) {
            logger.error("interface ip is null");
            return null;
        }
        if (!checkIp(targetIp)){
            logger.error("interface ip is invalid : {}",targetIp);
            return null;
        }
        targetIp = StringUtils.trim(targetIp);
        for (PcapNetworkInterface pcapNetworkInterface : doGetAllNetworkInterfaces()){
            List<PcapAddress> pcapAddressList = pcapNetworkInterface.getAddresses();
            for (PcapAddress address : pcapAddressList){
                if (address.getAddress().getHostAddress().equals(targetIp)){
                    return pcapNetworkInterface.getName();
                }
            }
        }
        logger.error("can not find the target [name] of {} , please check ip again",targetIp);
        return null;
    }

    //=======================
    //=======================
    //=======================



    /* synchronized method for singleton
     */
    private static synchronized List<PcapNetworkInterface> doGetAllNetworkInterfaces(){
        //没有初始化/list被回收/没有正确获取网卡接口信息
        if (weakReference == null || weakReference.get() == null || Objects.requireNonNull(weakReference.get()).size() == 0 ){
            try {
                List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();
                if (allDevs == null){
                    logger.error("can not find any network interfaces");
                    weakReference = new WeakReference<>(new ArrayList<>());
                }else{
                    logger.info("get {} interfaces successfully",allDevs.size());
                    weakReference = new WeakReference<>(Pcaps.findAllDevs());
                }
            } catch (PcapNativeException e) {
                if (logger.isDebugEnabled()){
                    e.printStackTrace();
                }else{
                    logger.error(e.getMessage());
                }
            }
        }
        return weakReference.get();
    }

    private static List<String> doGetAllNetworkInterfaceDetailInfo(){
        List<PcapNetworkInterface> allDevs = doGetAllNetworkInterfaces();
        assert allDevs!= null;
        List<String> stringList = new ArrayList<>(allDevs.size());
        int i = 0;
        for (PcapNetworkInterface pcapNetworkInterface : allDevs){
            stringList.add(" " + i + " : " + pcapNetworkInterface.toString());
            i++;
        }
        return stringList;
    }
    /**
     *
     * @param targetIp
     * @return targetNetworkInterface
     */
    private static PcapNetworkInterface getTargetNetworkInterfaceByIp4(String targetIp){
        List<PcapNetworkInterface> list = doGetAllNetworkInterfaces();
        if (!checkIp(targetIp)){
            if (logger.isDebugEnabled()){
                logger.debug("{} is not a ip4 address",targetIp);
            }
            return null;
        }
        if (targetIp == null){
            return  null;
        }
        targetIp = StringUtils.trim(targetIp);
        for (PcapNetworkInterface pcapNetworkInterface : list){
            List<PcapAddress> pcapAddressList = pcapNetworkInterface.getAddresses();
            for (PcapAddress address : pcapAddressList){
                if (address.getAddress().getHostAddress().equals(targetIp)){
                    return pcapNetworkInterface;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param targetName
     * @return targetNetworkInterface
     */
    private static PcapNetworkInterface getTargetNetworkInterfaceByName(String targetName){
        List<PcapNetworkInterface> list = doGetAllNetworkInterfaces();
        if (targetName == null){
            return  null;
        }
        targetName = StringUtils.trim(targetName);
        for (PcapNetworkInterface pcapNetworkInterface : list){
            if (pcapNetworkInterface.getName().equals(targetName)){
                return pcapNetworkInterface;
            }
        }
        return null;
    }

    private static String getTargetNetInterfaceMacAddress(PcapNetworkInterface pcapNetworkInterface){
        List<LinkLayerAddress> linkLayerAddresses = pcapNetworkInterface.getLinkLayerAddresses();
        for (LinkLayerAddress address : linkLayerAddresses){
            if (macPattern.matcher(address.toString()).find())
                return address.toString();
        }
        return null;
    }

    /**
     * @param info ip or name
     * @return 打开的实例，如果还没有初始化，单线程进入初始化返回null
     */
    private static PcapHandle activeEnerty(String info){
        //如果已经初始化过，则直接返回
        if (cachedOpenedDevices.containsKey(info)){
            return cachedOpenedDevices.get(info);
        }
        //还没有初始化，记录等待的线程数
        getThreadInnerAtomicInterger(info).incrementAndGet();
            try {
                getThreadInnerSemaphore(info).acquire(1);
                //一条线程获取锁就减1
                getThreadInnerAtomicInterger(info).decrementAndGet();
            } catch (InterruptedException e) {
                logger.warn(e.getMessage());
            }
            if (cachedOpenedDevices.containsKey(info)) {
                return cachedOpenedDevices.get(info);
            }
            return null;
    }


    private static Semaphore getThreadInnerSemaphore(String info){
        if (getTargetNetworkInterfaceIp4.get(info) == null){
            Semaphore semaphore;
            synchronized (lockObject){
                if (getTargetNetworkInterfaceIp4.get(info) == null)
                    if (checkIp(info)){
                        String targetName;
                        if ((targetName = getTargetNetworkInterfaceName(info))!=null) {
                            semaphore = new Semaphore(1);
                            getTargetNetworkInterfaceIp4.put(info, semaphore);
                            getTargetNetworkInterfaceIp4.put(targetName, semaphore);
                        }
                    }else{
                        String targetIp;
                        if ((targetIp = getTargetNetworkInterfaceIp(info))!=null) {
                            semaphore = new Semaphore(1);
                            getTargetNetworkInterfaceIp4.put(info, semaphore);
                            getTargetNetworkInterfaceIp4.put(targetIp, semaphore);
                        }
                    }
            }
        }
        return getTargetNetworkInterfaceIp4.get(info);
    }

    private static AtomicInteger getThreadInnerAtomicInterger(String info){
        if ((atomicDeviceLocalMap.get(info)) == null){
            AtomicInteger atomicInteger;
            synchronized (lockObject1){
                if (atomicDeviceLocalMap.get(info) == null)
                    if (checkIp(info)){
                        String targetName;
                        if ((targetName = getTargetNetworkInterfaceName(info))!=null) {
                            atomicInteger = new AtomicInteger(0);
                            atomicDeviceLocalMap.put(info, atomicInteger);
                            atomicDeviceLocalMap.put(targetName, atomicInteger);
                        }
                    }else{
                        String targetIp;
                        if ((targetIp = getTargetNetworkInterfaceIp(info))!=null) {
                            atomicInteger = new AtomicInteger(0);
                            atomicDeviceLocalMap.put(info, atomicInteger);
                            atomicDeviceLocalMap.put(targetIp, atomicInteger);
                        }
                    }
            }
        }
        return atomicDeviceLocalMap.get(info);
    }

    //输入ip是否符合IP地址格式
    private static boolean checkIp(String info){
        if (ipPattern.matcher(info).matches()) {
            logger.info("{} is ip",info);
            return true;
        }
        logger.info("{} is name",info);
        return false;
    }



    private static void clear(String info, PcapHandle pcapHandle, String targetIp) {
        cachedOpenedDevices.remove(info, pcapHandle);
        cachedOpenedDevices.remove(targetIp, pcapHandle);
        getTargetNetworkInterfaceIp4.put(info,null);
        getTargetNetworkInterfaceIp4.remove(info);
        getTargetNetworkInterfaceIp4.put(targetIp,null);
        getTargetNetworkInterfaceIp4.remove(targetIp);
        atomicDeviceLocalMap.put(info,null);
        atomicDeviceLocalMap.remove(info);
        atomicDeviceLocalMap.put(targetIp,null);
        atomicDeviceLocalMap.remove(targetIp);
    }


/*


    ####### ######  #######     #     # #######   ###   #        #####
    #     # #     #    #        #     #    #       #    #       #     #
    #     # #     #    #        #     #    #       #    #       #
    #     # ######     #        #     #    #       #    #        #####
    #     # #          #        #     #    #       #    #             #
    #     # #          #        #     #    #       #    #       #     #
    ####### #          #         #####     #      ###   #######  #####


* */

    public static void endlessLoopHandler(PcapHandle pcapHandle,PacketListener listener){
        if (!cachedOpenedDevices.containsValue(pcapHandle)){
            logger.error("{} has not start or has been closed,please check",pcapHandle);
            return;
        }
        try {
            pcapHandle.loop(-1,listener);
        } catch (PcapNativeException | NotOpenException e) {
            logger.error("{}",e);
        } catch (InterruptedException e) {
            if (logger.isDebugEnabled()){
                logger.debug("{} closed",pcapHandle);
            }
            logger.info("{} closed",pcapHandle);
        }
    }

    public static void getDumpPacket(String info,String fileName){
        PcapNetworkInterface pcapNetworkInterface = getTargetNetworkInterface(info);
        PcapHandle pcapHandle = null;
        try {
            pcapHandle = pcapNetworkInterface.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,1000);
            String filter = "tcp"; // 设置过滤的字符串
            try {
                pcapHandle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
            } catch (PcapNativeException | NotOpenException e) {
                System.err.println(e);
            }
        } catch (PcapNativeException e) {
            System.err.println(e);
        }
        if (pcapHandle!=null){
            try {
                PcapDumper pcapDumper = pcapHandle.dumpOpen(fileName);
                pcapHandle.loop(10,pcapDumper);
            } catch (PcapNativeException | NotOpenException | InterruptedException e) {
                System.err.println(e);
            }

        }
    }

    public static void sendDumpedPacket(String info,String dumpedFileName){
        PcapHandle pcapHandle;
        PcapHandle sendTargetDevicePcaHandler = openDevice(info,65536,1000);
        try {
            pcapHandle = Pcaps.openOffline(dumpedFileName);
            Packet packet;
            while ((packet = pcapHandle.getNextPacket())!=null){
                sendTargetDevicePcaHandler.sendPacket(packet);
            }
        } catch (PcapNativeException | NotOpenException e) {
            System.err.println(e);
        }
    }

    //=======================
    //=======================
    //=======================


    private static PcapHandle openDeviceByIp(String ip, int snapLength,int timeOut){
        PcapHandle pcapHandle;
        if (ip == null){
            logger.error("can not open device by 'null' ip");
            return null;
        }
        if ((pcapHandle = activeEnerty(ip))!=null){
            return pcapHandle;
        }
        PcapNetworkInterface dev = null;
        try {
            dev = Pcaps.getDevByAddress(InetAddress.getByName(ip));
        } catch (PcapNativeException e) {
            logger.error("can not get the ip {},please check the input dev ip \n" +
                    "error message : {}",ip , e.getReturnCode() ,e.getMessage());
        } catch (UnknownHostException e) {
            logger.error("can not get the ip {},please check the input dev ip \n" +
                    "error message : {}",ip , e.getMessage());
        }
        pcapHandle = doOpenDevice(dev,snapLength,timeOut,ip,INTERFACE_IP);
        getThreadInnerSemaphore(ip).release(atomicDeviceLocalMap.get(ip).get());
        return pcapHandle;
    }



    private static PcapHandle doOpenDevice(PcapNetworkInterface dev,int snapLength,int timeOut,String info,int type){
        PcapHandle openingHandler;
        if (null != dev){
            try {
                openingHandler =  dev.openLive(snapLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,timeOut);
                //info is [name]
                if (type == INTERFACE_NAME){
                    String targetIp;
                    if ((targetIp = getTargetNetworkInterfaceIp(info))!=null){
                        //add ip & name
                        cachedOpenedDevices.put(info,openingHandler);
                        cachedOpenedDevices.put(targetIp,openingHandler);
                    }
                }else
                //info is [ip]
                {
                    String targetName;
                    if ((targetName = getTargetNetworkInterfaceName(info))!=null){
                        //add ip & name
                        cachedOpenedDevices.put(info,openingHandler);
                        cachedOpenedDevices.put(targetName,openingHandler);
                    }
                }
                return openingHandler;
            } catch (PcapNativeException e) {
                logger.error("can not open the device {} \n error message {}" , dev.getName() , e.getMessage());
            }
        }else{
            logger.error("can not touch the device \n error message");
        }
        return null;
    }


    private static void clearOpenedDevice(String info,PcapHandle pcapHandle){
        if (checkIp(info)) {
            String targetName;
            if ((targetName = getTargetNetworkInterfaceName(info)) != null) {
                clear(info, pcapHandle, targetName);
            } else {
                logger.warn("{} has not target interface name" +
                        "cachedOpenedDevices size : {} ",info,cachedOpenedDevices.size() );
            }
        }else{
            String targetIp;
            if ((targetIp = getTargetNetworkInterfaceIp(info)) != null) {
                clear(info, pcapHandle, targetIp);
            } else {
                logger.warn("{} has not target interface ip" +
                        "cachedOpenedDevices size : {} " ,info,cachedOpenedDevices.size());
            }
        }
    }

    /**
     *  通过网卡名字打开设备
     * @param name 网卡名
     * @param snapLength 报文最长长度
     * @param timeOut 超时
     * @return 打开的网卡实例
     */
    private static PcapHandle openDeviceByName(String name, int snapLength,int timeOut){
        PcapHandle pcapHandle;
        if (name == null){
            logger.error("can not open device by 'null' name");
            return null;
        }
        if ((pcapHandle = activeEnerty(name))!=null){
            return pcapHandle;
        }

        PcapNetworkInterface dev = null;
        try {
            dev = Pcaps.getDevByName(name);
        } catch (PcapNativeException e) {
            logger.error("can not get the device {},please check the input dev name \n" +
                    "return code : {} \n" +
                    "error message : {}",name , e.getReturnCode() ,e.getMessage());
        }
        pcapHandle = doOpenDevice(dev,snapLength,timeOut,name,INTERFACE_NAME);
        getThreadInnerSemaphore(name).release(getThreadInnerAtomicInterger(name).get());
        return pcapHandle;
    }

}
