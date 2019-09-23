package com.lgmn.swcyapi.service.commodity;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcyCommodityDto;
import com.lgmn.swcy.basic.entity.SwcyCommodityEntity;
import com.lgmn.swcy.basic.service.SwcyCommodityService;
import com.lgmn.swcyapi.dto.store.CommodityNewestPriceDto;
import org.springframework.stereotype.Component;

import java.util.List;

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

}
