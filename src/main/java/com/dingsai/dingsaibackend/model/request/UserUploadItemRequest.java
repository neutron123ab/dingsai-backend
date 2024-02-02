package com.dingsai.dingsaibackend.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @author zzs
 * @date 2023/11/20 15:29
 */
@Data
public class UserUploadItemRequest implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 图片id
     */
    private String imageId;
    
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
}
