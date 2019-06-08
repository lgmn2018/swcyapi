package com.lgmn.swcyapi.service.store;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnPageRequest;
import com.lgmn.swcy.basic.entity.SwcyStoreEntity;
import com.lgmn.swcy.basic.service.SwcyStoreService;
import com.lgmn.swcyapi.dto.store.StoreDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SStoreService {

    @Reference(version = "${demo.service.version}")
    private SwcyStoreService swcyStoreService;

    public LgmnPage<Map> getStoreByIndustryId (StoreDto storeDto) {
        LgmnPageRequest lgmnPageRequest = new StoreDto().getLgmnPageRequest();
        lgmnPageRequest.setPageNumber(storeDto.getPageNumber());
        lgmnPageRequest.setPageSize(storeDto.getPageSize());
        List<LgmnOrder> lgmnOrders = new ArrayList<>();
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setDirection(Sort.Direction.ASC);
        lgmnOrder.setProperty("juli");
        lgmnPageRequest.setOrders(lgmnOrders);
        return swcyStoreService.getPageStore(storeDto.getLat(), storeDto.getLng(), storeDto.getIndustryId(), lgmnPageRequest);
    }

    public SwcyStoreEntity getStoreById (Integer id) {
        return swcyStoreService.findById(id);
    }
}
