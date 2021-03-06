package com.lgmn.swcyapi.service.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcyOrderDetailDto;
import com.lgmn.swcy.basic.entity.SwcyOrderDetailEntity;
import com.lgmn.swcy.basic.service.SwcyOrderDetailService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SOrderDetailService {

    @Reference(version = "${demo.service.version}")
    private SwcyOrderDetailService swcyOrderDetailService;

    public List<SwcyOrderDetailEntity> getOrderDetailsByOrderId (String orderId) throws Exception {
        SwcyOrderDetailDto swcyOrderDetailDto = new SwcyOrderDetailDto();
        swcyOrderDetailDto.setOrderId(orderId);
        return swcyOrderDetailService.getListByDto(swcyOrderDetailDto);
    }

    public List<SwcyOrderDetailEntity> saveOrderDetails(List<SwcyOrderDetailEntity> swcyOrderDetailEntitys) {
        List<SwcyOrderDetailEntity> orderDetails = new ArrayList<>();
        for (SwcyOrderDetailEntity item : swcyOrderDetailEntitys) {
            SwcyOrderDetailEntity swcyOrderDetailEntity = save(item);
            orderDetails.add(swcyOrderDetailEntity);
        }
        return orderDetails;
    }

    private SwcyOrderDetailEntity save (SwcyOrderDetailEntity swcyOrderDetailEntity) {
        return swcyOrderDetailService.saveEntity(swcyOrderDetailEntity);
    }
}
