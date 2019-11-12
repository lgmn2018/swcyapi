package com.lgmn.swcyapi.service.supplier;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcySupplierOrderDetailDto;
import com.lgmn.swcy.basic.entity.SwcySupplierOrderDetailEntity;
import com.lgmn.swcy.basic.service.SwcySupplierOrderDetailService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SwcySupplierOrderDetailAPIService {

    @Reference(version = "${demo.service.version}")
    SwcySupplierOrderDetailService swcySupplierOrderDetailService;

    public List<SwcySupplierOrderDetailEntity> saveSupplierOrderDetails(List<SwcySupplierOrderDetailEntity> swcySupplierOrderDetailEntityList) {
        List<SwcySupplierOrderDetailEntity> list = new ArrayList<>();
        for (SwcySupplierOrderDetailEntity swcySupplierOrderDetailEntity : swcySupplierOrderDetailEntityList) {
            SwcySupplierOrderDetailEntity temp = swcySupplierOrderDetailService.saveEntity(swcySupplierOrderDetailEntity);
            list.add(temp);
        }
        return list;
    }

    public List<SwcySupplierOrderDetailEntity> getSupplierOrderDetailListByOrderId(String orderId) throws Exception {
        SwcySupplierOrderDetailDto swcySupplierOrderDetailDto = new SwcySupplierOrderDetailDto();
        swcySupplierOrderDetailDto.setOrderId(orderId);
        return swcySupplierOrderDetailService.getListByDto(swcySupplierOrderDetailDto);
    }
}
