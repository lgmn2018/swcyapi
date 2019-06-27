package com.lgmn.swcyapi.service.message;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcyMessageDto;
import com.lgmn.swcy.basic.entity.SwcyMessageEntity;
import com.lgmn.swcy.basic.service.SwcyMessageService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageService {

    @Reference(version = "${demo.service.version}")
    private SwcyMessageService swcyMessageService;

    public SwcyMessageEntity save (SwcyMessageEntity swcyMessageEntity) {
        return swcyMessageService.saveEntity(swcyMessageEntity);
    }

    public SwcyMessageEntity getMessageById (Integer id) {
        return swcyMessageService.findById(id);
    }

    public LgmnPage<SwcyMessageEntity> getMyMessagePageByUid (String uid, Integer type, Integer pageNumber, Integer pageSize) throws Exception {
        List<LgmnOrder> orders = getOrders();
        SwcyMessageDto swcyMessageDto = new SwcyMessageDto();
        swcyMessageDto.setUid(uid);
        swcyMessageDto.setType(type);
        swcyMessageDto.setPageNumber(pageNumber);
        swcyMessageDto.setPageSize(pageSize);
        swcyMessageDto.setOrders(orders);
        return swcyMessageService.getPageByDtoWithPageRequet(swcyMessageDto);
    }

    public LgmnPage<SwcyMessageEntity> getMessagePage (Integer type, Integer pageNumber, Integer pageSize) throws Exception {
        List<LgmnOrder> orders = getOrders();
        SwcyMessageDto swcyMessageDto = new SwcyMessageDto();
        swcyMessageDto.setType(type);
        swcyMessageDto.setOrders(orders);
        swcyMessageDto.setPageNumber(pageNumber);
        swcyMessageDto.setPageSize(pageSize);
        return swcyMessageService.getPageByDtoWithPageRequet(swcyMessageDto);
    }

    private List<LgmnOrder> getOrders () {
        List<LgmnOrder> orders = new ArrayList<>();
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setDirection(Sort.Direction.DESC);
        lgmnOrder.setProperty("createTime");
        orders.add(lgmnOrder);
        return orders;
    }
}
