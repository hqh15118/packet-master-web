package com.zjucsc.tshark.packets;

public class PnioPacket {

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

    public static class LayersBean extends FvDimensionLayer {
    }
}
