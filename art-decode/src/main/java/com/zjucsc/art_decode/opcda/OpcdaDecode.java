package com.zjucsc.art_decode.opcda;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.zjucsc.art_decode.artconfig.OpcdaConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.common.util.Bytecut;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.OpcDaPacket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OpcdaDecode extends BaseArtDecode<OpcdaConfig> {

    private static class OpcdaMap{
        /** 考虑到opcda创建监控变量的流程特点，构建三个hashmap，形成MonitoredItemId -> Name -> ClientHandle -> Value的链式结构，方便索引与修改 **/
        //private Map<String,Float> Handle_Value_Map = new HashMap<>();
        private BiMap<String,String> Handle_Name_Map = HashBiMap.create();
        private BiMap<String,String> Id_Handle_Map = HashBiMap.create();

        /** 英文名称与中文名称对应的map，第一项为英文名称，第二项为中文名称 **/
        private Map<String, String> NameConfigMap = new HashMap<>();

        /** 输出map **/
        private Map<String,Float> Output_Map = new ConcurrentHashMap<>();

        /** Request Map，用于Request与Response的对应 **/
        private Map<String,ArrayList<String>> Request_Map = new HashMap<>();

        /** Name Id Map, for attack detection **/
        private Map<String,String> Id_Name_Map = new HashMap<>();
    }

    private OpcdaMap opcdaMap = new OpcdaMap();

    private byte[] bytesStripZeros(byte[] Bytes){
        int length = Bytes.length;
        byte[] result = new byte[length/2];
        for(int offset=0; offset<length; offset+=2){
            result[offset/2] = Bytes[offset];
        }
        return result;
    }

    private byte[] bytesReverse(byte[] Array) {
        byte[] new_array = new byte[Array.length];
        for (int i = 0; i < Array.length; i++) {
            // 反转后数组的第一个元素等于源数组的最后一个元素：
            new_array[i] = Array[Array.length - i - 1];
        }
        return new_array;
    }

    private int byte2Int(byte[] bytes) {
        return (bytes[0]&0xff)<<24
                | (bytes[1]&0xff)<<16
                | (bytes[2]&0xff)<<8
                | (bytes[3]&0xff);
    }

    private short byte2Short(byte[] bytes) {
        return (short)( (bytes[0]&0xff)<<8 | (bytes[1]&0xff) );
    }


    /** ItemMgt Request **/
    private void decode_ItemMgt_Request(byte[] payload, String payload_String, String call_id){
        ArrayList<String> handlelist = new ArrayList<>();
        int offset = 0;
        offset += 32;//偏移32字节，跳过报文头和UUID
        int value_count = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );//添加或删除的变量数目
        if(value_count == 0){
            return;
            /* 一般来说不会到这里 */
        }
        else {
            offset += 8;//偏移8字节，移出长度字段（长度字段是4byte重复两次）
            int operation_type = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );
            if(operation_type == 0) {
                /** 添加或浏览变量 **/
                if (value_count == 1) {
                    /** 只添加一个变量 **/
                    offset += 6;
                    if (byte2Int(Bytecut.Bytecut(payload, offset, 4)) == 0) {
                        //client handle为0，说明是浏览操作，直接返回，不继续解析
                        return;
                    }
                    //取出client handle，以String类型保存
                    String client_handle = payload_String.substring((offset * 2), (offset * 2 + 8));
                    offset += 4;//移出handle字段
                    offset += 12;
                    //获取对应的名称
                    int name_length = byte2Int(bytesReverse(Bytecut.Bytecut(payload, offset, 4)));
                    String name = "";
                    offset += 12;//偏移3个int
                    if (name_length > 0) {
                        //OPCDA的字符串固定以0x00结尾，长度也包含0x00这一字节，因此截取字符串时，长度需要减1
                        byte[] name_bytes = bytesStripZeros(Bytecut.Bytecut(payload, offset, name_length - 1));
                        name = new String(name_bytes);//TODO:实际上报文中是UTF-8，这里因为只有英文，所以当做ASCII处理了
                        //offset += name_length; //不再继续解析了，因此不再偏移
                        //将 名称-handle 加入map
                        if (!(opcdaMap.Handle_Name_Map.containsValue(name))) {
                            //不记录重复的变量名
                            opcdaMap.Handle_Name_Map.put(client_handle, name);
                            handlelist.add(client_handle);
                        } else {
                            //有重复名称，忽略此次添加
                        }

                        if (!(handlelist.isEmpty())) {
                            /* handlelist不能为空，否则此条request无效，不向RequestMap中写入 */
                            opcdaMap.Request_Map.put(call_id, handlelist);
                        }
                    }
                }
                else {
                    /** 添加多个变量 **/
                    String[] client_handles = new String[value_count];
                    String[] names = new String[value_count];

                    //循环获取client handle
                    for (int i = 0; i < value_count; i++) {
                        offset += 6;
                        //TODO:这里直接默认handle不是0，可能需要改进
                        //取出client handle，以String类型保存
                        client_handles[i] = payload_String.substring((offset * 2), (offset * 2 + 8));
                        offset += 4;//移出handle字段
                        if (i == (value_count - 1)) {
                            offset += 12; //最后一项，偏移12字节
                        } else {
                            offset += 18; //其它项，偏移18字节
                        }
                    }

                    //循环获取name
                    int name_length = 0;
                    for (int i = 0; i < value_count; i++) {
                        name_length = byte2Int(bytesReverse(Bytecut.Bytecut(payload, offset, 4)));
                        names[i] = "";
                        offset += 12;//偏移3个int，移出长度字段
                        if (name_length > 0) {
                            //OPCDA的字符串固定以0x00结尾，长度也包含0x00这一字节，因此截取字符串时，长度需要减1
                            byte[] name_bytes = bytesStripZeros(Bytecut.Bytecut(payload, offset, name_length - 1));
                            names[i] = new String(name_bytes);//TODO:实际上报文中是UTF-8，这里因为只有英文，所以当做ASCII处理了
                            offset += name_length; //移出本段字符串
                        }
                    }

                    //将 名称-handle 加入map
                    for (int i = 0; i < value_count; i++) {
                        if ( !(opcdaMap.Handle_Name_Map.containsValue(names[i])) ) {
                            //不记录重复的变量名
                            opcdaMap.Handle_Name_Map.put(client_handles[i], names[i]);
                            handlelist.add(client_handles[i]);
                        } else {
                            //有重复名称，忽略此次添加
                        }
                    }

                    if (!(handlelist.isEmpty())) {
                        /* handlelist不能为空，否则此条request无效，不向RequestMap中写入 */
                        opcdaMap.Request_Map.put(call_id, handlelist);
                    }
                }
            }

            else {
                /** 删除变量 **/
                String[] server_handles = new String[value_count];
                //String name_to_delete = "";
                String client_handle_to_delete = "";

                for(int i=0; i<value_count; i++){
                    server_handles[i] = payload_String.substring( (offset*2), (offset*2+8) );
                    offset += 4;
                    //将对应的变量移除
                    if(opcdaMap.Id_Handle_Map.containsKey(server_handles[i])){
                       client_handle_to_delete = opcdaMap.Id_Handle_Map.get(server_handles[i]);
                       opcdaMap.Id_Handle_Map.remove(server_handles[i]);
                       if(opcdaMap.Handle_Name_Map.containsKey(client_handle_to_delete)){
                           opcdaMap.Handle_Name_Map.remove(client_handle_to_delete);
                           //OPCDA config的中英文名称对应map的变量移除
                           String name_en = opcdaMap.Handle_Name_Map.get(client_handle_to_delete);
                           if(opcdaMap.NameConfigMap.containsKey(name_en)){
                               String name_cn = opcdaMap.NameConfigMap.get(name_en);
                               opcdaMap.Output_Map.remove(name_cn);
                               //TODO:1.这里没有移除config_map的内容
                               //TODO:2.这里没进行callback，但实际上删除操作也没法callback
                           }
                       }
                    }
                }
            }
        }

    }

    /** ItemMgt Response **/
    private void decode_ItemMgt_Response(byte[] payload, String payload_String, String call_id){
        //目前只解析添加变量操作的返回响应
        int offset = 0;
        ArrayList<String> handlelist;

        /* 如果requestmap能找到对应键值，则说明本条response报文有对应的request */
        if(opcdaMap.Request_Map.containsKey(call_id)) {
            handlelist = opcdaMap.Request_Map.get(call_id);
            offset += 12;//移出报文头部
            int value_count = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );//变量数目
            offset += 4;
            String[] server_handles = new String[value_count];

            for(int i=0; i<value_count; i++) {
                //原则上讲server handle是个int，但是直接取原始字符串作为标记也没差，毕竟server handle是不对外展现的
                server_handles[i] = payload_String.substring( (offset*2), (offset*2+8) );
                offset += 4;//移出handle字段
                offset += 4;//移出Type字段 TODO:这里不解析具体是什么Type，在DataCallBack报文中解析
                offset += 12;//移动到下一个变量
            }

            //put Id-Handle Map
            int i = 0;
            for(String clienthandle: handlelist) {
                opcdaMap.Id_Handle_Map.put(server_handles[i], clienthandle);
                i += 1;
            }
            //处理完毕，移除request map的对应项
            opcdaMap.Request_Map.remove(call_id);
        }
    }

    /** DataCallback **/
    private void decode_DataCallback(byte[] payload, String payload_String, FvDimensionLayer fvDimensionLayer){
        //TODO:只有部分类别的变量会予以更新
        int offset = 0;
        offset += 12;//跳过报头
        offset += 16;//跳过UUID
        offset += 20;//偏移20字节
        int value_count = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );
        offset += 8;//变量数目是重复的两个int

        //获取全部待读取变量的client handle
        String[] client_handles = new String[value_count];
        for(int i=0; i<value_count; i++){
            client_handles[i] = payload_String.substring( (offset*2), (offset*2+8) );
            offset += 4;//移出handle字段
        }
        offset += 4;
        offset += 4*value_count;//TODO:这里是跳过了若干个字符串，但不确定字符串的长度是否固定
        //TODO:此处以后可能需要追加判断条件，保证对齐到数据段开头
        offset += 4;

        int has_value = 0;
        int type = 0;
        int str_len = 0;
        float result = 0;
        byte[] bytes = null;
        int tmp_int = 0;//用于作为无符号整数的中转
        for(int i=0; i<value_count; i++){
            offset += 8;//0x0300 0000 0000 0000
            //type = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );
            type = (Bytecut.Bytecut(payload, offset, 1))[0];
            offset += 12;//type字段总共包含3个int
            switch (type){
                case 0x0:
                    //没有数据字段,直接break，开始下一个
                    break;

                case 0x2:
                    //short (int16)
                    //short也占4个字节，多余的部分用0填充
                    //只取前两个字节，因为后两字节必定为0
                    result = byte2Short( bytesReverse(Bytecut.Bytecut(payload, offset,2)) );
                    offset += 4;
                    has_value = 1;
                    break;

                case 0x3:
                    //long (int32)
                    result = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );
                    offset += 4;
                    has_value = 1;
                    break;

                case 0x4:
                    //float
                    result = Bytecut.BytesTofloat( bytesReverse(Bytecut.Bytecut(payload, offset,4)), 0 );
                    offset += 4;
                    has_value = 1;
                    break;

                case 0x5:
                    //double
                    offset += 4;//首先跳过4个字节，这里是单精度值，且不保证有值
                    //TODO:double赋值
                    offset += 8;
                    has_value = 1;
                    break;

                case 0x7:
                    //date&time
                    offset += 12;
                    break;

                case 0x8:
                    //string
                    str_len = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );
                    offset += 12;//string的长度位为12字节
                    //TODO:暂时不对string类型处理
                    //长度为0的话，后面就没有字符串的内容
                    if(str_len != 0) {
                        //bytes = bytesStripZeros(Bytecut.Bytecut(payload, offset, str_len));
                        offset += 2 * str_len;//string为utf-8编码，每个字符占2字节
                        offset += 2;//string最后为0x0000
                    }
                    break;

                case 0xb:
                    //boolean
                    //注意，bool类型的False为0，True为0xFFFF0000，占4个字节
                    bytes = Bytecut.Bytecut(payload, offset,2);
                    if(bytes[0] == 0){
                        //False
                        result = 0;
                    }
                    else if(bytes[0] == -1){
                        //True
                        result = 1;
                    }
                    else{
                        /* 正常不会跑到这里 */
                        result = 1;//这里将所有不为False的都视为True
                    }
                    offset += 4;
                    has_value = 1;
                    break;

                case 0x10:
                    //char (int8)
                    bytes = Bytecut.Bytecut(payload, offset,1);
                    result = bytes[0];
                    offset += 4;
                    has_value = 1;
                    break;

                case 0x11:
                    //byte (uint8)
                    bytes = Bytecut.Bytecut(payload, offset,1);
                    tmp_int = bytes[0] & 0xFF;
                    result = tmp_int;
                    offset += 4;
                    has_value = 1;
                    break;

                case 0x12:
                    //word (uint16)
                    tmp_int = byte2Int( bytesReverse(Bytecut.Bytecut(payload, offset,4)) );
                    result = tmp_int & 0xFFFF;
                    offset += 4;
                    has_value = 1;
                    break;

                case 0x13:
                    //dword (uint32)
                    //TODO
                    offset += 4;
                    //has_value = 1;
                    break;

                default:
                    //未知的类型
                    break;
            }
            if(has_value == 1){
                //更新数据
                if (opcdaMap.Handle_Name_Map.containsKey(client_handles[i])) {
                    String update_name = opcdaMap.Handle_Name_Map.get(client_handles[i]);
                    updateOutput(update_name, result, fvDimensionLayer);
                }
                else {
                    //TODO:对于这种只有handle和value对应的变量，如何处理比较合适？
                }
            }
            has_value = 0;
        }

    }

    /** 检查配置类中是否存在应当输出的变量，只输出配置类中有对应的变量 **/
    private void updateOutput(String name_en, float value, FvDimensionLayer fvDimensionLayer){
        //配置类中存在与名称相符的中文配置名
        if(opcdaMap.NameConfigMap.containsKey(name_en)){
            String name_cn = opcdaMap.NameConfigMap.get(name_en); //变量对应的中文名称
            opcdaMap.Output_Map.put(name_cn, value); //更新输出Map
            callback(name_cn, value, fvDimensionLayer); //callback function

            String client_handle = opcdaMap.Handle_Name_Map.inverse().get(name_en);
            String server_handle = opcdaMap.Id_Handle_Map.inverse().get(client_handle);
            opcdaMap.Id_Name_Map.put(server_handle, name_cn);
            if( !(opcdaMap.Id_Name_Map.isEmpty()) ) {
                callback("",0f,null,"opcda",opcdaMap.Id_Name_Map);
            }
        }
    }

    @Override
    public Map<String, Float> decode(OpcdaConfig opcdaConfig, Map<String, Float> result_map, byte[] bytes, FvDimensionLayer fvDimensionLayer, Object... objects) {
        OpcDaPacket.LayersBean opcDaPacket = ((OpcDaPacket.LayersBean) fvDimensionLayer);
        byte[] payload = hexStringToByteArray2(opcDaPacket.dcerpc_stub_data[0]);
        opcdaMap.Output_Map = result_map;

        switch (opcDaPacket.dcerpc_pkt_type[0]){
            case "0": //Requset
            case "2": //Response
                switch (opcDaPacket.dcerpc_datype[0]){
                    case "IOPCItemMgt":
                        //增添、删除、浏览变量
                        if(opcDaPacket.dcerpc_pkt_type[0].equals("0")){
                            //Request
                            decode_ItemMgt_Request(payload, opcDaPacket.dcerpc_stub_data[0], opcDaPacket.dcerpc_cn_call_id[0]);
                        }
                        else if(opcDaPacket.dcerpc_pkt_type[0].equals("2")){
                            //Response
                            decode_ItemMgt_Response(payload, opcDaPacket.dcerpc_stub_data[0], opcDaPacket.dcerpc_cn_call_id[0]);
                        }
                        break;
                    case "IOPCDataCallback":
                        //读取数据
                        if(opcDaPacket.dcerpc_pkt_type[0].equals("0")){
                            //Request
                            decode_DataCallback(payload, opcDaPacket.dcerpc_stub_data[0], fvDimensionLayer);
                        }
                        break;
                    /*case "IOPCSyncIO":
                        //同步读写
                        break;
                    case "IOPCAsyncIO2":
                        //异步读写
                        break;*/
                    default:
                        break;
                }
                break;
            default:
                //暂不处理其余类型
                break;
        }

        return opcdaMap.Output_Map;
    }


    /**
     * 写需要解析的协议名字，不清楚的可以查看PACKET_PROTOCOL.java
     */
    @Override
    public String protocol() {
        return "opcda";
    }

    public interface OpcdaArtNameCallback{
        void call(String name);
    }

    private OpcdaArtNameCallback opcdaArtNameCallback;

    public void setOpcdaArtNameCallback(OpcdaArtNameCallback opcdaArtNameCallback){
        this.opcdaArtNameCallback = opcdaArtNameCallback;
    }

    @Override
    public void addArtConfig(OpcdaConfig opcdaConfig) {
        String name_en = opcdaConfig.getName_en();
        String name_cn = opcdaConfig.getName_cn();
        if(opcdaMap.NameConfigMap.containsKey(name_en)){
            //如果名称是已经配置过的，则更新已有的名称
            String name_cn_to_remove = opcdaMap.NameConfigMap.get(name_en); //获取原来配置的中文名称
            if( (opcdaMap.Output_Map.containsKey(name_cn_to_remove)) && !(name_cn.equals(name_cn_to_remove))) {
                float value = opcdaMap.Output_Map.get(name_cn_to_remove); //获取此名称对应的值
                opcdaMap.Output_Map.remove(name_cn_to_remove); //移除旧名称
                opcdaMap.Output_Map.put(name_cn, value); //添加新名称
            }
        }
        opcdaMap.NameConfigMap.put(name_en, name_cn); //更新名称配置对应表 20190726
    }




    /**
     * decode non : string
     * String trailer = "00020d04fc6aa8defba27a10fc6aa8defba27a80";
     *         String fsc = "0x00000075";
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray2(String s) {
        return hexStringToByteArray2(s, 0 );
    }

    public static byte[] hexStringToByteArray2(String s , int offset) {
        int len = s.length();
        if (len == 0){
            return null;
        }
        int byteArraySize = (len - offset) >>> 1;
        byte[] data = new byte[byteArraySize];
        int j = 0;
        for (int i = offset ; i < len; i += 2) {
            data[j] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
            j++;
        }
        return data;
    }
}
