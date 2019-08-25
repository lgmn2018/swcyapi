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
}
