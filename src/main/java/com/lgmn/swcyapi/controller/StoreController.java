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
    public Result getStoreAdList() {
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



}
