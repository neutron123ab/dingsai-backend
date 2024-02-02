package com.dingsai.dingsaibackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/11/18 23:07
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户原始头像id
     */
    private String avatarId;

    /**
     * 电话号码
     */
    private String telephoneNumber;
    
}
