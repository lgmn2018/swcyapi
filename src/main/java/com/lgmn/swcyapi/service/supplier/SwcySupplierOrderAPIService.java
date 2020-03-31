package com.lgmn.swcyapi.service.supplier;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcySupplierOrderDto;
import com.lgmn.swcy.basic.entity.SwcySupplierOrderEntity;
import com.lgmn.swcy.basic.service.SwcySupplierOrderService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class SwcySupplierOrderAPIService {

    @Reference(version = "${demo.service.version}")
    SwcySupplierOrderService swcySupplierOrderService;

    public LgmnPage<SwcySupplierOrderEntity> getSupplierOrderPageByStoreOwnerId (String storeOwnerId, Integer pageNumber, Integer pageSize) throws Exception {
        SwcySupplierOrderDto swcySupplierOrderDto = getSupplierOrderDto(pageNumber, pageSize);
        swcySupplierOrderDto.setStoreOwnerId(storeOwnerId);
        return swcySupplierOrderService.getPageByDtoWithPageRequet(swcySupplierOrderDto);
    }

    public SwcySupplierOrderEntity getSupplierOrderById (String id) {
        return swcySupplierOrderService.findById(id);
    }

    public SwcySupplierOrderEntity save(SwcySupplierOrderEntity swcySupplierOrderEntity) {
        return swcySupplierOrderService.saveEntity(swcySupplierOrderEntity);
    }

    public LgmnPage<SwcySupplierOrderEntity> getSupplierOrderPageByStoreId(Integer storeId, Integer pageNumber, Integer pageSize) throws Exception {
        SwcySupplierOrderDto swcySupplierOrderDto = getSupplierOrderDto(pageNumber, pageSize);
        swcySupplierOrderDto.setStoreId(storeId);
        return swcySupplierOrderService.getPageByDtoWithPageRequet(swcySupplierOrderDto);
    }

    private SwcySupplierOrderDto getSupplierOrderDto(Integer pageNumber, Integer pageSize) {
        SwcySupplierOrderDto swcySupplierOrderDto = new SwcySupplierOrderDto();
        List<Integer> statuss = new ArrayList<>();
        statuss.add(1);
        statuss.add(2);
        statuss.add(3);
        statuss.add(4);
        statuss.add(5);
        statuss.add(6);
        swcySupplierOrderDto.setStatus(statuss);
        swcySupplierOrderDto.setPageNumber(pageNumber);
        swcySupplierOrderDto.setPageSize(pageSize);
        List<LgmnOrder> lgmnOrderList = new ArrayList<>();
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setProperty("orderTime");
        lgmnOrder.setDirection(Sort.Direction.DESC);
        lgmnOrderList.add(lgmnOrder);
        swcySupplierOrderDto.setOrders(lgmnOrderList);

        return swcySupplierOrderDto;
    }
}
