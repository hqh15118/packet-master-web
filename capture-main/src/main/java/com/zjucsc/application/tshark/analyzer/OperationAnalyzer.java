package com.zjucsc.application.tshark.analyzer;

import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.tshark.domain.BadPacket;
import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
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
public class OperationAnalyzer extends AbstractAnalyzer<OperationPacketFilter<Integer,String>> {
    @Override
    public Object analyze(Object... objs){
        int fun_code = ((int) objs[0]);
        FvDimensionLayer layer = ((FvDimensionLayer) objs[1]);
        if (!getAnalyzer().getWhiteMap().containsKey(fun_code)){
            try {
                if(fun_code!=-1) {
                    return new BadPacket.Builder(layer.frame_protocols[0])
                            .setComment(attackrec(layer.frame_protocols[0], fun_code))//具体报警信息
                            .set_five_Dimension(layer)
                            .setDangerLevel(DangerLevel.VERY_DANGER)
                            .setFun_code(fun_code)
                            .setOperation(getOperation(layer.frame_protocols[0], fun_code))
                            .build();
                }
                return null;

            } catch (ProtocolIdNotValidException e) {
                log.error("protocol <==> ID not valid " , e);
            }
        }
        return null;
    }

    public OperationAnalyzer(OperationPacketFilter<Integer, String> integerStringPacketFilter) {
        super(integerStringPacketFilter);
    }

    private String getOperation(String protocol , int fun_code) throws ProtocolIdNotValidException {
        String str = CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocol,fun_code);
        return str==null ? "unknown operation" : str;
    }

    @Override
    public String toString() {
        return getAnalyzer().toString();
    }

    private String attackrec(String protocols , int Fun_code)
    {
        if(protocols==null || Fun_code==-1)
        {
            return null;
        }
        switch (protocols)
        {
            case "s7comm": {
                switch (Fun_code) {
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
                switch (Fun_code) {
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
                switch (Fun_code) {
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
}
