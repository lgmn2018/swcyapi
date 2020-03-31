package com.lgmn.swcyapi.service.commodity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcyCommodityTypeDto;
import com.lgmn.swcy.basic.entity.SwcyCommodityTypeEntity;
import com.lgmn.swcy.basic.service.SwcyCommodityTypeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwcyCommodityTypeApiService {
    @Reference(version = "${demo.service.version}")
    private SwcyCommodityTypeService swcyCommodityTypeService;

    public List<SwcyCommodityTypeEntity> getCommodityTypeByStoreId(Integer storeId) throws Exception {
        SwcyCommodityTypeDto swcyCommodityTypeDto = new SwcyCommodityTypeDto();
        swcyCommodityTypeDto.setStoreId(storeId);
        swcyCommodityTypeDto.setStatus(1);
        return swcyCommodityTypeService.getListByDto(swcyCommodityTypeDto);
    }

    public SwcyCommodityTypeEntity save(SwcyCommodityTypeEntity swcyCommodityTypeEntity) {
        return swcyCommodityTypeService.saveEntity(swcyCommodityTypeEntity);
    }

    public void delete(Integer id) {
        SwcyCommodityTypeEntity swcyCommodityTypeEntity = swcyCommodityTypeService.findById(id);
        swcyCommodityTypeEntity.setStatus(0);
        swcyCommodityTypeService.saveEntity(swcyCommodityTypeEntity);
    }

    public SwcyCommodityTypeEntity getCommodityTypeById(Integer id) {
        SwcyCommodityTypeEntity swcyCommodityTypeEntity = swcyCommodityTypeService.findById(id);
        return swcyCommodityTypeEntity;
    }

    public List<SwcyCommodityTypeEntity> getCommodityTypeByStoreIdAndSupplierCategoryId(Integer storeId, Integer supplierCategoryId) throws Exception {
        SwcyCommodityTypeDto swcyCommodityTypeDto = new SwcyCommodityTypeDto();
        swcyCommodityTypeDto.setStoreId(storeId);
        swcyCommodityTypeDto.setSupplierCategoryId(supplierCategoryId);
        return swcyCommodityTypeService.getListByDto(swcyCommodityTypeDto);
    }
}
