package com.lgmn.swcyapi.controller;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.store.*;
import com.lgmn.swcyapi.service.StoreService;
import com.lgmn.userservices.basic.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Api(description = "商家")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @ApiOperation(value = "获取门店类型")
    @PostMapping("/getStoreIndustryList")
    public Result getStoreIndustryList() {
        try {
            return storeService.getStoreIndustryList();
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取门店列表")
    @PostMapping("/getPageStore")
    public Result getPageStore(@RequestBody StoreDto storeDto) {
        try {
            return storeService.getPageStore(storeDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "搜索门店列表")
    @PostMapping("/getPageSearchStore")
    public Result getPageSearchStore(@RequestBody SearchStoreDto searchStoreDto) {
        try {
            return storeService.getPageSearchStore(searchStoreDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "门店分类、详情，是否关注")
    @PostMapping("/getShopTypeAndEssentialMessage")
    public Result getShopTypeAndEssentialMessage(@RequestHeader String Authorization, Principal principal, @RequestBody ShopTypeAndEssentialMessageDto shopTypeAndEssentialMessageDto) {
        try {
            LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
            return storeService.getShopTypeAndEssentialMessage(lgmnUserInfo.getId(), shopTypeAndEssentialMessageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "关注和取消关注")
    @PostMapping("/followAndCleanFollow")
    public Result followAndCleanFollow(@RequestHeader String Authorization, Principal principal, @RequestBody ShopTypeAndEssentialMessageDto shopTypeAndEssentialMessageDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return storeService.followAndCleanFollow(lgmnUserInfo.getId(), shopTypeAndEssentialMessageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取门店商品列表")
    @PostMapping("/getCommodityPageByCommodityTypeId")
    public Result getCommodityPageByCommodityTypeId(@RequestBody CommodityPageByCommodityTypeDto commodityPageByCommodityTypeDto) {
        try {
            return storeService.getCommodityPageByCommodityTypeId(commodityPageByCommodityTypeDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取购物车最新的价格")
    @PostMapping("/getCommodityNewestPrice")
    public Result getCommodityNewestPrice(@RequestBody CommodityNewestPriceDto commodityNewestPriceDto) {
        try {
            return storeService.getCommodityNewestPrice(commodityNewestPriceDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "添加无证共享店")
    @PostMapping("/addStoreForUnlicensed")
    public Result addStoreForUnlicensed(@RequestHeader String Authorization, Principal principal, @RequestBody AddStoreForUnlicensedDto addStoreForUnlicensedDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return storeService.addStore(lgmnUserInfo, addStoreForUnlicensedDto);
    }

    @ApiOperation(value = "添加有证共享店")
    @PostMapping("/addStore")
    public Result addStore(@RequestHeader String Authorization, Principal principal, @RequestBody AddStoreDto addStoreDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return storeService.addStore(lgmnUserInfo, addStoreDto);
    }

    @ApiOperation(value = "认证共享店")
    @PostMapping("/authenticationStore")
    public Result authenticationStore(@RequestBody AuthenticationStoreDto authenticationStoreDto) {
        return storeService.authenticationStore(authenticationStoreDto);
    }

    @ApiOperation(value = "添加商品类型")
    @PostMapping("/addCommodityType")
    public Result addCommodityType(@RequestBody AddCommodityTypeDto addCommodityTypeDto) {
        return storeService.addCommodityType(addCommodityTypeDto);
    }

    @ApiOperation(value = "修改商品类型")
    @PostMapping("/editCommodityType")
    public Result editCommodityType(@RequestBody EditCommodityTypeDto editCommodityTypeDto) {
        return storeService.editCommodityType(editCommodityTypeDto);
    }

    @ApiOperation(value = "删除商品类型")
    @PostMapping("/deleteCommodityType")
    public Result deleteCommodityType(@RequestBody DeleteCommodityTypeDto deleteCommodityTypeDto) {
        return storeService.deleteCommodityType(deleteCommodityTypeDto);
    }

    @ApiOperation(value = "添加商品")
    @PostMapping("/addCommodity")
    public Result addCommodity(@RequestBody AddCommodityDto addCommodityDto) {
        return storeService.addCommodity(addCommodityDto);
    }

    @ApiOperation(value = "编辑商品详情")
    @PostMapping("/editCommodityDetail")
    public Result editCommodityDetail(@RequestBody EditCommodityDetailDto editCommodityDetailDto) {
        return storeService.editCommodityDetails(editCommodityDetailDto);
    }

    @ApiOperation(value = "修改商品")
    @PostMapping("/editCommodity")
    public Result editCommodity(@RequestBody EditCommodityDto editCommodityDto) {
        return storeService.editCommodity(editCommodityDto);
    }

    @ApiOperation(value = "删除商品")
    @PostMapping("/deleteCommodity")
    public Result deleteCommodity(@RequestBody DeleteCommodityDto deleteCommodityDto) {
        return storeService.deleteCommodity(deleteCommodityDto);
    }

    @ApiOperation(value = "上架或下架：0下架，1 上架")
    @PostMapping("/upperShelfAndLowerShelf")
    public Result upperShelfAndLowerShelf(@RequestBody DeleteCommodityDto deleteCommodityDto) {
        return storeService.upperShelfAndLowerShelf(deleteCommodityDto);
    }

    @ApiOperation(value = "获取七牛云上传文件Token")
    @PostMapping("/getQiNiuToken")
    public Result getQiNiuToken() {
        return storeService.getQiNiuToken();
    }

    @ApiOperation(value = "获取我的共享店")
    @PostMapping("/getMyStorePage")
    public Result getMyStorePage(@RequestHeader String Authorization, Principal principal, @RequestBody MyStorePageDto myStorePageDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return storeService.getMyStorePage(lgmnUserInfo, myStorePageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取门店")
    @PostMapping("/getStoreById")
    public Result getStoreById(@RequestBody GetStoreByIdDto getStoreByIdDto) {
        return storeService.getStoreById(getStoreByIdDto);
    }

    @ApiOperation(value = "编辑门店详情")
    @PostMapping("/editStoreDescription")
    public Result editStoreDescription(@RequestBody EditStoreDescriptionDto editStoreDescriptionDto) {
        return storeService.editStoreDescription(editStoreDescriptionDto);
    }

    @ApiOperation(value = "获取关注门店列表")
    @PostMapping("/getMyFollowPage")
    public Result getMyFollowPage(@RequestHeader String Authorization, Principal principal, @RequestBody MyFollowPageDto myFollowPageDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return storeService.getMyFollowPage(lgmnUserInfo, myFollowPageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取店列表")
    @PostMapping("/newsGetPageStore")
    public Result newsGetPageStore (@RequestBody NewsGetPageStoreDto newsGetPageStoreDto) {
        try {
            return storeService.newsGetPageStore(newsGetPageStoreDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取共享店信息")
    @PostMapping("/getShareStoreMsg")
    public Result getShareStoreMsg(@RequestBody ShareStoreMsgDto shareStoreMsgDto) {
        return storeService.getShareStoreMsg(shareStoreMsgDto);
    }
}
