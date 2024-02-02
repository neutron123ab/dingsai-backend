package com.dingsai.dingsaibackend.service;

import com.dingsai.dingsaibackend.model.dto.TradingItemDTO;
import com.dingsai.dingsaibackend.model.entity.TradingItems;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingsai.dingsaibackend.model.request.UserUploadItemRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author zzs
* @description 针对表【trading_items(交易物品表)】的数据库操作Service
* @createDate 2023-11-18 14:27:08
*/
public interface TradingItemsService extends IService<TradingItems> {
    
    String uploadImage(MultipartFile file);
}
