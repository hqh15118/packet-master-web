package com.zjucsc.application.tshark.analyzer;

import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.AttackTypePro;
import com.zjucsc.tshark.analyzer.AbstractAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:49
 */

@Slf4j
//OperationPacketFilter<Integer,String>   --->    <fun_code , fun_code_meaning>
public class OperationAnalyzer extends AbstractAnalyzer<OperationPacketFilter<String,String>> {
    @Override
    public Object analyze(Object... objs){
        String fun_code = ((String) objs[0]);
        FvDimensionLayer layer = ((FvDimensionLayer) objs[1]);
        String protocol = (String)objs[2];
        if (!getAnalyzer().getWhiteMap().containsKey(fun_code)){
            return new AttackBean.Builder()
                    .attackInfo(attackrec(protocol,fun_code))
                    .attackType(AttackTypePro.VISIT_COMMAND)
                    .fvDimension(layer)
                    .build();
        }
        return null;
    }

    private String attackrec(String protocols , String Fun_code)
    {
        if(protocols==null || Fun_code.equals("--"))
        {
            return null;
        }
        int funCode = Integer.decode(Fun_code);
        switch (protocols)
        {
            case "s7comm": {
                switch (funCode) {
                    case 0x04:
                        return "数据篡改攻击!";
                    case 0x05:
                        return "非法读取数据!";
                    case 0x1a:
                    case 0x1b:
                        return "代码篡改攻击";
                    case 0x1c:
                        return "代码异常攻击";
                    case 0x1d:
                    case 0x1e:
                    case 0x1f:
                        return "非法获取控制代码!";
                    case 0x28:
                    case 0x29:
                        return "配置篡改攻击!";
                    default:
                        return "非法功能码未知操作";
                }
            }
            case "s7comm_user_data":
            {
                switch (funCode) {
                    case 0xf:
                        return "代码篡改攻击!";
                    case 0x1:
                    case 0x0:
                        return "配置篡改攻击!";
                    case 0x4:
                        return "非法访问CPU!";
                    case 0x2:
                        return "非法读取数据!";
                    default:
                        return null;
                }
            }
            case "modbus":
            {
                switch (funCode) {
                    case 43:
                        return "嗅探攻击";
                    case 5:
                    case 15:
                    case 6:
                    case 16:
                    case 23:
                        return "数据篡改攻击!";
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        return "非法读取数据!";
                    case 22:
                        return "配置篡改攻击!";
                    case 20:
                    case 21:
                        return "非法访问文件记录";
                    default:
                        return "非法功能码未知操作";
                }
            }
            default:
                return "未知攻击";
        }
    }

    public OperationAnalyzer(OperationPacketFilter<String, String> integerStringPacketFilter) {
        super(integerStringPacketFilter);
    }

    @Override
    public String toString() {
        return getAnalyzer().toString();
    }
}
