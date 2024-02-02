package com.dingsai.dingsaibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingsai.dingsaibackend.common.constants.DemandStatus;
import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.model.entity.TOrder;
import com.dingsai.dingsaibackend.model.entity.TradingItems;
import com.dingsai.dingsaibackend.service.TOrderService;
import com.dingsai.dingsaibackend.mapper.TOrderMapper;
import com.dingsai.dingsaibackend.service.TradingItemsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author zzs
* @description 针对表【order(订单表)】的数据库操作Service实现
* @createDate 2023-12-02 15:56:11
*/
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder>
    implements TOrderService {

    

    
    
}




