package com.dingsai.dingsaibackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/11/27 22:16
 */
@Data
public class UserLoginByPhoneRequest implements Serializable {
    
    private String telephoneNumber;

    private String verifyCode;
}
