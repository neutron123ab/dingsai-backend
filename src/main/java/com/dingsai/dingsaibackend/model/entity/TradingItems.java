package com.dingsai.dingsaibackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.dingsai.dingsaibackend.handler.StringSetTypeHandler;
import lombok.Data;

/**
 * 交易物品表
 * @author zzs
 * @TableName trading_items
 */
@TableName(value ="trading_items")
@Data
public class TradingItems implements Serializable {
    /**
     * 商品id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品标题
     */
    private String title;

    /**
     * 类型（海报、手绘、视频）
     */
    private String type;

    /**
     * 需求方预期价格
     */
    private BigDecimal expectPrice;

    /**
     * 设计主题
     */
    private String topic;

    /**
     * 元素要求
     */
    private String elementRequirement;

    /**
     * 备注
     */
    private String remark;

    /**
     * 原始文件id
     */
    private String originalFileId;

    /**
     * 成品文件id
     */
    private String finishedFileId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 当前状态（待接单-0、已接单-1、已交付-2）
     */
    private Integer status;

    /**
     * 逻辑删除(0-未删除，1-已删除)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}