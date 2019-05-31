package com.zjucsc.application.domain.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 
 *
 * @author hongqianhui
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArtConfig implements Serializable {

    //每一条配置对应唯一一个ID
    private int artConfigId;

    private String tag;

    private Integer offset;

    private Integer length;

    private String meaning;

    private int protocolId;
    private int minLength;

    private String paramType;

    private String srcIp;
    private String dstIp;
    private String funCode;

    private boolean showGraph;

    private int artType;


    private int bitBucket;
    //参数解析类型
    private int decodeType;

    /**********************************
     * 工艺参数类型 开关量 连续量
     **********************************/
    public static final int ART_TYPE_BOOL = 1;
    public static final int ART_TYPE_CONT = 2;

    /***********************************
     * 工艺参数的解析类型 BIT INT FLOAT
     **********************************/
    public static final int DECODE_TYPE_BIT = 1;
    public static final int DECODE_TYPE_FLOAT = 2;
    public static final int DECODE_TYPE_INT = 3;


}
