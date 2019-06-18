package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.Data;

import java.util.List;


@Data
public class OpcUaPacket {


    /**
     * timestamp : 1560149163130
     * layers : {"opcua_ClientHandle":["38","39"],"opcua_datavalue_has_value":["1","1"],"opcua_Boolean":["1","1"]}
     */
    private LayersBean layers;

    public LayersBean getLayers() {
        return layers;
    }

    public void setLayers(LayersBean layers) {
        this.layers = layers;
    }

    public static class LayersBean extends FvDimensionLayer  {
        @JSONField(name = "opcua_ClientHandle")
        public List<String> opcua_ClientHandleX;
        @JSONField(name = "opcua_datavalue_has_value")
        public List<String> opcua_datavalue_has_valueX;
        @JSONField(name = "opcua_Boolean")
        public List<String> opcua_BooleanX;

    }
}
