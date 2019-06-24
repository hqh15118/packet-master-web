package com.zjucsc.art_decode.opcua;

import com.zjucsc.art_decode.artconfig.OpcuaConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.other.AttackType;
import com.zjucsc.tshark.packets.OpcUaPacket;

import java.util.*;

public class OpcuaDecode extends BaseArtDecode<OpcuaConfig> {

    /**
     * 解析入口方法
     * @param opcuaConfig 自定义的通用配置
     * @param map 全局的参数map，key是参数名，value是参数值
     * @param payload【原始数据/tcp payload】
     * @param objects 其他数据
     * @return 返回map即可
     */
    /** 注意opcUaPacket中的每一个条目均为String[]，注意数组元素的获取，以及类型转换 **/
    /** OpcuaValue类当中的元素是会随着执行而变动的，不要遍历其中的元素，如果有遍历需要，请直接遍历opcuaPacket **/
    /** TODO:opcua.datavalue.mask需要检查，不是所有clienthandle下面都有变量的 **/
    /** TODO:opcua是支持double类型的，现在最终使用float来传输是不是有漏洞？ **/
    /** TODO：如果同一个clienthandle下面的变量类型变了怎么处理？（一般来说不会有这种情况，除非有异常包，或者中间监控有一段时间没在线，目前并不处理，算是一个漏洞） **/

    private class OpcuaMap{
        /** 考虑到opcua创建监控变量的流程特点，构建三个hashmap，形成MonitoredItemId -> Name -> ClientHandle -> Value的链式结构，方便索引与修改 **/
        /** TODO：考虑到ModifyMonitoredItems服务会发送MonitoredItemId与ClientHandle，但不会发送name，想要把ModifyMonitoredItems也作为构建Map的依据可能需要再加入MonitoredItemId -> ClientHandle的对应关系 **/
        private Map<String,Float> Handle_Value_Map = new HashMap<>();
        private Map<String,String> Name_Handle_Map = new HashMap<>();
        private Map<String,String> Id_Name_Map = new HashMap<>();
        //private Map<String,Float> Id_Handle_Map = new HashMap<>();

        private Map<String,ArrayList<String>> Request_Map = new HashMap<>();

        /** 增添、更新变量 **/
        public void MapPut(String Name, String ClientHandle, String Id, float value){

        }

        /** 删除变量 **/
        public void MapDelete(String Id, float value){

        }
    }

    private OpcuaMap opcuaMap = new OpcuaMap();

    private enum OpcUaValueType{
        /** 参数 **/
        opcua_nodeid_string,
        opcua_clienthandle,
        opcua_monitoreditemid,
        opcua_monitoreditemids,

        /** 数值变量 **/
        opcua_boolean,
        opcua_sbyte,
        opcua_byte,
        opcua_int16,
        opcua_int32,
        opcua_int64,
        opcua_uint16,
        opcua_uint32,
        opcua_uint64,
        opcua_float,
        opcua_double
    }

    private class OpcUaValue{
        /** 参数 **/
        private ArrayList<String> opcua_nodeid_string = new ArrayList<>();
        private ArrayList<String> opcua_clienthandle = new ArrayList<>();
        private ArrayList<String> opcua_monitoreditemid = new ArrayList<>();
        private ArrayList<String> opcua_monitoreditemids = new ArrayList<>();

        /** 数值变量 **/
        private ArrayList<String> opcua_boolean = new ArrayList<>();
        private ArrayList<String> opcua_sbyte = new ArrayList<>();
        private ArrayList<String> opcua_byte = new ArrayList<>();
        private ArrayList<String> opcua_int16 = new ArrayList<>();
        private ArrayList<String> opcua_int32 = new ArrayList<>();
        private ArrayList<String> opcua_int64 = new ArrayList<>();
        private ArrayList<String> opcua_uint16 = new ArrayList<>();
        private ArrayList<String> opcua_uint32 = new ArrayList<>();
        private ArrayList<String> opcua_uint64 = new ArrayList<>();
        private ArrayList<String> opcua_float = new ArrayList<>();
        private ArrayList<String> opcua_double = new ArrayList<>();

        public OpcUaValue(OpcUaPacket.LayersBean opcUaPacket){
            /** 参数 **/
            if(opcUaPacket.opcua_nodeid_string != null){
                this.opcua_nodeid_string = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_nodeid_string));
            }
            if(opcUaPacket.opcua_clienthandle != null){
                this.opcua_clienthandle = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_clienthandle));
            }
            if(opcUaPacket.opcua_monitoreditemid != null){
                this.opcua_monitoreditemid = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_monitoreditemid));
            }
            if(opcUaPacket.opcua_monitoreditemids != null){
                this.opcua_monitoreditemids = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_monitoreditemids));
            }

            /** 数值变量 **/
            if(opcUaPacket.opcua_boolean != null){
                this.opcua_boolean = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_boolean));
            }
            if(opcUaPacket.opcua_sbyte != null){
                this.opcua_sbyte = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_sbyte));
            }
            if(opcUaPacket.opcua_byte != null){
                this.opcua_byte = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_byte));
            }
            if(opcUaPacket.opcua_int16 != null){
                this.opcua_int16 = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_int16));
            }
            if(opcUaPacket.opcua_int32 != null){
                this.opcua_int32 = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_int32));
            }
            if(opcUaPacket.opcua_int64 != null){
                this.opcua_int64 = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_int64));
            }
            if(opcUaPacket.opcua_uint16 != null){
                this.opcua_uint16 = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_uint16));
            }
            if(opcUaPacket.opcua_uint32 != null){
                this.opcua_uint32 = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_uint32));
            }
            if(opcUaPacket.opcua_uint64 != null){
                this.opcua_uint64 = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_uint64));
            }
            if(opcUaPacket.opcua_float != null){
                this.opcua_float = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_float));
            }
            if(opcUaPacket.opcua_double != null){
                this.opcua_double = new ArrayList<>(Arrays.asList(opcUaPacket.opcua_double));
            }
        }

        public String getValue(OpcUaValueType type){
            String result = null;

            switch (type){
                /** 参数 **/
                case opcua_nodeid_string:
                    if( !(this.opcua_nodeid_string.isEmpty()) ){
                        result = this.opcua_nodeid_string.get(0);
                        this.opcua_nodeid_string.remove(0);
                    }
                    break;
                case opcua_clienthandle:
                    if( !(this.opcua_clienthandle.isEmpty()) ){
                        result = this.opcua_clienthandle.get(0);
                        this.opcua_clienthandle.remove(0);
                    }
                    break;
                case opcua_monitoreditemid:
                    if( !(this.opcua_monitoreditemid.isEmpty()) ){
                        result = this.opcua_monitoreditemid.get(0);
                        this.opcua_monitoreditemid.remove(0);
                    }
                    break;
                case opcua_monitoreditemids:
                    if( !(this.opcua_monitoreditemids.isEmpty()) ){
                        result = this.opcua_monitoreditemids.get(0);
                        this.opcua_monitoreditemids.remove(0);
                    }
                    break;

                /** 数值变量 **/
                case opcua_boolean:
                    if( !(this.opcua_boolean.isEmpty()) ) {
                        result = this.opcua_boolean.get(0);
                        this.opcua_boolean.remove(0);
                    }
                    break;
                case opcua_sbyte:
                    if( !(this.opcua_sbyte.isEmpty()) ) {
                        result = this.opcua_sbyte.get(0);
                        this.opcua_sbyte.remove(0);
                    }
                    break;
                case opcua_byte:
                    if( !(this.opcua_byte.isEmpty()) ) {
                        result = this.opcua_byte.get(0);
                        this.opcua_byte.remove(0);
                    }
                    break;
                case opcua_int16:
                    if( !(this.opcua_int16.isEmpty()) ) {
                        result = this.opcua_int16.get(0);
                        this.opcua_int16.remove(0);
                    }
                    break;
                case opcua_int32:
                    if( !(this.opcua_int32.isEmpty()) ) {
                        result = this.opcua_int32.get(0);
                        this.opcua_int32.remove(0);
                    }
                    break;
                case opcua_int64:
                    if( !(this.opcua_int64.isEmpty()) ) {
                        result = this.opcua_int64.get(0);
                        this.opcua_int64.remove(0);
                    }
                    break;
                case opcua_uint16:
                    if( !(this.opcua_uint16.isEmpty()) ) {
                        result = this.opcua_uint16.get(0);
                        this.opcua_uint16.remove(0);
                    }
                    break;
                case opcua_uint32:
                    if( !(this.opcua_uint32.isEmpty()) ) {
                        result = this.opcua_uint32.get(0);
                        this.opcua_uint32.remove(0);
                    }
                    break;
                case opcua_uint64:
                    if( !(this.opcua_uint64.isEmpty()) ) {
                        result = this.opcua_uint64.get(0);
                        this.opcua_uint64.remove(0);
                    }
                    break;
                case opcua_float:
                    if( !(this.opcua_float.isEmpty()) ) {
                        result = this.opcua_float.get(0);
                        this.opcua_float.remove(0);
                    }
                    break;
                case opcua_double:
                    if( !(this.opcua_double.isEmpty()) ) {
                        result = this.opcua_double.get(0);
                        this.opcua_double.remove(0);
                    }
                    break;

                default:
                    break;
            }
            return result;
        }
    }

    @Override
    public Map<String, Float> decode(OpcuaConfig opcuaConfig, Map<String, Float> result_map, byte[] payload, Object... objects) {
        OpcUaPacket.LayersBean opcUaPacket = ((OpcUaPacket.LayersBean) objects[0]);
        switch (opcUaPacket.opcua_transport_type[0]) {
            /** 目前仅关注MSG报文，其余类型的报文暂无需求 **/
            case "MSG":
                result_map = MSG_decode(opcUaPacket, result_map);
                return result_map;
            default:
                break;
        }
        return null;
    }

    private Map<String, Float> MSG_decode(OpcUaPacket.LayersBean opcUaPacket, Map<String, Float> result_map){ //TODO
        switch (opcUaPacket.opcua_servicenodeid_numeric[0]){
            /** CreateMonitoredItems **/
            case "751": //request
                decode_CreateMonitoredItemsRequest(opcUaPacket);
                break;
            case "754": //response
                decode_CreateMonitoredItemsResponse(opcUaPacket);
                break;

            /** ModifyMonitoredItems **/
            case "763":
                decode_ModifyMonitoredItemsRequest(opcUaPacket);
                break;
            case "766":
                decode_ModifyMonitoredItemsResponse(opcUaPacket);
                break;

            /** DeleteMonitoredItems **/
            case "781":
                decode_DeleteMonitoredItemsRequest(opcUaPacket);
                break;
            case "784":
                decode_DeleteMonitoredItemsResponse(opcUaPacket);
                break;

            /** Publish **/
            case "829":
                result_map = decode_Publish(opcUaPacket, result_map);
                break;

            default:
                break;
        }
        return result_map;
    }


    /** Service: CreateMonitoredItems **/
    private void decode_CreateMonitoredItemsRequest(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        ArrayList<String> namelist = new ArrayList<>();

        /** 仅仅是遍历一遍opcUaPacket.opcua_nodeid_string，保证次数符合，实际操作的是opcuaValue，没有用到element变量 **/
        for(String element: opcUaPacket.opcua_nodeid_string){
            String name = opcUaValue.getValue(OpcUaValueType.opcua_nodeid_string);
            String clienthandle = opcUaValue.getValue(OpcUaValueType.opcua_clienthandle);
            /** 将变量名与clienthandle对应 **/
            opcuaMap.Name_Handle_Map.put(name, clienthandle);
            /** 将变量名加入namelist，以备加入requestmap **/
            namelist.add(name);
        }
        /** 使用一个map临时记录报文的requesthandle，用来和response对应，对应完成后，map中的这一项应当移除 **/
        /** 在CreateMonitoredItems中，requesthandle与报文中的变量名对应 **/
        //TODO：一个requesthandle下可以对应多个变量名，怎么处理
        opcuaMap.Request_Map.put(opcUaPacket.opcua_request_handle[0], namelist); //opcua_request_handle必定只有一个，所以不放在opcuaValue里面处理
    }

    private void decode_CreateMonitoredItemsResponse(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        ArrayList<String> namelist = new ArrayList<>();

        /** 如果requestmap能找到对应键值，则说明本条response报文有对应的request **/
        if(opcuaMap.Request_Map.containsKey(opcUaPacket.opcua_request_handle[0])){
            namelist = opcuaMap.Request_Map.get(opcUaPacket.opcua_request_handle[0]);
            for(String name: namelist){
                if(opcuaMap.Name_Handle_Map.containsKey(name)){
                    //TODO：需要追加判断条件，判断opcua.StatusCode是否为0
                    //TODO：从这里继续开始工作
                    opcuaMap.Id_Name_Map.put(opcUaValue.getValue(OpcUaValueType.opcua_monitoreditemid), name);
                }
                else {
                    /** 正常来说不会跑到这里 **/
                }
            }
            /** 本条request-response对处理完毕，从map中移除键值 **/
            opcuaMap.Request_Map.remove(opcUaPacket.opcua_request_handle[0]);
        }
    }

    /** Service: ModifyMonitoredItems **/
    private void decode_ModifyMonitoredItemsRequest(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        //TODO
    }

    private void decode_ModifyMonitoredItemsResponse(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        //TODO
    }

    /** Service: DeleteMonitoredItems **/
    private void decode_DeleteMonitoredItemsRequest(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
    }

    private void decode_DeleteMonitoredItemsResponse(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
    }

    /** Service: Publish **/
    private Map<String, Float> decode_Publish(OpcUaPacket.LayersBean opcUaPacket, Map<String, Float> result_map){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        if (opcUaPacket.opcua_clienthandle != null){
            int i = 0;
            int flag_err = 0;
            int has_value; //对应opcUaPacket.opcua_variant_has_value
            float value = 0;

            /** 遍历所有的clienthandle，注意clienthandle不一定与value一一对应 **/
            for(String clienthandle: opcUaPacket.opcua_clienthandle){
                /** TODO：需要检查encoding mask **/
                /** 获取当前clienthandle下的变量值 **/
                has_value = Integer.valueOf((opcUaPacket.opcua_variant_has_value[i]).substring(2),16);
                switch (has_value){
                    /** Boolean **/
                    case 1:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_boolean));
                        break;
                    /** Sbyte **/
                    case 2:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_sbyte));
                        break;
                    /** byte **/
                    case 3:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_byte));
                        break;
                    /** int16 **/
                    case 4:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_int16));
                        break;
                    /** uint16 **/
                    case 5:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_uint16));
                        break;
                    /** int32 **/
                    case 6:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_int32));
                        break;
                    /** uint32 **/
                    case 7:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_uint32));
                        break;
                    /** int64 **/
                    case 8:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_int64));
                        break;
                    /** uint64 **/
                    case 9:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_uint64));
                        break;
                    /** float **/
                    case 10:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_float));
                        break;
                    /** double **/
                    case 11:
                        value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_double));
                        break;
                    /** 不在列表中的类型，不进行解析 **/
                    default:
                        flag_err = 1;
                        break;
                }
                if(flag_err == 0) {
                    /** 填充clienthandle_value_map **/
                    opcuaMap.Handle_Value_Map.put(clienthandle, value);

                    /** 结果 **/
                    result_map.put(clienthandle, value);
                }
                else {
                    flag_err = 0;
                }

                i += 1;
            }
        }
        return result_map;
    }


    /**
     * 写需要解析的协议名字，不清楚的可以查看PACKET_PROTOCOL.java
     * @see com.tonggong.test.PACKET_PROTOCOL
     * @return 协议名称如s7comm
     */
    @Override
    public String protocol() {
        return "opcua";
    }

}
