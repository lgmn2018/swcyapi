package com.lgmn.swcyapi.service.supplier;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcySupplierDto;
import com.lgmn.swcy.basic.entity.SwcySupplierEntity;
import com.lgmn.swcy.basic.service.SwcySupplierService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwcySupplierAPIService {

    @Reference(version = "${demo.service.version}")
    SwcySupplierService swcySupplierService;

    public LgmnPage<SwcySupplierEntity> getSupplierPageByIndustryId(Integer industryId, Integer pageNumber, Integer pageSize) throws Exception {
        SwcySupplierDto swcySupplierDto = new SwcySupplierDto();
        swcySupplierDto.setIndustryId(industryId);
        swcySupplierDto.setStatus(1);
        swcySupplierDto.setPageNumber(pageNumber);
        swcySupplierDto.setPageSize(pageSize);
        return swcySupplierService.getPageByDtoWithPageRequet(swcySupplierDto);
    }

    public SwcySupplierEntity getSupplierById(Integer id) {
        return swcySupplierService.findById(id);
    }

    public List<SwcySupplierEntity> getSupplierListByIndustryId(Integer industryId) throws Exception {
        SwcySupplierDto swcySupplierDto = new SwcySupplierDto();
        swcySupplierDto.setIndustryId(industryId);
        swcySupplierDto.setStatus(1);
        return swcySupplierService.getListByDto(swcySupplierDto);
    }
}
