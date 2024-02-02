package com.dingsai.dingsaibackend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zzs
 * @date 2023/11/18 22:14
 */
@Data
public class UserDTO implements Serializable {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;
    

    /**
     * 用户头像id
     */
    private String avatarId;

    /**
     * 电话号码
     */
    private String telephoneNumber;

    /**
     * 角色（0-普通用户，1-管理员）
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;
    
}
