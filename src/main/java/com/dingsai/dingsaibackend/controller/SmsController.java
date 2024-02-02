package com.dingsai.dingsaibackend.controller;

import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.BaseResponse;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.common.response.ResultUtils;
import com.dingsai.dingsaibackend.service.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zzs
 * @date 2023/11/27 22:40
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    
    @Resource
    private SmsService smsService;
    
    @GetMapping("/send")
    public BaseResponse<String> sendMsg(String telephoneNumber) {
        if (telephoneNumber == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String verifyCode = smsService.sendMsg(telephoneNumber);
        if (verifyCode == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(verifyCode, "验证码发送成功");
    }
    
}
