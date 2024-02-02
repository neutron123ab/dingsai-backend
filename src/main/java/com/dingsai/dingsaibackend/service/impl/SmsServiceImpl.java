package com.dingsai.dingsaibackend.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.tea.TeaException;
import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.service.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.dingsai.dingsaibackend.common.constants.UserConstants.USER_TELEPHONE_VERIFY;

/**
 * @author zzs
 * @date 2023/11/27 22:22
 */
@Service
public class SmsServiceImpl implements SmsService {
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 发送短信
     * @param telephoneNumber 用户电话号码
     * @return 发送成功则返回验证码
     */
    @Override
    public String sendMsg(String telephoneNumber) {

        // TODO: 加锁使下面的代码对同一个用户一分钟只能执行一次
        
        // lock.tryLock()

        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "dysmsapi.aliyuncs.com";

        Client client;
        try {
            client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String verifyCode = RandomUtil.randomNumbers(6);
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(telephoneNumber)
                .setSignName("叮赛校园设计平台")
                .setTemplateCode("SMS_464105629")
                .setTemplateParam("{\"code\":\"" + verifyCode + "\"}");

        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, new com.aliyun.teautil.models.RuntimeOptions());
            //发送成功则将验证码存入redis中，过期时间五分钟
            stringRedisTemplate.opsForValue().set(USER_TELEPHONE_VERIFY + telephoneNumber, verifyCode, 5, TimeUnit.MINUTES);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "验证码发送失败");
        }

        return verifyCode;
    }
}
