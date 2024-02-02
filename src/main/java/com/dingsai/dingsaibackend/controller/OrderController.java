package com.dingsai.dingsaibackend.controller;

import com.dingsai.dingsaibackend.common.response.BaseResponse;
import com.dingsai.dingsaibackend.common.response.ResultUtils;
import com.dingsai.dingsaibackend.model.dto.UserDTO;
import com.dingsai.dingsaibackend.service.TOrderService;
import com.dingsai.dingsaibackend.service.TradingOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dingsai.dingsaibackend.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * @author zzs
 * @date 2023/12/2 19:34
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Resource
    private TradingOrderService tradingOrderService;
    
    @PostMapping("/receiveDemand")
    public BaseResponse<Boolean> receiveDemand(Long itemId, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        tradingOrderService.takeDemand(itemId, user.getId());
        
        return ResultUtils.success(true, "用户接单成功");
    }
    
    @PostMapping("/finishDemand")
    public BaseResponse<Boolean> finishDemand(Long itemId, String imageId, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        tradingOrderService.finishDemand(itemId, imageId, user.getId());
        
        return ResultUtils.success(true);
    }
    
    @PostMapping("/purchase")
    public BaseResponse<Boolean> purchase(Long itemId, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        tradingOrderService.purchaseDemand(itemId, user.getId());
        return ResultUtils.success(true);
    }
    
}
