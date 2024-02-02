package com.dingsai.dingsaibackend.controller;

import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.BaseResponse;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.common.response.ResultUtils;
import com.dingsai.dingsaibackend.model.dto.UserDTO;
import com.dingsai.dingsaibackend.model.request.*;
import com.dingsai.dingsaibackend.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dingsai.dingsaibackend.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 用户管理
 * @author zzs
 * @date 2023/11/18 15:45
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     * @param userRegisterRequest 注册请求实体
     * @return 是否注册成功
     */
    @PostMapping("/register")
    public BaseResponse<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(userService.userRegister(userRegisterRequest), "注册成功");
    }

    /**
     * 用户登录接口
     * @param userLoginRequest 登录请求实体
     * @return 用户信息
     */
    @PostMapping("/login")
    public BaseResponse<UserDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        UserDTO userDTO = userService.userLogin(userLoginRequest);
        if (userDTO != null) {
            request.getSession().setAttribute(USER_LOGIN_STATE, userDTO);
        }

        return ResultUtils.success(userDTO, "登录成功");
    }
    
    @PostMapping("/loginByPhone")
    public BaseResponse<UserDTO> userLoginByPhone(@RequestBody UserLoginByPhoneRequest userLoginByPhoneRequest, HttpServletRequest request) {
        if (userLoginByPhoneRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        UserDTO userDTO = userService.userLoginByPhone(userLoginByPhoneRequest);
        if (userDTO == null) {
            return ResultUtils.success(null, "验证码登录成功，但首次需要完善用户信息");
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, userDTO);
        return ResultUtils.success(userDTO, "登录成功");
    }

    /**
     * 用户修改个人信息接口
     * @param file 头像
     * @param userUpdateRequest 修改信息实体
     * @return 是否修改成功
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> userUpdate(@RequestParam(value = "file", required = false) MultipartFile file, @RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Boolean isUpdated = userService.updateUserInfo(file, userUpdateRequest);
        if (Boolean.FALSE.equals(isUpdated)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户信息更新失败");
        }
        return ResultUtils.success(true, "用户信息更新成功");
    }
    
    @PostMapping("/uploadUserInfo")
    public BaseResponse<UserDTO> userUpload(@RequestParam("file") MultipartFile file, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("telephoneNumber") String telephoneNumber) {
        UserUploadRequest userUploadRequest = new UserUploadRequest();
        userUploadRequest.setUsername(username);
        userUploadRequest.setPassword(password);
        userUploadRequest.setTelephoneNumber(telephoneNumber);
        
        UserDTO userDTO = userService.userUploadInfo(file, userUploadRequest);
        return ResultUtils.success(userDTO);
    }
    
}
