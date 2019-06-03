package com.zjucsc.attack.bean;

import com.zjucsc.tshark.packets.FvDimensionLayer;

public class MultisiteFvDimensionAttackWrapper {
    private FvDimensionLayer layer;

    public MultisiteFvDimensionAttackWrapper(FvDimensionLayer layer) {
        this.layer = layer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultisiteFvDimensionAttackWrapper)) return false;
        MultisiteFvDimensionAttackWrapper that = (MultisiteFvDimensionAttackWrapper) o;
        return layer.ip_src[0].equals(that.layer.ip_src[0]);
    }

    @Override
    public int hashCode() {
        //return layer..hash(layer);
        return layer.ip_src[0].hashCode();
    }
}
