package com.lgmn.swcyapi.service.store;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnPageRequest;
import com.lgmn.swcy.basic.dto.SwcyStoreDto;
import com.lgmn.swcy.basic.entity.SwcyStoreEntity;
import com.lgmn.swcy.basic.service.SwcyStoreService;
import com.lgmn.swcyapi.dto.store.SearchStoreDto;
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
//        LgmnPageRequest lgmnPageRequest = new SwcyStoreDto().getLgmnPageRequest();
//        lgmnPageRequest.setPageNumber(storeDto.getPageNumber());
//        lgmnPageRequest.setPageSize(storeDto.getPageSize());
//        List<LgmnOrder> lgmnOrders = new ArrayList<>();
//        LgmnOrder lgmnOrder = new LgmnOrder();
//        lgmnOrder.setDirection(Sort.Direction.ASC);
//        lgmnOrder.setProperty("juli");
//        lgmnPageRequest.setOrders(lgmnOrders);
        LgmnPageRequest lgmnPageRequest = getLgmnPageRequest(storeDto.getPageNumber(), storeDto.getPageSize());
        return swcyStoreService.getPageStore(storeDto.getLat(), storeDto.getLng(), storeDto.getIndustryId(), lgmnPageRequest);
    }

    public LgmnPage<Map> getPageSearchStore (SearchStoreDto searchStoreDto) {
        LgmnPageRequest lgmnPageRequest = getLgmnPageRequest(searchStoreDto.getPageNumber(), searchStoreDto.getPageSize());
//        LgmnPageRequest lgmnPageRequest = new SwcyStoreDto().getLgmnPageRequest();
//        lgmnPageRequest.setPageNumber(searchStoreDto.getPageNumber());
//        lgmnPageRequest.setPageSize(searchStoreDto.getPageSize());
//        List<LgmnOrder> lgmnOrders = new ArrayList<>();
//        LgmnOrder lgmnOrder = new LgmnOrder();
//        lgmnOrder.setDirection(Sort.Direction.ASC);
//        lgmnOrder.setProperty("juli");
//        lgmnPageRequest.setOrders(lgmnOrders);
        return swcyStoreService.getPageSearchStore(searchStoreDto.getLat(), searchStoreDto.getLng(), searchStoreDto.getName(), lgmnPageRequest);
    }

    private LgmnPageRequest getLgmnPageRequest (int pageNumber, int pageSize) {
        LgmnPageRequest lgmnPageRequest = new SwcyStoreDto().getLgmnPageRequest();
        lgmnPageRequest.setPageNumber(pageNumber);
        lgmnPageRequest.setPageSize(pageSize);
        List<LgmnOrder> lgmnOrders = new ArrayList<>();
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setDirection(Sort.Direction.ASC);
        lgmnOrder.setProperty("juli");
        lgmnPageRequest.setOrders(lgmnOrders);
        return lgmnPageRequest;
    }

    public SwcyStoreEntity getStoreById (Integer id) {
        return swcyStoreService.findById(id);
    }

    public SwcyStoreEntity save(SwcyStoreEntity swcyStoreEntity) {
        return swcyStoreService.saveEntity(swcyStoreEntity);
    }

    public LgmnPage<SwcyStoreEntity> getMyStorePage(String uid, Integer pageNumber, Integer pageSize) throws Exception {
        SwcyStoreDto swcyStoreDto = new SwcyStoreDto();
        swcyStoreDto.setUid(uid);
        swcyStoreDto.setPageNumber(pageNumber);
        swcyStoreDto.setPageSize(pageSize);
        swcyStoreDto.setDelFlag(0);
        LgmnOrder lgmnOrder2 = new LgmnOrder(Sort.Direction.DESC, "storeName");
        LgmnOrder lgmnOrder1 = new LgmnOrder(Sort.Direction.ASC, "createTime");
        List<LgmnOrder> lgmnOrders = new ArrayList<>();
        lgmnOrders.add(lgmnOrder2);
        lgmnOrders.add(lgmnOrder1);
        swcyStoreDto.setOrders(lgmnOrders);
        return swcyStoreService.getPageByDtoWithPageRequet(swcyStoreDto);
    }

    public List<SwcyStoreEntity> getMyStoreListByUid(String uid) throws Exception {
        SwcyStoreDto swcyStoreDto = new SwcyStoreDto();
        swcyStoreDto.setUid(uid);
        return swcyStoreService.getListByDto(swcyStoreDto);
    }

    public List<SwcyStoreEntity> getStoreListByDto (SwcyStoreDto swcyStoreDto) throws Exception {
        return swcyStoreService.getListByDto(swcyStoreDto);
    }
}
