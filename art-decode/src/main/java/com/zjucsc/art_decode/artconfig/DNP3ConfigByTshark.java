package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

public class DNP3ConfigByTshark extends BaseConfig {
    //dnp3.al.obj:
    // 0a02(2562)read binary output
    // 1e02(7682)analog input
    // (0102)258 binary input #
    // (0c01)3073 control code
    /**
     * config
     * 1.binary_output
     * 2.binary_input
     * 3.analog_input
     */
    private String objAndVar;

    private String pointIndex;
    public String getObjAndVar() {
        return objAndVar;
    }

    public void setObjAndVar(String objAndVar) {
        this.objAndVar = objAndVar;
    }

    public String getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(String pointIndex) {
        this.pointIndex = pointIndex;
    }
}
