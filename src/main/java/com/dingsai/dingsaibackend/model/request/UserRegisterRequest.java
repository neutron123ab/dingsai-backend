package com.dingsai.dingsaibackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/11/18 15:48
 */
@Data
public class UserRegisterRequest implements Serializable {
    
    private String username;
    
    private String password;
    
    private String checkPassword;
    
}
