package com.zjucsc.application.system.entity;

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
    @TableId(value = "id" , type = IdType.AUTO)
    private int artConfigId;

    private String tag;

    private Integer offset;

    private Integer length;

    private String meaning;

    @TableField("protocol_id")
    private int protocolId;
    @TableField("min_length")
    private int minLength;

    private String paramType;

    private String srcIp;
    private String dstIp;
    @TableField("fun_code")
    private String funCode;

    @TableField("show_graph")
    private boolean showGraph;

    //工艺参数类型
    @TableField("art_type")
    private int artType;

    //第几位 0-7
    @TableField("bit_bucket")
    @Max(7)
    @Min(0)
    private int bitBucket;
    //参数解析类型
    @TableField("decode_type")
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
