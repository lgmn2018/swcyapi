package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.result.Result;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcy.basic.entity.SwcyIndustryEntity;
import com.lgmn.swcy.basic.entity.SwcyStoreEntity;
import com.lgmn.swcyapi.dto.store.SearchStoreDto;
import com.lgmn.swcyapi.dto.store.StoreDto;
import com.lgmn.swcyapi.service.ad.AdService;
import com.lgmn.swcyapi.service.industry.IndustryService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.vo.home.HomeAdVo;
import com.lgmn.swcyapi.vo.store.AdAndIndustryVo;
import com.lgmn.swcyapi.vo.store.StoreIndustryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class StoreService {

    @Autowired
    IndustryService industryService;

    @Autowired
    AdService adService;

    @Autowired
    SStoreService sStoreService;

    public Result getStoreIndustryList() throws Exception {
        List<SwcyIndustryEntity> swcyIndustryEntities = industryService.getIndustryList();
//        List<SwcyAdEntity> swcyAdEntities = adService.getAdListByType(2);
        List<StoreIndustryVo> storeIndustryVos = new StoreIndustryVo().getVoList(swcyIndustryEntities, StoreIndustryVo.class);
//        List<HomeAdVo> homeAdVos = new HomeAdVo().getVoList(swcyAdEntities, HomeAdVo.class);
//        AdAndIndustryVo adAndIndustryVo = new AdAndIndustryVo();
//        adAndIndustryVo.setHomeAdVos(homeAdVos);
//        adAndIndustryVo.setStoreIndustryVos(storeIndustryVos);
        return Result.success(storeIndustryVos);
    }

    public Result getPageStore (StoreDto storeDto) {
        LgmnPage<Map> swcyStoreEntityLgmnPage = sStoreService.getStoreByIndustryId(storeDto);
        return Result.success(swcyStoreEntityLgmnPage);
    }
    public Result getPageSearchStore (SearchStoreDto searchStoreDto) {
        LgmnPage<Map> swcyStoreEntityLgmnPage = sStoreService.getPageSearchStore(searchStoreDto);
        return Result.success(swcyStoreEntityLgmnPage);
    }

}
