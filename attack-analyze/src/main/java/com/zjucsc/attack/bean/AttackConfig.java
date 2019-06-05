package com.zjucsc.attack.bean;

import java.util.HashSet;
import java.util.Set;
public class AttackConfig {
    /***************************************************
     *
     * 攻击类型
     *
     **************************************************/
    public static final int DOS = 21,               //DOS攻击
                            SNIFF = 38,             //嗅探攻击
                            CONTROL_TAMPER = 15,    //控制篡改
                            DATA_TAMPER = 35,       //数据篡改
                            CODE_TAMPER = 54,       //代码篡改
                            CONFIG_TAMPER = 4;      //配置篡改

    public static boolean debug = false;

    /***************************************************
     *
     * Ⅰ ： D_DOS攻击
     *
     **************************************************/

    /****************************************************
     * 一、某节点（IP地址为目的站点）在一段时间（暂定为【1】分钟，可设置）内，
     * 连续收到【2】个以上（可动态设置）来自一个IP地址（站点）的TCP连接
     * 建立请求报文，即同址TCP-DOS攻击。
     ****************************************************/
    private static volatile int CO_SITE_TIME_GAP = 60 * 1000;   //60s
    private static volatile int CO_SITE_NUM = 2;

    /****************************************************
     * 二、某节点（IP地址为目的站点）在一段时间（暂定为1秒钟，可设置）内，
     * 连续收到2个（可动态设置）以上来自不同IP地址（站点）发出的TCP
     * 连接建立请求报文，即多址TCP-DOS攻击。
     ****************************************************/
    private static volatile int MULTI_SITE_TIME_GAP = 1000;     //1s
    private static volatile int MULTI_SITE_NUM = 2;

    /****************************************************
     * 三、某节点（IP地址为目的站点）在一段时间（暂定为10毫秒，可动态设置）内，
     * 连续收到2个（可动态设置）以上来自同一IP地址（站点）发出的工控协议报
     * 文，即允许工控协议同址占用信道攻击；
     *****************************************************/
    private static volatile int CO_SITE_CHANNEL_HOLD_TIME_GAP = 10;     //10ms
    private static volatile int CO_SITE_CHANNEL_HOLD_NUM = 2;

    /****************************************************
     * 四、某节点（IP地址为目的站点）在一段时间（暂定为10毫秒，可动态设置）内，
     * 连续收到2个（可动态设置）以上来自不同IP地址（站点）发出的工控协议报
     * 文，即允许工控协议多址占用信道攻击；
     ****************************************************/
    private static volatile int MULTI_SITE_CHANNEL_HOLD_TIME_GAP = 10;
    private static volatile int MULTI_SITE_CHANNEL_HOLD_NUM = 2;

    /***************************************************
     *
     * Ⅱ ： 嗅探攻击
     *
     **************************************************/

    /***************************************************
     * 一、某节点（IP地址为目的站点）接收的报文中，来自本地控制系统局域网内的IP站点（需配置）内、
     * 利用允许工控协议指令报文(节点允许协议，如modbus,profinet,s7,103,104,OPC等)探测
     * 设备的请求报文，即某工控协议嗅探攻击。
     ***************************************************/
    private static volatile Set<String> SNIFF_LAN_IPS = new HashSet<>();

    /***************************************************
     * 二、某节点（IP地址为目的站点）接收的报文中，来自本地控制系统局域网内的IP站点（需配置）外，
     * 利用允许工控协议指令报文(节点允许协议，如modbus,profinet,s7,103,104,OPC等)探测
     * 设备的请求报文，即某工控协议嗅探攻击。
     ***************************************************/

    /***************************************************
     * 来自远程的ARP/R_ARP报文，指本地局域网内的站点发出的（可用于后期分析，到底有没有这样的探测？）
     ***************************************************/

    /***************************************************
     *
     * Ⅲ ： 篡改攻击 tampering attack
     *
     **************************************************/

    /***************************************************
     * 一、某节点（IP地址为目的站点）接收的报文中，发出工控协议(节点允许协议，如modbus,profinet,s7,103,104,OPC等)
     * 读、写指令的站点（源IP地址）不在“被允许的控制节点”队列（针对重要节点，需配置“被允许的控制节点”，
     * 是否应配置控制节点的MAC，请商讨）中，即非法控制站点攻击，可区分本地控制系统局域网内、外两种情况，以区别内外的嗅探。
     * 【功能码黑名单】
     ***************************************************/

    /***************************************************
     * 二、某节点（IP地址为目的站点）接收的报文中，发出工控协议(节点允许协议，如modbus,profinet,s7,103,104,OPC等)读、
     * 写指令的站点（源IP地址）在“被允许的控制节点”队列中，但在一段时间（暂定为10毫秒，可动态设置）内，连续发出差异比较大
     * （（本条指令操作数据-上条指令操作数据）/ 上条指令操作数据>50%），即异常控制数据攻击。此条包括两种类型，即传感器数据篡改
     * （读指令）、控制器操作指令（写指令）篡改，可区分。
     * 【。。。】
     ***************************************************/

    /***************************************************
     * 三、某节点（IP地址为目的站点）接收的报文中，发出工控协议(节点允许协议，如modbus,profinet,s7,103,104,OPC等)读、
     * 写指令的站点（源IP地址）在“被允许的控制节点”队列中，但该指令的发出不满足安全生产条件（如案例1：梯级船闸系统中，
     * 某一闸门的打开指令必须在其前后闸门皆关闭的前提下才是合法的，否则为非法（可将此例做一个配置、判断案例1）；再比如，
     * 案例2：某反应器中的加热棒控制指令、进放料控制指令发出时，反应器中的压力、温度已超越安全生产限，此时其操作指令将使
     * 反应器中的压力、温度继续越限；案例3：变电站控制系统，利用101，103，104，61850，61970等协议，将断路器、闸刀操作
     * 顺序颠倒，正常顺序为：断开电路时，先断开断路器，再断开相邻配置的刀闸；合上电路时，先合刀闸，再合相邻配置的断路器。
     * 请研究一下，针对上述案例，写一个配置规则和配置页面，），即非安全控制指令攻击。
     * 【工艺的操作顺序，用一个链表记录所有的操作顺序】
     ***************************************************/

    /***************************************************
     * >>> 代码篡改攻击
     * 四、某节点（IP地址为目的站点）接收的报文中，发出工控协议(节点允许协议，如modbus,profinet,s7,103,104,OPC等)读、
     * 写程序/数据块指令的站点（源IP地址）不在“被允许的程序/数据上下载控制节点”队列（针对重要节点，需配置“被允许的程序/
     * 数据上下载控制节点”）中，即非法程序上下载控制站点攻击。
     * 【功能码黑名单】
     ***************************************************/

    /***************************************************
     * >>> 代码篡改攻击
     * 五、某节点（IP地址为目的站点）接收的报文中，发出工控协议(节点允许协议，如modbus,profinet,s7,103,104,OPC等)读、
     * 写程序/数据块指令的站点（源IP地址）在“被允许的程序/数据上下载控制节点”队列中，但该操作的执行不在允许的时间（
     * 针对重要节点，需配置是否“被允许程序/数据上下载”）内，即非法程序篡改攻击。
     ***************************************************/

    /***************************************************
     * >>> 配置篡改攻击
     * 六、某节点（IP地址为目的站点）接收的报文中，发出工控协议(节点允许协议，如modbus,profinet,s7,103,104,OPC等)配置文件、
     * 数据的读、写指令的站点（源IP地址）不在“被允许的配置上下载控制节点”队列（针对重要节点，需配置“被允许的配置上下载控制节点”）
     * 中，即非法配置控制站点攻击。
     ***************************************************/

    /***************************************************
     * >>> 配置篡改攻击
     * 七、某节点（IP地址为目的站点）接收的报文中，发出工控协议(节点允许协议，如modbus,profinet,s7,103,104,OPC等)配置文件、
     * 数据的读、写指令的站点（源IP地址）在“被允许的配置上下载控制节点”队列（针对重要节点，需配置“被允许的配置上下载控制节点”）中，
     * 但该操作的执行不在允许的时间（针对重要节点，需配置是否“被允许配置上下载”）内，即非法配置篡改攻击。
     ***************************************************/


    public static Set<String> getSniffLanIps() {
        return SNIFF_LAN_IPS;
    }

    public static synchronized void setSniffLanIps(Set<String> sniffLanIps) {
        SNIFF_LAN_IPS = sniffLanIps;
    }


    public static int getCoSiteTimeGap() {
        return CO_SITE_TIME_GAP;
    }

    public static void setCoSiteTimeGap(int coSiteTimeGap) {
        CO_SITE_TIME_GAP = coSiteTimeGap;
    }

    public static int getCoSiteNum() {
        return CO_SITE_NUM;
    }

    public static void setCoSiteNum(int coSiteNum) {
        CO_SITE_NUM = coSiteNum;
    }

    public static int getMultiSiteTimeGap() {
        return MULTI_SITE_TIME_GAP;
    }

    public static void setMultiSiteTimeGap(int multiSiteTimeGap) {
        MULTI_SITE_TIME_GAP = multiSiteTimeGap;
    }

    public static int getMultiSiteNum() {
        return MULTI_SITE_NUM;
    }

    public static void setMultiSiteNum(int multiSiteNum) {
        MULTI_SITE_NUM = multiSiteNum;
    }

    public static int getCoSiteChannelHoldTimeGap() {
        return CO_SITE_CHANNEL_HOLD_TIME_GAP;
    }

    public static void setCoSiteChannelHoldTimeGap(int coSiteChannelHoldTimeGap) {
        CO_SITE_CHANNEL_HOLD_TIME_GAP = coSiteChannelHoldTimeGap;
    }

    public static int getCoSiteChannelHoldNum() {
        return CO_SITE_CHANNEL_HOLD_NUM;
    }

    public static void setCoSiteChannelHoldNum(int coSiteChannelHoldNum) {
        CO_SITE_CHANNEL_HOLD_NUM = coSiteChannelHoldNum;
    }

    public static int getMultiSiteChannelHoldTimeGap() {
        return MULTI_SITE_CHANNEL_HOLD_TIME_GAP;
    }

    public static void setMultiSiteChannelHoldTimeGap(int multiSiteChannelHoldTimeGap) {
        MULTI_SITE_CHANNEL_HOLD_TIME_GAP = multiSiteChannelHoldTimeGap;
    }

    public static int getMultiSiteChannelHoldNum() {
        return MULTI_SITE_CHANNEL_HOLD_NUM;
    }

    public static void setMultiSiteChannelHoldNum(int multiSiteChannelHoldNum) {
        MULTI_SITE_CHANNEL_HOLD_NUM = multiSiteChannelHoldNum;
    }
}
