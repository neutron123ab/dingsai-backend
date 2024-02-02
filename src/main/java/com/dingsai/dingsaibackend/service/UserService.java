package com.dingsai.dingsaibackend.service;

import com.dingsai.dingsaibackend.model.dto.UserDTO;
import com.dingsai.dingsaibackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingsai.dingsaibackend.model.request.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author zzs
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-11-18 14:12:09
*/
public interface UserService extends IService<User> {

    Boolean userRegister(UserRegisterRequest userRegisterRequest);
    
    UserDTO userLogin(UserLoginRequest userLoginRequest);
    
    UserDTO userLoginByPhone(UserLoginByPhoneRequest userLoginByPhoneRequest);
    
    Boolean updateUserInfo(MultipartFile file, UserUpdateRequest userUpdateRequest);
    
    UserDTO userUploadInfo(MultipartFile file, UserUploadRequest userUploadRequest);
    
}
