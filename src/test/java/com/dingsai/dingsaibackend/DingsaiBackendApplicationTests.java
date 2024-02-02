package com.dingsai.dingsaibackend;

import com.dingsai.dingsaibackend.model.entity.TOrder;
import com.dingsai.dingsaibackend.service.TOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class DingsaiBackendApplicationTests {
    
    @Resource
    private GridFsTemplate gridFsTemplate;
    
    @Resource
    private TOrderService TOrderService;

    @Test
    void contextLoads() {

        TOrder order = new TOrder();
        order.setDemandUserId(6L);
        order.setDemandTakerId(6L);
        order.setItemId(6L);
        
        
        TOrderService.save(order);
        

    }

}
