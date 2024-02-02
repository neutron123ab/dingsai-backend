package com.dingsai.dingsaibackend.controller;

import com.dingsai.dingsaibackend.common.exception.BusinessException;
import com.dingsai.dingsaibackend.common.response.BaseResponse;
import com.dingsai.dingsaibackend.common.response.ErrorCode;
import com.dingsai.dingsaibackend.common.response.ResultUtils;
import com.dingsai.dingsaibackend.model.dto.TradingItemDTO;
import com.dingsai.dingsaibackend.model.dto.UserDTO;
import com.dingsai.dingsaibackend.model.request.UserUploadItemRequest;
import com.dingsai.dingsaibackend.service.TradingItemsService;
import com.dingsai.dingsaibackend.service.TradingOrderService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.dingsai.dingsaibackend.common.constants.UserConstants.USER_LOGIN_STATE;

/**
 * @author zzs
 * @date 2023/11/20 15:38
 */
@RestController
@RequestMapping("/item")
public class TradingItemController {
    
    @Resource
    private TradingItemsService tradingItemsService;
    
    @Resource
    private TradingOrderService tradingOrderService;
    
    @Resource
    private GridFSBucket gridFSBucket;
    
    @Resource
    private GridFsTemplate gridFsTemplate;
    
    @PostMapping("/upload")
    public BaseResponse<Boolean> upload(@RequestBody UserUploadItemRequest userUploadItemRequest, HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getSession().getAttribute(USER_LOGIN_STATE);
        Boolean upload = tradingOrderService.userUploadItem(userUploadItemRequest, user.getId());
        if (!upload) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        return ResultUtils.success(true);
    }
    
    @PostMapping("/uploadImage")
    public BaseResponse<String> uploadImage(@RequestParam MultipartFile file) {
        String imageId = tradingItemsService.uploadImage(file);
        return ResultUtils.success(imageId);
    }
    
    @GetMapping("/getItems")
    public BaseResponse<List<TradingItemDTO>> getItems() {
        List<TradingItemDTO> list = tradingOrderService.getItemList();
        return ResultUtils.success(list);
    }
    
    @GetMapping("/getImage")
    public void getPic(String pictureId, HttpServletResponse response) {
        //根据文件id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(pictureId)));

        //打开一个下载流对象
        assert gridFSFile != null;
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridFsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = gridFsResource.getInputStream();
            response.setContentType("image/png");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
