package com.tonggong.capture;

import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.tshark.packets.FvDimensionLayer;
//import com.zjucsc.art_decode.other.AttackType;

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
    /** TODO:为了处理不同的OPC UA通信进程，可能需要记录源/目标的mac信息，以作为区分标记 **/

    private class OpcuaMap{
        /** 考虑到opcua创建监控变量的流程特点，构建三个hashmap，形成MonitoredItemId -> Name -> ClientHandle -> Value的链式结构，方便索引与修改 **/
        /** TODO：考虑到ModifyMonitoredItems服务会发送MonitoredItemId与ClientHandle，但不会发送name，想要把ModifyMonitoredItems也作为构建Map的依据可能需要再加入MonitoredItemId -> ClientHandle的对应关系 **/
        private Map<String,Float> Handle_Value_Map = new HashMap<>();
        private Map<String,String> Handle_Name_Map = new HashMap<>();
        private Map<String,String> Id_Handle_Map = new HashMap<>();
        //private Map<String,Float> Id_Handle_Map = new HashMap<>();

        /** OutputMap的key是将变量的name和handle拼接，组成一个字符串 **/
        private Map<String,Float> Output_Map = new HashMap<>();

        private Map<String,ArrayList<String>> Request_Map = new HashMap<>();

        /** 增添、更新变量 **/
        public void MapPut(String Name, String ClientHandle, String Id, float value){

        }

        /** 删除变量 **/
        public void MapDelete(String Id){

        }
    }

    private OpcuaMap opcuaMap = new OpcuaMap();

    public enum OpcUaValueType{
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

    public class OpcUaValue{
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

    private void MSG_decode(OpcUaPacket.LayersBean opcUaPacket){
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
                decode_Publish(opcUaPacket);
                break;

            default:
                break;
        }
    }

    /** Service: CreateMonitoredItems **/
    private void decode_CreateMonitoredItemsRequest(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        ArrayList<String> handlelist = new ArrayList<>();

        /** 仅仅是遍历一遍opcUaPacket.opcua_nodeid_string，保证次数符合，实际操作的是opcuaValue，没有用到element变量(其实直接用element给name赋值也行的) **/
        for(String element: opcUaPacket.opcua_nodeid_string){
            String name = opcUaValue.getValue(OpcUaValueType.opcua_nodeid_string);
            String clienthandle = opcUaValue.getValue(OpcUaValueType.opcua_clienthandle);
            /** 将变量名与clienthandle对应 **/
            opcuaMap.Handle_Name_Map.put(clienthandle, name);
            /** 将变量名加入namelist，以备加入requestmap **/
            handlelist.add(clienthandle);
        }
        /** 使用一个map临时记录报文的requesthandle，用来和response对应，对应完成后，map中的这一项应当移除 **/
        /** 在CreateMonitoredItems中，requesthandle与报文中的变量名对应 **/
        opcuaMap.Request_Map.put(opcUaPacket.opcua_request_handle[0], handlelist); //opcua_request_handle必定只有一个，所以不放在opcuaValue里面处理
    }

    private void decode_CreateMonitoredItemsResponse(OpcUaPacket.LayersBean opcUaPacket){
        int i = 0;
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        ArrayList<String> handlelist = new ArrayList<>();

        /** 如果requestmap能找到对应键值，则说明本条response报文有对应的request **/
        if(opcuaMap.Request_Map.containsKey(opcUaPacket.opcua_request_handle[0])){
            handlelist = opcuaMap.Request_Map.get(opcUaPacket.opcua_request_handle[0]);
            for(String handle: handlelist){
                if(opcuaMap.Handle_Name_Map.containsKey(handle)){
                    int statuscode = Integer.valueOf((opcUaPacket.opcua_StatusCode[i]).substring(2));
                    if(statuscode == 0){
                        /** statuscode为0，说明操作成功 **/
                        opcuaMap.Id_Handle_Map.put(opcUaValue.getValue(OpcUaValueType.opcua_monitoreditemid), handle);
                    }
                    else {
                        /** statuscode为其它值，说明出现错误。将map中的对应项移除 **/
                        opcuaMap.Handle_Name_Map.remove(handle);
                    }
                    i += 1;
                }
                else {
                    /** 正常来说不会跑到这里 **/
                }
            }
            /** 本条request-response对处理完毕，从map中移除键值 **/
            opcuaMap.Request_Map.remove(opcUaPacket.opcua_request_handle[0]);
            /** 在这里不需要将结果加入OutputMap，只需要在HandleNameMap记录此名称是存在的，在解析Publish时，即可更新OutputMap **/
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
        //OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        ArrayList<String> Idlist = new ArrayList<>();

        for(String Id_to_delete: opcUaPacket.opcua_monitoreditemids){
            Idlist.add(Id_to_delete);
        }
        opcuaMap.Request_Map.put(opcUaPacket.opcua_request_handle[0], Idlist);
    }

    private void decode_DeleteMonitoredItemsResponse(OpcUaPacket.LayersBean opcUaPacket){
        //OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        ArrayList<String> Idlist = new ArrayList<>();

        if(opcuaMap.Request_Map.containsKey(opcUaPacket.opcua_request_handle[0])){
            Idlist = opcuaMap.Request_Map.get(opcUaPacket.opcua_request_handle[0]);
            /** 遍历Id列表，将Id所对应的Id->Name->Handle->Value全部移除 **/
            for(String Id: Idlist){
                if(opcuaMap.Id_Handle_Map.containsKey(Id)){
                    String Id_to_delete = Id; //这个赋值只是为了名字统一
                    String Handle_to_delete = opcuaMap.Id_Handle_Map.get(Id_to_delete);
                    if(opcuaMap.Handle_Name_Map.containsKey(Handle_to_delete)){
                        String name = opcuaMap.Handle_Name_Map.get(Handle_to_delete);
                        String Name_to_delete = name + "@" + Handle_to_delete;
                        /** 根据结果更新OutputMap，此处必须移除OutputMap的对应key，否则应当移除的key会一直存在 **/
                        if(opcuaMap.Output_Map.containsKey(Name_to_delete)){
                            opcuaMap.Output_Map.remove(Name_to_delete);
                        }
                        opcuaMap.Handle_Name_Map.remove(Handle_to_delete);
                        opcuaMap.Handle_Value_Map.remove(Handle_to_delete);
                    }
                    opcuaMap.Id_Handle_Map.remove(Id_to_delete);
                }

            }
            /** 遍历结束，将requestmap的对应项移除 **/
            opcuaMap.Request_Map.remove(opcUaPacket.opcua_request_handle[0]);
        }

    }

    /** Service: Publish **/
    private void decode_Publish(OpcUaPacket.LayersBean opcUaPacket){
        OpcUaValue opcUaValue = new OpcUaValue(opcUaPacket);
        if (opcUaPacket.opcua_clienthandle != null){
            int i = 0;
            int flag_err = 0;
            int has_value; //对应opcUaPacket.opcua_variant_has_value
            int mask; //opcUaPacket.opcua_datavalue_mask
            int has_data; //opcUaPacket.opcua_datavalue_mask的最后1bit
            float value = 0;

            /** 遍历所有的clienthandle，注意clienthandle不一定与value一一对应 **/
            for(String clienthandle: opcUaPacket.opcua_clienthandle){
                /** 检查encoding mask，只有末位bit为1时才继续提取数据 **/
                mask = Integer.valueOf((opcUaPacket.opcua_datavalue_mask[i]).substring(2), 16);
                has_data = mask & 0x01;
                if(has_data == 1) {
                    /** 获取当前clienthandle下的变量值 **/
                    has_value = Integer.valueOf((opcUaPacket.opcua_variant_has_value[i]).substring(2), 16);
                    switch (has_value) {
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
                    if (flag_err == 0) {
                        /** 填充clienthandle_value_map **/
                        opcuaMap.Handle_Value_Map.put(clienthandle, value);

                        /** 根据结果更新OutputMap **/
                        if (opcuaMap.Handle_Name_Map.containsKey(clienthandle)) {
                            String name = opcuaMap.Handle_Name_Map.get(clienthandle);
                            String Name_and_Handle = name + "@" + clienthandle; //变量名均以 名字@Handle 的形式存在。
                            opcuaMap.Output_Map.put(Name_and_Handle, value);
                        }
                        else {
                            //TODO:对于这种只有handle和value对应的变量，如何处理比较合适？
                            //System.out.println("警告：监控变量没有对应的变量名");
                            opcuaMap.Output_Map.put("clienthandle=" + clienthandle, value);
                        }
                    }
                    else {
                        flag_err = 0;
                    }
                }
                else{
                    /** TODO：如果跑到这里，说明该clienthandle下没有数据，这种情况一般是由于OPC Client读取数据出错（例如由于网络断开等），这里是否追加后续处理有待考虑 **/
                }
                i += 1;
            }
        }
    }


    @Override
    public Map<String, Float> decode(OpcuaConfig opcuaConfig, Map<String, Float> result_map, byte[] bytes, FvDimensionLayer fvDimensionLayer, Object... objects) {
        OpcUaPacket.LayersBean opcUaPacket = ((OpcUaPacket.LayersBean) objects[0]);
        opcuaMap.Output_Map = result_map;
        callback("水位1",10.0f,fvDimensionLayer);

        switch (opcUaPacket.opcua_transport_type[0]) {
            /** 目前仅关注MSG报文，其余类型的报文暂无需求 **/
            case "MSG":
                MSG_decode(opcUaPacket);
/*
                System.out.println("Id_Handle_map");
                for(String key: opcuaMap.Id_Handle_Map.keySet()){
                    System.out.println(key);
                    System.out.println(opcuaMap.Id_Handle_Map.get(key));
                }
                System.out.println("Handle_Name_Map");
                for(String key: opcuaMap.Handle_Name_Map.keySet()){
                    System.out.println(key);
                    System.out.println(opcuaMap.Handle_Name_Map.get(key));
                }
                System.out.println("Output_Map");
                for(String key: opcuaMap.Output_Map.keySet()){
                    System.out.println(key);
                    System.out.println(opcuaMap.Output_Map.get(key));
                }

*/
                return opcuaMap.Output_Map;
            default:
                break;
        }
        return null;
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

//    @Override
//    public List<AttackType> attackDecode(List<AttackType> list, byte[] bytes, Object... objects) {
//        return null;
//    }
}
