package com.dingsai.dingsaibackend.service;

import com.dingsai.dingsaibackend.model.dto.TradingItemDTO;
import com.dingsai.dingsaibackend.model.request.UserUploadItemRequest;

import java.util.List;

/**
 * @author zzs
 * @date 2023/12/3 0:56
 */
public interface TradingOrderService {

    void takeDemand(Long itemId, Long userId);

    void finishDemand(Long itemId, String imageId, Long userId);

    void purchaseDemand(Long itemId, Long userId);

    Boolean userUploadItem(UserUploadItemRequest userUploadItemRequest, Long userId);

    List<TradingItemDTO> getItemList();
}
