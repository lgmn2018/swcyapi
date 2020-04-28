package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.common.utils.ObjectTransfer;
import com.lgmn.qiniu.starter.service.QiNiu_UpLoad_Img_StarterService;
import com.lgmn.swcy.basic.dto.SwcyStoreDto;
import com.lgmn.swcy.basic.entity.*;
import com.lgmn.swcyapi.dto.store.*;
import com.lgmn.swcyapi.service.ad.AdService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityApiService;
import com.lgmn.swcyapi.service.commodity.SwcyCommodityTypeApiService;
import com.lgmn.swcyapi.service.follow.SwcyFollowApiService;
import com.lgmn.swcyapi.service.industry.IndustryService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.service.supplier.*;
import com.lgmn.swcyapi.vo.person.QiNiuTokenVo;
import com.lgmn.swcyapi.vo.store.NewsGetPageStoreVo;
import com.lgmn.swcyapi.vo.store.ShopTypeAndEssentialMessageVo;
import com.lgmn.swcyapi.vo.store.StoreIndustryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

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

    @Autowired
    SwcySupplierCommodityAPIService swcySupplierCommodityAPIService;

    @Autowired
    SwcySupplierCategoryAPIService swcySupplierCategoryAPIService;

    @Autowired
    SwcySupplierOrderAPIService swcySupplierOrderAPIService;

    @Autowired
    SwcySupplierOrderDetailAPIService swcySupplierOrderDetailAPIService;

    @Autowired
    SwcySupplierAPIService swcySupplierAPIService;

    @Autowired
    SupplierService supplierService;

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

    public Result getPageStore (StoreDto storeDto) throws Exception {
        if (storeDto.getIndustryId().get(0) == 0) {
            List<Integer> industryIds = new ArrayList<>();
            List<SwcyIndustryEntity> industryList = industryService.getIndustryList();
            for (SwcyIndustryEntity swcyIndustryEntity : industryList) {
                industryIds.add(swcyIndustryEntity.getId());
            }
            storeDto.setIndustryId(industryIds);
        }
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
        if (!shopTypeAndEssentialMessageDto.getIsAdmin()) {
            if (commodityTypeList.size() > 0) {
                SwcyCommodityTypeEntity allCommodityType = new SwcyCommodityTypeEntity();
                allCommodityType.setId(0);
                allCommodityType.setName("全部");
                allCommodityType.setStatus(1);
                allCommodityType.setSupplierCategoryId(0);
                allCommodityType.setStoreId(commodityTypeList.get(0).getStoreId());
                commodityTypeList.add(0, allCommodityType);
            }
        }
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
        List<Integer> typeIds = new ArrayList<>();
        if (commodityPageByCommodityTypeDto.getCommodityTypeId() == 0) {
            List<SwcyCommodityTypeEntity> typeList = swcyCommodityTypeApiService.getCommodityTypeByStoreId(commodityPageByCommodityTypeDto.getStoreId());
            for (SwcyCommodityTypeEntity swcyCommodityTypeEntity : typeList) {
                typeIds.add(swcyCommodityTypeEntity.getId());
            }

            LgmnPage<SwcyCommodityEntity> lgmnPage = swcyCommodityApiService.getCommodityPageByCommodityTypeId(typeIds, commodityPageByCommodityTypeDto.getPageNumber(), commodityPageByCommodityTypeDto.getPageSize(), commodityPageByCommodityTypeDto.getIsAdmin());
            return Result.success(lgmnPage);
        }
        typeIds.add(commodityPageByCommodityTypeDto.getCommodityTypeId());
        LgmnPage<SwcyCommodityEntity> lgmnPage = swcyCommodityApiService.getCommodityPageByCommodityTypeId(typeIds, commodityPageByCommodityTypeDto.getPageNumber(), commodityPageByCommodityTypeDto.getPageSize(), commodityPageByCommodityTypeDto.getIsAdmin());
        return Result.success(lgmnPage);
    }

    public Result getCommodityNewestPrice(CommodityNewestPriceDto commodityNewestPriceDto) throws Exception {
        List<SwcyCommodityEntity> list = swcyCommodityApiService.getCommodityNewestPrice(commodityNewestPriceDto);
        return Result.success(list);
    }

    public Result addStore(LgmnUserInfo lgmnUserInfo, AddStoreForUnlicensedDto addStoreForUnlicensedDto) throws Exception {
        SwcyStoreEntity swcyStoreEntity;
        if (addStoreForUnlicensedDto.getId() == null) {
            swcyStoreEntity = new SwcyStoreEntity();
            swcyStoreEntity.setUid(lgmnUserInfo.getId());
            swcyStoreEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            swcyStoreEntity.setIsChecked(3);
            swcyStoreEntity.setType(0);
            swcyStoreEntity.setStatus(0);
            swcyStoreEntity.setDelFlag(0);
        } else {
            swcyStoreEntity = sStoreService.getStoreById(addStoreForUnlicensedDto.getId());
        }
        ObjectTransfer.transValue(addStoreForUnlicensedDto, swcyStoreEntity);
        swcyStoreEntity.setArea(new BigDecimal(addStoreForUnlicensedDto.getArea()));
        SwcyStoreEntity newStore = sStoreService.save(swcyStoreEntity);

        SwcyStoreDto swcyStoreDto = new SwcyStoreDto();
        swcyStoreDto.setDelFlag(0);
        swcyStoreDto.setStoreId(newStore.getId());
        List<SwcyStoreEntity> stores = sStoreService.getStoreListByDto(swcyStoreDto);
        for (SwcyStoreEntity item : stores) {
            item.setStoreName(newStore.getStoreName());
            item.setAddress(newStore.getAddress());
            item.setLat(newStore.getLat());
            item.setLng(newStore.getLng());
            item.setIndustryId(newStore.getIndustryId());
            item.setPhoto(newStore.getPhoto());
            item.setIndustryName(newStore.getIndustryName());
            item.setProvinceName(newStore.getProvinceName());
            item.setCityName(newStore.getCityName());
            item.setAreaName(newStore.getAreaName());
            item.setArea(newStore.getArea());
            item.setPhone(newStore.getPhone());
            sStoreService.save(item);
        }
        return Result.success(newStore);
    }

    public Result authenticationStore(AuthenticationStoreDto authenticationStoreDto) {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(authenticationStoreDto.getId());
        ObjectTransfer.transValue(authenticationStoreDto, swcyStoreEntity);
        swcyStoreEntity.setIsChecked(0);
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
        SwcyCommodityTypeEntity swcyCommodityTypeEntity = swcyCommodityTypeApiService.getCommodityTypeById(addCommodityDto.getTypeId());
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(swcyCommodityTypeEntity.getStoreId());
        SwcyCommodityEntity swcyCommodityEntity = new SwcyCommodityEntity();
        ObjectTransfer.transValue(addCommodityDto, swcyCommodityEntity);
        swcyCommodityEntity.setStatus(1);
        swcyCommodityEntity.setDelFlag(0);
        swcyCommodityEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        swcyCommodityEntity.setStarCode(swcyStoreEntity.getStarCode());
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

    public Result editStoreDescription(EditStoreDescriptionDto editStoreDescriptionDto) {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(editStoreDescriptionDto.getId());
        swcyStoreEntity.setDescription(editStoreDescriptionDto.getDescription());
        sStoreService.save(swcyStoreEntity);
        return Result.success("编辑详情成功");
    }

    public Result getMyFollowPage(LgmnUserInfo lgmnUserInfo, MyFollowPageDto myFollowPageDto) throws Exception {
        LgmnPage<SwcyFollowEntity> swcyFollowEntityLgmnPage = swcyFollowApiService.getMyFollowPage(lgmnUserInfo.getId(), myFollowPageDto.getPageNumber(), myFollowPageDto.getPageSize());
        LgmnPage<SwcyStoreEntity> swcyStoreEntityLgmnPage = new LgmnPage<>();
        swcyStoreEntityLgmnPage.setPageNumber(swcyFollowEntityLgmnPage.getPageNumber());
        swcyStoreEntityLgmnPage.setPageSize(swcyFollowEntityLgmnPage.getPageSize());
        swcyStoreEntityLgmnPage.setCount(swcyFollowEntityLgmnPage.getCount());
        swcyStoreEntityLgmnPage.setTotalPage(swcyFollowEntityLgmnPage.getTotalPage());
        List<SwcyStoreEntity> swcyStoreEntities = new ArrayList<>();
        for(SwcyFollowEntity swcyFollowEntity : swcyFollowEntityLgmnPage.getList()) {
            SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(swcyFollowEntity.getStoreId());
            swcyStoreEntities.add(swcyStoreEntity);
        }
        swcyStoreEntityLgmnPage.setList(swcyStoreEntities);
        return Result.success(swcyStoreEntityLgmnPage);
    }

    public Result newsGetPageStore(NewsGetPageStoreDto newsGetPageStoreDto) throws Exception {
        List<NewsGetPageStoreVo> newsGetPageStoreVos = new ArrayList<>();
        List<SwcyIndustryEntity> swcyIndustryEntities = industryService.getIndustryList();
        swcyIndustryEntities = industryService.getIndustryAll(swcyIndustryEntities);
        List<Integer> industryIds = new ArrayList<>();
        for (int i = 0; i < swcyIndustryEntities.size(); i++) {
            StoreDto storeDto = new StoreDto();
            if (swcyIndustryEntities.get(i).getId() != 0) {
                List<Integer> tempIds = new ArrayList<>();
                tempIds.add(swcyIndustryEntities.get(i).getId());
                storeDto.setIndustryId(tempIds);
            } else {
                storeDto.setIndustryId(industryIds);
            }
            storeDto.setPageNumber(0);
            storeDto.setPageSize(10);
            storeDto.setLat(newsGetPageStoreDto.getLat());
            storeDto.setLng(newsGetPageStoreDto.getLng());
            LgmnPage<Map> lgmnPage = sStoreService.getStoreByIndustryId(storeDto);

            NewsGetPageStoreVo newsGetPageStoreTempVo = new NewsGetPageStoreVo();
            newsGetPageStoreTempVo.setSwcyIndustryEntity(swcyIndustryEntities.get(i));
            newsGetPageStoreTempVo.setStoreMap(lgmnPage);
            newsGetPageStoreTempVo.setNewsGetPageStoreDto(newsGetPageStoreDto);
            if (swcyIndustryEntities.get(i).getId() != 0) {
                newsGetPageStoreVos.add(newsGetPageStoreTempVo);
            } else {
                newsGetPageStoreVos.add(0, newsGetPageStoreTempVo);
            }
            industryIds.add(swcyIndustryEntities.get(i).getId());
        }
        return Result.success(newsGetPageStoreVos);
    }

    public Result getShareStoreMsg(ShareStoreMsgDto shareStoreMsgDto) {
        return Result.success(sStoreService.getStoreById(shareStoreMsgDto.getId()));
    }

    public Result createLeagueStore(CreateLeagueStoreDto createLeagueStoreDto) throws Exception {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(createLeagueStoreDto.getStoreId());
        if (swcyStoreEntity.getType() != 0) {
            return Result.serverError("已存在盟店，请勿重复添加");
        }

        // 查找盟店对应供应商
        List<Integer> industryIds = new ArrayList<>();
        industryIds.add(swcyStoreEntity.getIndustryId());
        List<SwcySupplierEntity> swcySupplierEntities = swcySupplierAPIService.getSupplierListByIndustryId(industryIds);
        if (swcySupplierEntities.size() <= 0) {
            return Result.serverError("未有此类型供应商，暂不允许创建");
        }

        SwcySupplierEntity swcySupplierEntity = swcySupplierEntities.get(0);

        SwcyStoreEntity leagueStore = getLeagueStore(swcyStoreEntity, swcySupplierEntity.getDescription(), swcySupplierEntity.getBrief(), swcySupplierEntity.getStarCode());
        // 共享店修改为已有盟店状态
        swcyStoreEntity.setType(2);
        sStoreService.save(swcyStoreEntity);
        // 创建盟店
        SwcyStoreEntity newLeagueStore = sStoreService.save(leagueStore);

        // 盟店同步通硬伤类别以及商品
        List<SwcySupplierCategoryEntity> swcySupplierCategoryEntities = swcySupplierCategoryAPIService.getSupplierCategoryListBySupplierId(swcySupplierEntity.getId());
        saveCommodityType(swcySupplierCategoryEntities, newLeagueStore.getId(), newLeagueStore.getStarCode());

        return Result.success(newLeagueStore);
    }

    public Result leagueStoreAddCommodity(LeagueStoreAddCommodityDto leagueStoreAddCommodityDto) throws Exception {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(leagueStoreAddCommodityDto.getStoreId());
        Set<Integer> mapKey = leagueStoreAddCommodityDto.getMap().keySet();
        List<Integer> supplierCommodityIds = new ArrayList<>();
        for (Integer key : mapKey) {
            supplierCommodityIds.add(key);
        }
        List<SwcySupplierCommodityEntity> swcySupplierCommodityEntitys = swcySupplierCommodityAPIService.getSupplierCommoditysByIds(supplierCommodityIds);
        BigDecimal retailPrice = new BigDecimal(0);
        for (SwcySupplierCommodityEntity swcySupplierCommodityEntity : swcySupplierCommodityEntitys) {
            retailPrice = retailPrice.add(swcySupplierCommodityEntity.getRetailPrice());
        }

        SwcySupplierOrderEntity swcySupplierOrderEntity = new SwcySupplierOrderEntity();
        swcySupplierOrderEntity.setStatus(2);
        swcySupplierOrderEntity.setMoney(retailPrice);
        swcySupplierOrderEntity.setOrderTime(new Timestamp(System.currentTimeMillis()));
        swcySupplierOrderEntity.setPayTime(new Timestamp(System.currentTimeMillis()));
        swcySupplierOrderEntity.setPayChannel("免支付");
        swcySupplierOrderEntity.setPayNum("免支付");
        swcySupplierOrderEntity.setSupplierId(leagueStoreAddCommodityDto.getSupplierId());
        swcySupplierOrderEntity.setStoreOwnerId(swcyStoreEntity.getUid());
        swcySupplierOrderEntity.setStoreId(leagueStoreAddCommodityDto.getStoreId());
        SwcySupplierOrderEntity newSupplierOrder = swcySupplierOrderAPIService.save(swcySupplierOrderEntity);

        List<SwcySupplierOrderDetailEntity> saveSupplierOrderDetails = new ArrayList<>();
        for (SwcySupplierCommodityEntity swcySupplierCommodityEntity : swcySupplierCommodityEntitys) {
            SwcySupplierCategoryEntity swcySupplierCategoryEntity = swcySupplierCategoryAPIService.getSupplierCategoryById(swcySupplierCommodityEntity.getCategoryId());
            SwcySupplierOrderDetailEntity swcySupplierOrderDetailEntity = new SwcySupplierOrderDetailEntity();
            swcySupplierOrderDetailEntity.setOrderId(newSupplierOrder.getId());
            swcySupplierOrderDetailEntity.setCommodityId(swcySupplierCommodityEntity.getId());
            swcySupplierOrderDetailEntity.setCommodityName(swcySupplierCommodityEntity.getName());
            swcySupplierOrderDetailEntity.setCommodityType(swcySupplierCategoryEntity.getName());
            swcySupplierOrderDetailEntity.setCover(swcySupplierCommodityEntity.getCover());
            swcySupplierOrderDetailEntity.setPrice(swcySupplierCommodityEntity.getRetailPrice());
            swcySupplierOrderDetailEntity.setNum(leagueStoreAddCommodityDto.getMap().get(swcySupplierCommodityEntity.getId()));
            saveSupplierOrderDetails.add(swcySupplierOrderDetailEntity);
        }
        swcySupplierOrderDetailAPIService.saveSupplierOrderDetails(saveSupplierOrderDetails);
        return Result.success("确定成功，请等待发货");
    }

    public Result delStoreById(DelStoreDto delStoreDto) throws Exception {
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(delStoreDto.getStoreId());
        swcyStoreEntity.setDelFlag(1);

        if (swcyStoreEntity.getType() == 1) {
            SwcyStoreEntity store = sStoreService.getStoreById(swcyStoreEntity.getStoreId());
            store.setType(0);
            sStoreService.save(store);
        } else {
            SwcyStoreDto swcyStoreDto = new SwcyStoreDto();
            swcyStoreDto.setStoreId(swcyStoreEntity.getId());
            swcyStoreDto.setDelFlag(0);
            List<SwcyStoreEntity> tempList = sStoreService.getStoreListByDto(swcyStoreDto);
            for (SwcyStoreEntity item : tempList) {
                item.setDelFlag(1);
                sStoreService.save(item);
            }
        }

        sStoreService.save(swcyStoreEntity);
        return Result.success("删除成功");
    }

    private SwcyStoreEntity getLeagueStore(SwcyStoreEntity swcyStoreEntity, String description, String brief, Integer starCode) {
        SwcyStoreEntity leagueStore = new SwcyStoreEntity();
        leagueStore.setUid(swcyStoreEntity.getUid());
        leagueStore.setStoreName(swcyStoreEntity.getStoreName());
        leagueStore.setAddress(swcyStoreEntity.getAddress());
        leagueStore.setLat(swcyStoreEntity.getLat());
        leagueStore.setLng(swcyStoreEntity.getLng());
        leagueStore.setIndustryId(swcyStoreEntity.getIndustryId());
        leagueStore.setCreateTime(new Timestamp(System.currentTimeMillis()));
        leagueStore.setPhoto(swcyStoreEntity.getPhoto());
        leagueStore.setDescription(description);
        leagueStore.setIndustryName(swcyStoreEntity.getIndustryName());
        leagueStore.setProvinceName(swcyStoreEntity.getProvinceName());
        leagueStore.setCityName(swcyStoreEntity.getCityName());
        leagueStore.setAreaName(swcyStoreEntity.getAreaName());
        leagueStore.setLicenseCode(swcyStoreEntity.getLicenseCode());
        leagueStore.setLicensePhoto(swcyStoreEntity.getLicensePhoto());
        leagueStore.setLegalPerson(swcyStoreEntity.getLegalPerson());
        leagueStore.setIsChecked(0);
        leagueStore.setReason("");
        leagueStore.setArea(swcyStoreEntity.getArea());
        leagueStore.setPhone(swcyStoreEntity.getPhone());
        leagueStore.setStarCode(starCode);
        leagueStore.setStatus(0);
        leagueStore.setBrief(brief);
        leagueStore.setType(1);
        leagueStore.setDelFlag(0);
        leagueStore.setStoreId(swcyStoreEntity.getId());

        return leagueStore;
    }

    private void saveCommodityType(List<SwcySupplierCategoryEntity> swcySupplierCategoryEntities, Integer storeId, Integer starCode) throws Exception {
        for (SwcySupplierCategoryEntity swcySupplierCategoryEntity : swcySupplierCategoryEntities) {
            List<SwcyCommodityTypeEntity> swcyCommodityTypeEntityList = swcyCommodityTypeApiService.getCommodityTypeByStoreIdAndSupplierCategoryId(storeId, swcySupplierCategoryEntity.getId());
            List<SwcySupplierCommodityEntity> swcySupplierCommodityEntities = swcySupplierCommodityAPIService.getSupplierCommoditysByCategoryId(swcySupplierCategoryEntity.getId());
            if (swcyCommodityTypeEntityList.size() <= 0) {
                SwcyCommodityTypeEntity swcyCommodityTypeEntity = new SwcyCommodityTypeEntity();
                swcyCommodityTypeEntity.setStoreId(storeId);
                swcyCommodityTypeEntity.setName(swcySupplierCategoryEntity.getName());
                swcyCommodityTypeEntity.setStatus(swcySupplierCategoryEntity.getStatus());
                swcyCommodityTypeEntity.setSupplierCategoryId(swcySupplierCategoryEntity.getId());
                SwcyCommodityTypeEntity newCommodityType = swcyCommodityTypeApiService.save(swcyCommodityTypeEntity);
                saveCommodity(swcySupplierCommodityEntities, newCommodityType.getId(), starCode);
            }
        }
    }

    private void saveCommodity(List<SwcySupplierCommodityEntity> swcySupplierCommodityEntities, Integer commodityTypeId, Integer starCode) throws Exception {
        for (SwcySupplierCommodityEntity swcySupplierCommodityEntity : swcySupplierCommodityEntities) {
            List<SwcyCommodityEntity> swcyCommodityEntities = swcyCommodityApiService.getCommodityBySupplierCommodityIdAndTypeId(swcySupplierCommodityEntity.getId(), commodityTypeId);
            if (swcyCommodityEntities.size() <= 0) {
                SwcyCommodityEntity swcyCommodityEntity = supplierService.getCommodity(commodityTypeId, swcySupplierCommodityEntity, starCode, 0);
                swcyCommodityApiService.saveCommodity(swcyCommodityEntity);
            }
        }
    }
}
