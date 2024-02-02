package com.dingsai.dingsaibackend.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.model.dto.TradingItemDTO;
import com.dingsai.dingsaibackend.model.entity.TOrder;
import com.dingsai.dingsaibackend.model.entity.TradingItems;
import com.dingsai.dingsaibackend.model.entity.User;
import com.dingsai.dingsaibackend.model.request.UserUploadItemRequest;
import com.dingsai.dingsaibackend.service.TOrderService;
import com.dingsai.dingsaibackend.service.TradingItemsService;
import com.dingsai.dingsaibackend.mapper.TradingItemsMapper;
import com.dingsai.dingsaibackend.service.UserService;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzs
 * @description 针对表【trading_items(交易物品表)】的数据库操作Service实现
 * @createDate 2023-11-18 14:27:08
 */
@Service
public class TradingItemsServiceImpl extends ServiceImpl<TradingItemsMapper, TradingItems>
        implements TradingItemsService {

    @Resource
    private GridFsTemplate gridFsTemplate;
    

    @Resource
    private UserService userService;

    

    @Override
    public String uploadImage(MultipartFile file) {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        String newAvatarName = IdUtil.fastSimpleUUID() + suffix;

        String avatarId;
        try {
            avatarId = gridFsTemplate.store(file.getInputStream(), newAvatarName, "").toString();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件操作失败");
        }
        return avatarId;
    }
}




