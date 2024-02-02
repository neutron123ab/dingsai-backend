package com.dingsai.dingsaibackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.dingsai.dingsaibackend.common.constants.DemandStatus;
import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.model.dto.TradingItemDTO;
import com.dingsai.dingsaibackend.model.entity.TOrder;
import com.dingsai.dingsaibackend.model.entity.TradingItems;
import com.dingsai.dingsaibackend.model.entity.User;
import com.dingsai.dingsaibackend.model.request.UserUploadItemRequest;
import com.dingsai.dingsaibackend.service.TOrderService;
import com.dingsai.dingsaibackend.service.TradingItemsService;
import com.dingsai.dingsaibackend.service.TradingOrderService;
import com.dingsai.dingsaibackend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzs
 * @date 2023/12/3 0:56
 */
@Service
public class TradingOrderServiceImpl implements TradingOrderService {
    
    @Resource
    private TradingItemsService tradingItemsService;
    
    @Resource
    private TOrderService tOrderService;
    
    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void takeDemand(Long itemId, Long userId) {

        boolean update = tOrderService.lambdaUpdate().eq(TOrder::getItemId, itemId)
                .set(TOrder::getDemandTakerId, userId)
                .set(TOrder::getStatus, DemandStatus.DEMAND_RECEIVED)
                .update();
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户接单失败");
        }
        boolean tradingItemsUpdate = tradingItemsService.lambdaUpdate()
                .eq(TradingItems::getId, itemId)
                .set(TradingItems::getStatus, DemandStatus.DEMAND_RECEIVED)
                .update();
        if (!tradingItemsUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户接单失败");
        }
    }

    @Override
    public void finishDemand(Long itemId, String imageId, Long userId) {
        boolean orderUpdate = tOrderService.lambdaUpdate().eq(TOrder::getItemId, itemId)
                .eq(TOrder::getDemandTakerId, userId)
                .set(TOrder::getStatus, DemandStatus.DEMAND_FINISHED)
                .update();
        if (!orderUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新订单表失败");
        }
        boolean tradingItemsUpdate = tradingItemsService.lambdaUpdate()
                .eq(TradingItems::getId, itemId)
                .set(TradingItems::getFinishedFileId, imageId)
                .set(TradingItems::getStatus, DemandStatus.DEMAND_FINISHED)
                .update();
        if (!tradingItemsUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "需求表更新失败");
        }
    }

    @Override
    public void purchaseDemand(Long itemId, Long userId) {
        boolean orderUpdate = tOrderService.lambdaUpdate().eq(TOrder::getItemId, itemId)
                .eq(TOrder::getDemandUserId, userId)
                .set(TOrder::getStatus, DemandStatus.DEMAND_PURCHASED)
                .update();
        if (!orderUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "订单表更新失败");
        }

        boolean tradingItemsUpdate = tradingItemsService.lambdaUpdate()
                .eq(TradingItems::getId, itemId)
                .set(TradingItems::getStatus, DemandStatus.DEMAND_PURCHASED)
                .update();
        if (!tradingItemsUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "需求表更新失败");
        }
    }

    @Override
    public Boolean userUploadItem(UserUploadItemRequest userUploadItemRequest, Long userId) {
        TradingItems tradingItems = BeanUtil.copyProperties(userUploadItemRequest, TradingItems.class);
        tradingItems.setOriginalFileId(userUploadItemRequest.getImageId());
        boolean save = tradingItemsService.save(tradingItems);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        TOrder order = new TOrder();
        order.setDemandUserId(userId);
        order.setDemandTakerId(userId);
        order.setItemId(tradingItems.getId());
        order.setRealPrice(userUploadItemRequest.getExpectPrice());
        boolean orderSave = tOrderService.save(order);
        if (!orderSave) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public List<TradingItemDTO> getItemList() {

        return tradingItemsService.list().stream()
                .map(tradingItems -> {
                    TOrder order = tOrderService.lambdaQuery().eq(TOrder::getItemId, tradingItems.getId()).one();
                    TradingItemDTO tradingItemDTO = BeanUtil.copyProperties(tradingItems, TradingItemDTO.class);
                    tradingItemDTO.setDemandUserId(order.getDemandUserId());

                    User user = userService.lambdaQuery().eq(User::getId, order.getDemandUserId()).one();
                    tradingItemDTO.setDemandUsername(user.getUsername());
                    return tradingItemDTO;
                })
                .collect(Collectors.toList());
    }
    
}
