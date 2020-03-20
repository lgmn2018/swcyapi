package com.lgmn.swcyapi.service.commodity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcyCommodityDto;
import com.lgmn.swcy.basic.entity.SwcyCommodityEntity;
import com.lgmn.swcy.basic.service.SwcyCommodityService;
import com.lgmn.swcyapi.dto.order.UnifiedOrderDto;
import com.lgmn.swcyapi.dto.store.CommodityNewestPriceDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SwcyCommodityApiService {
    @Reference(version = "${demo.service.version}")
    private SwcyCommodityService swcyCommodityService;

    public LgmnPage<SwcyCommodityEntity> getCommodityPageByCommodityTypeId(Integer commodityTypeId, Integer pageNumber, Integer pageSize, Boolean isAdmin) throws Exception {
        SwcyCommodityDto swcyCommodityDto = new SwcyCommodityDto();
        if(!isAdmin) {
            swcyCommodityDto.setStatus(1);
        }
        swcyCommodityDto.setTypeId(commodityTypeId);
        swcyCommodityDto.setPageNumber(pageNumber);
        swcyCommodityDto.setPageSize(pageSize);
        swcyCommodityDto.setDelFlag(0);
        return swcyCommodityService.getPageByDtoWithPageRequet(swcyCommodityDto);
    }

    public List<SwcyCommodityEntity> getCommodityNewestPrice(CommodityNewestPriceDto commodityNewestPriceDto) throws Exception {
        SwcyCommodityDto swcyCommodityDto = new SwcyCommodityDto();
        swcyCommodityDto.setId(commodityNewestPriceDto.getId());
        return swcyCommodityService.getListByDto(swcyCommodityDto);
    }

    public SwcyCommodityEntity saveCommodity (SwcyCommodityEntity swcyCommodityEntity) {
        return swcyCommodityService.saveEntity(swcyCommodityEntity);
    }

    public void deleteCommodity (Integer id) {
        SwcyCommodityEntity swcyCommodityEntity = swcyCommodityService.findById(id);
        swcyCommodityEntity.setDelFlag(1);
        swcyCommodityService.saveEntity(swcyCommodityEntity);
    }

    public SwcyCommodityEntity upperShelfAndLowerShelf(Integer id) {
        SwcyCommodityEntity swcyCommodityEntity = swcyCommodityService.findById(id);
        if (swcyCommodityEntity.getStatus() == 0) {
            swcyCommodityEntity.setStatus(1);
        } else {
            swcyCommodityEntity.setStatus(0);
        }
        return swcyCommodityService.saveEntity(swcyCommodityEntity);
    }

    public SwcyCommodityEntity getCommodityById(Integer id) {
        return swcyCommodityService.findById(id);
    }

    public Map<String, Object> getCommodityByUnifiedOrderDto(UnifiedOrderDto unifiedOrderDto) {
        List<SwcyCommodityEntity> list = new ArrayList<>();
//        BigDecimal sunPrice = new BigDecimal(0);
        double sunPrice = 0.0;
        for (Integer id : unifiedOrderDto.getIdAndCount().keySet()) {
            SwcyCommodityEntity swcyCommodityEntity = swcyCommodityService.findById(id);
            list.add(swcyCommodityEntity);
            Integer number = unifiedOrderDto.getIdAndCount().get(id);
            BigDecimal tempPrice = swcyCommodityEntity.getPrice().multiply(new BigDecimal(number));
            sunPrice += tempPrice.doubleValue();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("sunPrice", sunPrice);
        return map;
    }

    public List<SwcyCommodityEntity> getCommodityBySupplierCommodityIdAndTypeId(Integer supplierCommodityId, Integer typeId) throws Exception {
        SwcyCommodityDto swcyCommodityDto = new SwcyCommodityDto();
        swcyCommodityDto.setTypeId(typeId);
        swcyCommodityDto.setSupplierCommodityId(supplierCommodityId);
        return swcyCommodityService.getListByDto(swcyCommodityDto);
    }

}
