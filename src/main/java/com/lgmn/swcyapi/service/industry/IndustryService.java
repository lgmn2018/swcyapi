package com.lgmn.swcyapi.service.industry;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcyIndustryDto;
import com.lgmn.swcy.basic.entity.SwcyIndustryEntity;
import com.lgmn.swcy.basic.service.SwcyIndustryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndustryService {
    @Reference(version = "${demo.service.version}")
    private SwcyIndustryService swcyIndustryService;

    public List<SwcyIndustryEntity> getIndustryList () throws Exception {
        SwcyIndustryDto swcyIndustryDto = new SwcyIndustryDto();
        swcyIndustryDto.setPid(0);
        return swcyIndustryService.getListByDto(swcyIndustryDto);
    }
}
