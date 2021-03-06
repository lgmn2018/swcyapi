package com.lgmn.swcyapi.service.address;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.swcy.basic.dto.SwcyReceivingAddressDto;
import com.lgmn.swcy.basic.entity.SwcyReceivingAddressEntity;
import com.lgmn.swcy.basic.service.SwcyReceivingAddressService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SwcyReceivingAddressApiService {
    @Reference(version = "${demo.service.version}")
    private SwcyReceivingAddressService swcyReceivingAddressService;

    public SwcyReceivingAddressEntity saveReceivingAddress(SwcyReceivingAddressEntity swcyReceivingAddressEntity) {
        return swcyReceivingAddressService.saveEntity(swcyReceivingAddressEntity);
    }

    public void deleteReceivingAddressById(Integer id) {
        SwcyReceivingAddressEntity swcyReceivingAddressEntity = swcyReceivingAddressService.findById(id);
        swcyReceivingAddressEntity.setDelFlag(1);
        swcyReceivingAddressService.saveEntity(swcyReceivingAddressEntity);
    }

    public SwcyReceivingAddressEntity updateReceivingAddress(SwcyReceivingAddressEntity swcyReceivingAddressEntity) {
        return swcyReceivingAddressService.saveEntity(swcyReceivingAddressEntity);
    }

    public List<SwcyReceivingAddressEntity> getReceivingAddressListByUId(String uid) throws Exception {
        SwcyReceivingAddressDto swcyReceivingAddressDto = new SwcyReceivingAddressDto();
        swcyReceivingAddressDto.setUid(uid);
        swcyReceivingAddressDto.setDelFlag(0);
        List<LgmnOrder> orderList = new ArrayList<>();
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setDirection(Sort.Direction.DESC);
        lgmnOrder.setProperty("id");
        orderList.add(lgmnOrder);
        swcyReceivingAddressDto.setOrders(orderList);
        return swcyReceivingAddressService.getListByDto(swcyReceivingAddressDto);
    }

    public SwcyReceivingAddressEntity getReceivingAddressById(Integer id) {
        return swcyReceivingAddressService.findById(id);
    }

}
