package com.dingsai.dingsaibackend.service.impl;

import java.io.IOException;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.model.dto.UserDTO;
import com.dingsai.dingsaibackend.model.entity.User;
import com.dingsai.dingsaibackend.model.request.*;
import com.dingsai.dingsaibackend.service.UserService;
import com.dingsai.dingsaibackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dingsai.dingsaibackend.common.constants.UserConstants.USER_TELEPHONE_VERIFY;

/**
 * @author zzs
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-11-18 14:12:09
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private GridFsTemplate gridFsTemplate;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String USERNAME_VALID_REGEX = "^[a-zA-Z0-9]+$";

    private static final int USERNAME_MAX_LEN = 50;

    private static final int PASS_MAX_LEN = 20;

    private static final int PASS_MIN_LEN = 6;

    private static final String PASS_VALID_REGEX = "^[a-zA-Z0-9!@#$%^&*()\\[\\],.<>/?;':`~]+$";

    private static final Pattern USERNAME_PATTERN;

    private static final Pattern PASS_PATTERN;

    static {
        USERNAME_PATTERN = Pattern.compile(USERNAME_VALID_REGEX);
        PASS_PATTERN = Pattern.compile(PASS_VALID_REGEX);
    }

    @Override
    public Boolean userRegister(UserRegisterRequest userRegisterRequest) {
        if (BeanUtil.isEmpty(userRegisterRequest) || BeanUtil.hasNullField(userRegisterRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        if (username.length() > USERNAME_MAX_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过长");
        }
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (password.length() < PASS_MIN_LEN
                || password.length() > PASS_MAX_LEN
                || checkPassword.length() < PASS_MIN_LEN
                || checkPassword.length() > PASS_MAX_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度需要再6~20位");
        }
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        Matcher passMatcher = PASS_PATTERN.matcher(password);
        if (!passMatcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码包含特殊字符");
        }
        String encryptedPassword = SecureUtil.md5(password + username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setAvatarId("defaultAvatar.jpeg");
        user.setTelephoneNumber("");
        user.setRole(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDeleted(0);
        log.info("length: " + encryptedPassword.length());

        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        return true;
    }

    @Override
    public UserDTO userLogin(UserLoginRequest userLoginRequest) {
        if (BeanUtil.isEmpty(userLoginRequest) || BeanUtil.hasNullField(userLoginRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();

        if (username.length() > USERNAME_MAX_LEN || password.length() > PASS_MAX_LEN || password.length() < PASS_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数长度不符合");
        }
        if (!USERNAME_PATTERN.matcher(username).matches() || !PASS_PATTERN.matcher(password).matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "包含特殊字符");
        }
        String encryptedPassword = SecureUtil.md5(password + username);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username).eq(User::getPassword, encryptedPassword);
        User user = this.getOne(userLambdaQueryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        }

        return BeanUtil.copyProperties(user, UserDTO.class);
    }

    @Override
    public UserDTO userLoginByPhone(UserLoginByPhoneRequest userLoginByPhoneRequest) {
        if (BeanUtil.hasNullField(userLoginByPhoneRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String telephoneNumber = userLoginByPhoneRequest.getTelephoneNumber();
        String verifyCode = userLoginByPhoneRequest.getVerifyCode();
        
        if (telephoneNumber.length() > 30
        || verifyCode.length() < 4
        || verifyCode.length() > 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号或密码长度超出限制");
        }
        String realCode = stringRedisTemplate.opsForValue().get(USER_TELEPHONE_VERIFY + telephoneNumber);
        if (realCode == null || !realCode.equals(verifyCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }

        User user = lambdaQuery().eq(User::getTelephoneNumber, telephoneNumber).one();
        if (user == null) {
            return null;
        }

        return BeanUtil.copyProperties(user, UserDTO.class);
    }

    @Override
    public Boolean updateUserInfo(MultipartFile file, UserUpdateRequest userUpdateRequest) {
        if (BeanUtil.hasNullField(userUpdateRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Long userId = userUpdateRequest.getId();
        String username = userUpdateRequest.getUsername();
        String password = userUpdateRequest.getPassword();
        String avatarId = userUpdateRequest.getAvatarId();
        String telephoneNumber = userUpdateRequest.getTelephoneNumber();

        if (username.length() > USERNAME_MAX_LEN
                || password.length() > PASS_MAX_LEN
                || password.length() < PASS_MIN_LEN
                || telephoneNumber.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数长度超出限制");
        }
        if (!USERNAME_PATTERN.matcher(String.valueOf(userId)).matches()
                || !USERNAME_PATTERN.matcher(avatarId).matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数包含特殊字符");
        }
        User user = BeanUtil.copyProperties(userUpdateRequest, User.class);
        
        if (file != null && !file.isEmpty()) {
            // 进行文件上传
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
            String newAvatarName = IdUtil.fastSimpleUUID() + suffix;
            try {
                ObjectId newAvatarId = gridFsTemplate.store(file.getInputStream(), newAvatarName, "");
                //删除原文件
                gridFsTemplate.delete(new Query().addCriteria(Criteria.where("_id").is(avatarId)));
                user.setAvatarId(newAvatarId.toString());
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件操作失败");
            }
        }
        
        return this.save(user);
    }

    @Override
    public UserDTO userUploadInfo(MultipartFile file, UserUploadRequest userUploadRequest) {
        String password = userUploadRequest.getPassword();
        String encryptedPassword = SecureUtil.md5(password + userUploadRequest.getUsername());
        userUploadRequest.setPassword(encryptedPassword);
        User user = BeanUtil.copyProperties(userUploadRequest, User.class);
        
        if (file != null) {
            // 进行文件上传
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
            String newAvatarName = IdUtil.fastSimpleUUID() + suffix;
            try {
                ObjectId newAvatarId = gridFsTemplate.store(file.getInputStream(), newAvatarName, "");
                user.setAvatarId(newAvatarId.toString());
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件操作失败");
            }
        }

        boolean save = save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        
        return BeanUtil.copyProperties(user, UserDTO.class);
    }
}




