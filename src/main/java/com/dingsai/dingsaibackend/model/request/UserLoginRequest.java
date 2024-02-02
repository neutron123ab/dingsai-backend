package com.dingsai.dingsaibackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/11/18 22:11
 */
@Data
public class UserLoginRequest implements Serializable {
    
    private String username;
    
    private String password;
    
}
