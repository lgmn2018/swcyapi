package com.lgmn.swcyapi.controller;

import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.store.StoreDto;
import com.lgmn.swcyapi.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "商家")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @ApiOperation(value = "获取单家广告")
    @PostMapping("/getStoreAdList")
    public Result getStoreAdList() {
        try {
            return storeService.getStoreAdListAndIndustryList();
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
}
