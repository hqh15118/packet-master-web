package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class D2DWrapper {
    private int packetNumber;
    private boolean attack;

    public D2DWrapper(int packetNumber, boolean attack) {
        this.packetNumber = packetNumber;
        this.attack = attack;
    }
}
