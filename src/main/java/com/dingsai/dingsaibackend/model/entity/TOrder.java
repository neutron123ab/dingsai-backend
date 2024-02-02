package com.dingsai.dingsaibackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @author zzs
 * @TableName order
 */
@TableName(value ="t_order")
@Data
public class TOrder implements Serializable {
    /**
     * 订单id
     */
    @TableId
    private Long id;

    /**
     * 需求方id
     */
    private Long demandUserId;

    /**
     * 接单人id
     */
    private Long demandTakerId;

    /**
     * 需求id
     */
    private Long itemId;

    /**
     * 订单状态（0-未接单 1-已接单 2-订单完成）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 成交价
     */
    private BigDecimal realPrice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}