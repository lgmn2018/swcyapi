package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.result.Result;
import com.lgmn.swcy.basic.entity.SwcyCommodityEntity;
import com.lgmn.swcy.basic.entity.SwcyCommodityTypeEntity;
import com.lgmn.swcy.basic.entity.SwcyIndustryEntity;
import com.lgmn.swcy.basic.entity.SwcyStoreEntity;
import com.lgmn.swcyapi.dto.store.*;
import com.lgmn.swcyapi.service.ad.AdService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityApiService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityTypeApiService;
import com.lgmn.swcyapi.service.follow.SwcyFollowApiService;
import com.lgmn.swcyapi.service.industry.IndustryService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.vo.store.ShopTypeAndEssentialMessageVo;
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

    @Autowired
    SwcyCommodityApiService swcyCommodityApiService;

    @Autowired
    SwcyCommodityTypeApiService swcyCommodityTypeApiService;

    @Autowired
    SwcyFollowApiService swcyFollowApiService;

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

    public Result getShopTypeAndEssentialMessage(String userId, ShopTypeAndEssentialMessageDto shopTypeAndEssentialMessageDto) throws Exception {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(shopTypeAndEssentialMessageDto.getId());
        List<SwcyCommodityTypeEntity> commodityTypeList = swcyCommodityTypeApiService.getCommodityTypeByStoreId(swcyStoreEntity.getId());
        boolean isFollow = swcyFollowApiService.isFollow(userId, swcyStoreEntity.getId());
        ShopTypeAndEssentialMessageVo shopTypeAndEssentialMessageVo = new ShopTypeAndEssentialMessageVo();
        shopTypeAndEssentialMessageVo.setSwcyStoreEntity(swcyStoreEntity);
        shopTypeAndEssentialMessageVo.setCommodityTypeList(commodityTypeList);
        shopTypeAndEssentialMessageVo.setFollow(isFollow);
        return Result.success(shopTypeAndEssentialMessageVo);
    }

    public Result followAndCleanFollow(String userId, ShopTypeAndEssentialMessageDto shopTypeAndEssentialMessageDto) throws Exception {
        boolean followAndCancelFollow = swcyFollowApiService.followAndCancelFollow(userId, shopTypeAndEssentialMessageDto.getId());
        if (followAndCancelFollow) {
            return Result.success("收藏成功");
        }
        return Result.success("取消收藏成功");
    }

    public Result getCommodityPageByCommodityTypeId(CommodityPageByCommodityTypeDto commodityPageByCommodityTypeDto) throws Exception {
        LgmnPage<SwcyCommodityEntity> lgmnPage = swcyCommodityApiService.getCommodityPageByCommodityTypeId(commodityPageByCommodityTypeDto.getCommodityTypeId(), commodityPageByCommodityTypeDto.getPageNumber(), commodityPageByCommodityTypeDto.getPageSize());
        return Result.success(lgmnPage);
    }

    public Result getCommodityNewestPrice(CommodityNewestPriceDto commodityNewestPriceDto) throws Exception {
        List<SwcyCommodityEntity> list = swcyCommodityApiService.getCommodityNewestPrice(commodityNewestPriceDto);
        return Result.success(list);
    }

}
