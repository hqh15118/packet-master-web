package com.zjucsc.attack.opname;

import com.zjucsc.attack.bean.BaseOpName;

public class DNP3OpName extends BaseOpName {
    private int category;
    private int objGroup;
    private int index;
    private boolean result;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getObjGroup() {
        return objGroup;
    }

    public void setObjGroup(int objGroup) {
        this.objGroup = objGroup;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
