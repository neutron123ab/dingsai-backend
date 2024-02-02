package com.dingsai.dingsaibackend.common.exception;

import com.dingsai.dingsaibackend.common.response.ErrorCode;
import lombok.Getter;

/**
 * @author zzs
 * @date 2023/3/21 20:54
 */
@Getter
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 3420303773385435232L;
    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description){
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
