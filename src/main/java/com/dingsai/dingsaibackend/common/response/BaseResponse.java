package com.dingsai.dingsaibackend.common.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zzs
 * @date 2023/3/21 18:49
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 510916505691411117L;
    private final int code;

    private final T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
