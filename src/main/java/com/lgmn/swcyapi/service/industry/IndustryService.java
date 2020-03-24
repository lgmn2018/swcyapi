package com.lgmn.swcyapi.service.industry;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.swcy.basic.dto.SwcyIndustryDto;
import com.lgmn.swcy.basic.entity.SwcyIndustryEntity;
import com.lgmn.swcy.basic.service.SwcyIndustryService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IndustryService {
    @Reference(version = "${demo.service.version}")
    private SwcyIndustryService swcyIndustryService;

    public List<SwcyIndustryEntity> getIndustryList () throws Exception {
        SwcyIndustryDto swcyIndustryDto = new SwcyIndustryDto();
        swcyIndustryDto.setPid(0);
        swcyIndustryDto.setStatus(1);
        LgmnOrder lgmnOrder = new LgmnOrder();
        lgmnOrder.setProperty("sort");
        lgmnOrder.setDirection(Sort.Direction.ASC);
        List<LgmnOrder> orderList = new ArrayList<>();
        orderList.add(lgmnOrder);
        swcyIndustryDto.setOrders(orderList);
        List<SwcyIndustryEntity> industryEntitys = swcyIndustryService.getListByDto(swcyIndustryDto);
        return industryEntitys;
    }

    public List<SwcyIndustryEntity> getIndustryAll(List<SwcyIndustryEntity> industryEntitys) {
        SwcyIndustryEntity tempIndustry = new SwcyIndustryEntity();
        tempIndustry.setId(0);
        tempIndustry.setName("全部");
        tempIndustry.setPid(0);
        tempIndustry.setStatus(1);
        tempIndustry.setSort(0);
        industryEntitys.add(industryEntitys.size(), tempIndustry);
        return industryEntitys;
    }
}
