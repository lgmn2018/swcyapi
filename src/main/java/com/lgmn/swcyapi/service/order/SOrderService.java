package com.lgmn.swcyapi.service.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcyOrderDto;
import com.lgmn.swcy.basic.entity.SwcyOrderEntity;
import com.lgmn.swcy.basic.service.SwcyOrderService;
import com.lgmn.swcyapi.dto.order.OrderPageDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SOrderService {

    @Reference(version = "${demo.service.version}")
    private SwcyOrderService swcyOrderService;

    public LgmnPage<SwcyOrderEntity> getOrderPage (OrderPageDto orderPageDto, String uid) throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add(uid);
        SwcyOrderDto swcyOrderDto = new SwcyOrderDto();
        swcyOrderDto.setUid(ids);
        swcyOrderDto.setPageNumber(orderPageDto.getPageNumber());
        swcyOrderDto.setPageSize(orderPageDto.getPageSize());
        List<LgmnOrder> lgmnOrderList = new ArrayList<>();
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setProperty("orderTime");
        lgmnOrder.setDirection(Sort.Direction.DESC);
        lgmnOrderList.add(lgmnOrder);
        swcyOrderDto.setOrders(lgmnOrderList);
        return swcyOrderService.getPageByDtoWithPageRequet(swcyOrderDto);
    }

    public SwcyOrderEntity getOrderById (String id) {
        return swcyOrderService.findById(id);
    }

    public List<SwcyOrderEntity> getAllByUid (List<String> uids) throws Exception {
        SwcyOrderDto swcyOrderDto = new SwcyOrderDto();
        swcyOrderDto.setUid(uids);
        return swcyOrderService.getListByDto(swcyOrderDto);
    }
}
