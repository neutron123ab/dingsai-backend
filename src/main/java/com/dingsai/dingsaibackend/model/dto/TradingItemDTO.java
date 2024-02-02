package com.dingsai.dingsaibackend.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zzs
 * @date 2023/12/2 15:59
 */
@Data
public class TradingItemDTO {

    /**
     * 商品id
     */
    private Long id;

    /**
     * 需求方id
     */
    private Long demandUserId;

    /**
     * 需求方名称
     */
    private String demandUsername;

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
     * 当前状态（待接单-0、已接单-1、已交付-2）
     */
    private Integer status;
    
}
