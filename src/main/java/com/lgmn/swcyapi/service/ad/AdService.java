package com.lgmn.swcyapi.service.ad;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.swcy.basic.dto.SwcyAdDto;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcy.basic.service.SwcyAdService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdService {

    @Reference(version = "${demo.service.version}")
    private SwcyAdService swcyAdService;

    public List<SwcyAdEntity> getAdListByType (Integer type) throws Exception {
        SwcyAdDto swcyAdDto = new SwcyAdDto();
        swcyAdDto.setStatus(1);
        swcyAdDto.setType(type);
        List<LgmnOrder> orders = new ArrayList<>();
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setProperty("createTime");
        lgmnOrder.setDirection(Sort.Direction.DESC);
        swcyAdDto.setOrders(orders);
        return swcyAdService.getListByDto(swcyAdDto);
    }
}
