package com.lgmn.swcyapi.service.commodity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcyCommodityDto;
import com.lgmn.swcy.basic.entity.SwcyCommodityEntity;
import com.lgmn.swcy.basic.service.SwcyCommodityService;
import com.lgmn.swcyapi.dto.store.CommodityNewestPriceDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SwcyCommodityApiService {
    @Reference(version = "${demo.service.version}")
    private SwcyCommodityService swcyCommodityService;

    public LgmnPage<SwcyCommodityEntity> getCommodityPageByCommodityTypeId(Integer commodityTypeId, Integer pageNumber, Integer pageSize) throws Exception {
        SwcyCommodityDto swcyCommodityDto = new SwcyCommodityDto();
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
}
