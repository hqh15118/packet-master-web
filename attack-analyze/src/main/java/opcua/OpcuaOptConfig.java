package opcua;


import com.zjucsc.attack.base.BaseOptConfig;

import java.util.List;

class jOpcuaOptConfig extends BaseOptConfig {

    private String name; //变量的名称
    private float result;
    private String comment;

    public float isResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getComment() {
        return comment;
    }

}
