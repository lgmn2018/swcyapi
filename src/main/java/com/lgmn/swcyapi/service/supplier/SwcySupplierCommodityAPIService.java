package com.lgmn.swcyapi.service.supplier;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcySupplierCommodityDto;
import com.lgmn.swcy.basic.entity.SwcyCommodityEntity;
import com.lgmn.swcy.basic.entity.SwcySupplierCommodityEntity;
import com.lgmn.swcy.basic.service.SwcySupplierCommodityService;
import com.lgmn.swcyapi.dto.order.UnifiedOrderDto;
import com.lgmn.swcyapi.dto.supplier.SupplierUnifiedOrderDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SwcySupplierCommodityAPIService {

    @Reference(version = "${demo.service.version}")
    SwcySupplierCommodityService swcySupplierCommodityService;

    public LgmnPage<SwcySupplierCommodityEntity> getSupplierCommodityPageByCategoryId (Integer categoryId, Integer pageNumber, Integer pageSize) throws Exception {
        SwcySupplierCommodityDto swcySupplierCommodityDto = new SwcySupplierCommodityDto();
        swcySupplierCommodityDto.setStatus(1);
        swcySupplierCommodityDto.setCategoryId(categoryId);
        swcySupplierCommodityDto.setDelFlag(0);
        swcySupplierCommodityDto.setPageNumber(pageNumber);
        swcySupplierCommodityDto.setPageSize(pageSize);
        return swcySupplierCommodityService.getPageByDtoWithPageRequet(swcySupplierCommodityDto);
    }

    public List<SwcySupplierCommodityEntity> getSupplierCommoditysByIds (List<Integer> ids) throws Exception {
        SwcySupplierCommodityDto swcySupplierCommodityDto = new SwcySupplierCommodityDto();
        swcySupplierCommodityDto.setId(ids);
        return swcySupplierCommodityService.getListByDto(swcySupplierCommodityDto);
    }

    public Map<String, Object> getCommodityByUnifiedOrderDto(SupplierUnifiedOrderDto unifiedOrderDto) {
        List<SwcySupplierCommodityEntity> list = new ArrayList<>();
        double sunPrice = 0.0;
        for (Integer id : unifiedOrderDto.getIdAndCount().keySet()) {
            SwcySupplierCommodityEntity swcySupplierCommodityEntity = swcySupplierCommodityService.findById(id);
            list.add(swcySupplierCommodityEntity);
            Integer number = unifiedOrderDto.getIdAndCount().get(id);
            BigDecimal tempPrice = swcySupplierCommodityEntity.getRetailPrice().multiply(new BigDecimal(number));
            sunPrice += tempPrice.doubleValue();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("sunPrice", sunPrice);
        return map;
    }

    public SwcySupplierCommodityEntity getSupplierCommodityById(Integer id) {
        return swcySupplierCommodityService.findById(id);
    }
}
