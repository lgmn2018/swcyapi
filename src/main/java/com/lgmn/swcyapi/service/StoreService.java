package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import com.lgmn.qiniu.starter.service.QiNiu_UpLoad_Img_StarterService;
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
import com.lgmn.swcyapi.vo.person.QiNiuTokenVo;
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

    @Autowired
    QiNiu_UpLoad_Img_StarterService qiNiu_upLoad_img_starterService;

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
        LgmnPage<SwcyCommodityEntity> lgmnPage = swcyCommodityApiService.getCommodityPageByCommodityTypeId(commodityPageByCommodityTypeDto.getCommodityTypeId(), commodityPageByCommodityTypeDto.getPageNumber(), commodityPageByCommodityTypeDto.getPageSize(), commodityPageByCommodityTypeDto.getIsAdmin());
        return Result.success(lgmnPage);
    }

    public Result getCommodityNewestPrice(CommodityNewestPriceDto commodityNewestPriceDto) throws Exception {
        List<SwcyCommodityEntity> list = swcyCommodityApiService.getCommodityNewestPrice(commodityNewestPriceDto);
        return Result.success(list);
    }

    public Result addStore(LgmnUserInfo lgmnUserInfo, AddStoreForUnlicensedDto addStoreForUnlicensedDto) {
        SwcyStoreEntity swcyStoreEntity = new SwcyStoreEntity();
        ObjectTransfer.transValue(addStoreForUnlicensedDto, swcyStoreEntity);
        swcyStoreEntity.setUid(lgmnUserInfo.getId());
        SwcyStoreEntity newStore = sStoreService.save(swcyStoreEntity);
        return Result.success(newStore);
    }

    public Result authenticationStore(AuthenticationStoreDto authenticationStoreDto) {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(authenticationStoreDto.getId());
        ObjectTransfer.transValue(authenticationStoreDto, swcyStoreEntity);
        sStoreService.save(swcyStoreEntity);
        return Result.success("提交认证成功");
    }


    public Result addCommodityType(AddCommodityTypeDto addCommodityTypeDto) {
        SwcyCommodityTypeEntity swcyCommodityTypeEntity = new SwcyCommodityTypeEntity();
        ObjectTransfer.transValue(addCommodityTypeDto, swcyCommodityTypeEntity);
        swcyCommodityTypeEntity.setStatus(1);
        swcyCommodityTypeApiService.save(swcyCommodityTypeEntity);
        return Result.success("添加商品类型成功");
    }

    public Result editCommodityType(EditCommodityTypeDto editCommodityTypeDto) {
        SwcyCommodityTypeEntity swcyCommodityTypeEntity = swcyCommodityTypeApiService.getCommodityTypeById(editCommodityTypeDto.getId());
        ObjectTransfer.transValue(editCommodityTypeDto, swcyCommodityTypeEntity);
        swcyCommodityTypeApiService.save(swcyCommodityTypeEntity);
        return Result.success("修改商品类型成功");
    }

    public Result deleteCommodityType(DeleteCommodityTypeDto deleteCommodityTypeDto) {
        swcyCommodityTypeApiService.delete(deleteCommodityTypeDto.getId());
        return Result.success("删除成功");
    }

    public Result addCommodity(AddCommodityDto addCommodityDto) {
        SwcyCommodityEntity swcyCommodityEntity = new SwcyCommodityEntity();
        ObjectTransfer.transValue(addCommodityDto, swcyCommodityEntity);
        swcyCommodityEntity.setStatus(1);
        swcyCommodityEntity.setDelFlag(0);
        SwcyCommodityEntity newCommodity = swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
        return Result.success(newCommodity);
    }

    public Result editCommodity(EditCommodityDto editCommodityDto) {
        SwcyCommodityEntity swcyCommodityEntity = swcyCommodityApiService.getCommodityById(editCommodityDto.getId());
        ObjectTransfer.transValue(editCommodityDto, swcyCommodityEntity);
        SwcyCommodityEntity editCommodity = swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
        return Result.success(editCommodity);
    }

    public Result deleteCommodity(DeleteCommodityDto deleteCommodityDto) {
        swcyCommodityApiService.deleteCommodity(deleteCommodityDto.getId());
        return Result.success("删除商品成功");
    }

    public Result upperShelfAndLowerShelf(DeleteCommodityDto deleteCommodityDto) {
        SwcyCommodityEntity swcyCommodityEntity = swcyCommodityApiService.upperShelfAndLowerShelf(deleteCommodityDto.getId());
        return Result.success(swcyCommodityEntity);
    }

    public Result getQiNiuToken() {
        String token = qiNiu_upLoad_img_starterService.getUpLoadToken();
        QiNiuTokenVo qiNiuTokenVo = new QiNiuTokenVo();
        qiNiuTokenVo.setToken(token);
        return Result.success(qiNiuTokenVo);
    }

    public Result getMyStorePage(LgmnUserInfo lgmnUserInfo, MyStorePageDto myStorePageDto) throws Exception {
        LgmnPage<SwcyStoreEntity> lgmnPage = sStoreService.getMyStorePage(lgmnUserInfo.getId(), myStorePageDto.getPageNumber(), myStorePageDto.getPageSize());
        return Result.success(lgmnPage);
    }

    public Result getStoreById(GetStoreByIdDto getStoreByIdDto) {
        return Result.success(sStoreService.getStoreById(getStoreByIdDto.getStoreId()));
    }

    public Result editCommodityDetails(EditCommodityDetailDto editCommodityDetailDto) {
        SwcyCommodityEntity swcyCommodityEntity = swcyCommodityApiService.getCommodityById(editCommodityDetailDto.getId());
        swcyCommodityEntity.setDetail(editCommodityDetailDto.getDetail());
        swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
        return Result.success("编辑详情成功");
    }
}
