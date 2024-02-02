package com.dingsai.dingsaibackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/11/30 15:41
 */
@Data
public class UserUploadRequest implements Serializable {
    
    private String username;
    
    private String password;
    
    private String telephoneNumber;
    
}
