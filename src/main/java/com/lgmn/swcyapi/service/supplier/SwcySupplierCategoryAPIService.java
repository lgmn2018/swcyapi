package com.lgmn.swcyapi.service.supplier;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcySupplierCategoryDto;
import com.lgmn.swcy.basic.entity.SwcySupplierCategoryEntity;
import com.lgmn.swcy.basic.service.SwcySupplierCategoryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwcySupplierCategoryAPIService {

    @Reference(version = "${demo.service.version}")
    SwcySupplierCategoryService swcySupplierCategoryService;

    public List<SwcySupplierCategoryEntity> getSupplierCategoryListBySupplierId(Integer supplierId) throws Exception {
        SwcySupplierCategoryDto swcySupplierCategoryDto = new SwcySupplierCategoryDto();
        swcySupplierCategoryDto.setSupplierId(supplierId);
        return swcySupplierCategoryService.getListByDto(swcySupplierCategoryDto);
    }

    public SwcySupplierCategoryEntity getSupplierCategoryById(Integer id) {
        return swcySupplierCategoryService.findById(id);
    }

}
