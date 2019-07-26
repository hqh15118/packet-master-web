package com.zjucsc.attack.opcua;

import com.zjucsc.attack.bean.BaseOptAnalyzer;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.ArtAttackAnalyzeTask;
<<<<<<< HEAD
=======
import com.zjucsc.attack.config.OpcuaOptConfig;
>>>>>>> 34e966802296a0ab102185b90c825e77c32ecf06
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.OpcUaPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class OpcuaOptAnalyzer extends BaseOptAnalyzer<OpcuaOptConfig> {

    public AttackBean attackdecode(OpcUaPacket.LayersBean layer, Map<String,Float> techmap, OpcuaOptConfig OpcuaOptConfig)
    {
        if(ArtAttackAnalyzeTask.attackDecode(OpcuaOptConfig.getExpression(),techmap,"1")==null)
        {
            return null;
        }
        else if(ArtAttackAnalyzeTask.attackDecode(OpcuaOptConfig.getExpression(),techmap,"1").equals("配置错误"))
        {
//            String error = "";
//            for (String x:OpcuaOptConfig.getExpression())
//            {
//                error = error.concat(x);
//            }
//            return new AttackBean.Builder().attackType("配置错误:".concat(error) ).fvDimension(layer).attackInfo("").build();
            return null;
        }
        else if(ArtAttackAnalyzeTask.attackDecode(OpcuaOptConfig.getExpression(),techmap,"1").equals("1"))
        {
            if(operationdecode(layer, OpcuaOptConfig))
            {
                return new AttackBean.Builder().attackType("工艺操作异常").fvDimension(layer).attackInfo(OpcuaOptConfig.getComment()).build();
            }
        }
        return null;
    }

    private boolean operationdecode(OpcUaPacket.LayersBean layer, OpcuaOptConfig opcuaOptConfig)
    {
        if(layer==null || opcuaOptConfig==null)
        {
            return false;
        }
        else {
            OpcUaValue opcUaValue = new OpcUaValue(layer);
            int has_value;
            int mask;
            int has_data;
            int i = 0;
            float value = 0;

            switch (layer.opcua_servicenodeid_numeric[0]){
                /* Write */
                /* 不检查Write Request操作对应的Response（676）*/
                case "673":
                    for(String name: layer.opcua_nodeid_string) {
                        String name_target = opcuaOptConfig.getName();
                        /* 检查encoding mask，只有末位bit为1时才继续提取数据 */
                        mask = Integer.valueOf((layer.opcua_datavalue_mask[i]).substring(2), 16);
                        has_data = mask & 0x01;
                        if(has_data == 1) {
                            has_value = Integer.valueOf((layer.opcua_variant_has_value[i]).substring(2), 16);
                            switch (has_value) {
                                /* Boolean */
                                case 1:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_boolean));
                                    break;
                                /* Sbyte */
                                case 2:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_sbyte));
                                    break;
                                /* byte */
                                case 3:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_byte));
                                    break;
                                /* int16 */
                                case 4:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_int16));
                                    break;
                                /* uint16 */
                                case 5:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_uint16));
                                    break;
                                /* int32 */
                                case 6:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_int32));
                                    break;
                                /* uint32 */
                                case 7:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_uint32));
                                    break;
                                /* int64 */
                                case 8:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_int64));
                                    break;
                                /* uint64 */
                                case 9:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_uint64));
                                    break;
                                /* float */
                                case 10:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_float));
                                    break;
                                /* double */
                                case 11:
                                    value = Float.parseFloat(opcUaValue.getValue(OpcUaValueType.opcua_double));
                                    break;
                                /* 不在列表中的类型，不进行解析 */
                                default:
                                    break;
                            }
                            /* 检查写入的数值是否与规则中的相匹配 */
                            if((name == name_target) && (opcuaOptConfig.isResult() == value)) {
                                /* 变量名称、结果均匹配，返回true */
                                return true;
                            }
                            else {
                                /* 结果不匹配，继续处理下一个变量，请不要在这里返回false */
                            }
                        }
                        else {
                            /* 如果跑到这里，说明该变量下没有数据，继续处理下一个变量 */
                        }
                        i += 1;
                    }
                    break;

                default:
                    break;
            }
        }
        return false;
    }

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
    public AttackBean doAnalyze(FvDimensionLayer layer, Map<String,Float> techmap, OpcuaOptConfig OpcuaOptConfig, Object... objs) {
        OpcUaPacket.LayersBean opcuapacket = (OpcUaPacket.LayersBean) layer;
        return attackdecode(opcuapacket, techmap, OpcuaOptConfig);
    }
}
