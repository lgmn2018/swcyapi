package com.lgmn.swcyapi.controller;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.store.LeagueStoreGetSupplierDto;
import com.lgmn.swcyapi.dto.supplier.*;
import com.lgmn.swcyapi.service.SupplierService;
import com.lgmn.userservices.basic.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Api(description = "供应商")
@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @ApiOperation(value = "获取供应商列表")
    @PostMapping("/getSupplierPage")
    public Result getSupplierPage () {
        try {
            return supplierService.getSupplierPage();
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "加载更多供应商列表")
    @PostMapping("/loadMoreSupplierPage")
    public Result loadMoreSupplierPage (@RequestBody LoadMoreSupplierPageDto loadMoreSupplierPageDto) {
        try {
            return supplierService.loadMoreSupplierPage(loadMoreSupplierPageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取供应商商品列表")
    @PostMapping("/getSupplierCommodityPage")
    public Result getSupplierCommodityPage (@RequestBody GetSupplierCommodityPageDto getSupplierCommodityPageDto) {
        try {
            return supplierService.getSupplierCommodityPage(getSupplierCommodityPageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "加载跟多供应商商品列表")
    @PostMapping("/loadMoreSupplierCommodityPage")
    public Result loadMoreSupplierCommodityPage (@RequestBody LoadMoreSupplierCommodityPageDto loadMoreSupplierCommodityPageDto) {
        try {
            return supplierService.loadMoreSupplierCommodityPage(loadMoreSupplierCommodityPageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取商品最新价格")
    @PostMapping("/getSupplierCommodityLatestPrice")
    public Result getSupplierCommodityLatestPrice (@RequestBody GetSupplierCommodityLatestPriceDto getSupplierCommodityLatestPriceDto) {
        try {
            return supplierService.getSupplierCommodityLatestPrice(getSupplierCommodityLatestPriceDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取供应商订单")
    @PostMapping("/getSupplierOrderPage")
    public Result getSupplierOrderPage (@RequestHeader String Authorization, Principal principal, @RequestBody SupplierOrderPageDto supplierOrderPageDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return supplierService.getSupplierOrderPage(lgmnUserInfo, supplierOrderPageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "供应商订单确认收货")
    @PostMapping("/supplierOrderConfirmReceipt")
    public Result supplierOrderConfirmReceipt(@RequestHeader String Authorization, Principal principal, @RequestBody SupplierOrderConfirmReceiptDto supplierOrderConfirmReceiptDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return supplierService.supplierOrderConfirmReceipt(lgmnUserInfo, supplierOrderConfirmReceiptDto);
    }

    @ApiOperation(value = "申请退货")
    @PostMapping("/applyForReturn")
    public Result applyForReturn (@RequestHeader String Authorization, Principal principal, @RequestBody ApplyForReturnDto applyForReturnDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return supplierService.applyForReturn(lgmnUserInfo, applyForReturnDto);
    }

    @ApiOperation(value = "盟店获取相同类型的供应商")
    @PostMapping("/leagueStoreGetSupplier")
    public Result leagueStoreGetSupplier(@RequestHeader String Authorization, Principal principal, @RequestBody LeagueStoreGetSupplierDto leagueStoreGetSupplierDto) {
        try {
            return supplierService.leagueStoreGetSupplier(leagueStoreGetSupplierDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取盟店订单")
    @PostMapping("/getLeagueStoreOrderPage")
    public Result getLeagueStoreOrderPage (@RequestHeader String Authorization, Principal principal, @RequestBody GetLeagueStoreOrderPageDto getLeagueStoreOrderPageDto) {
        try {
            return supplierService.getLeagueStoreOrderPage(getLeagueStoreOrderPageDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取盟店订单详情")
    @PostMapping("/getLeagueStoreOrderDetails")
    public Result getLeagueStoreOrderDetails (@RequestHeader String Authorization, Principal principal, @RequestBody SupplierOrderConfirmReceiptDto supplierOrderConfirmReceiptDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return supplierService.getLeagueStoreOrderDetails(supplierOrderConfirmReceiptDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "盟店订单确认收货")
    @PostMapping("/leagueStoreOrderConfirmReceipt")
    public Result leagueStoreOrderConfirmReceipt(@RequestHeader String Authorization, Principal principal, @RequestBody SupplierOrderConfirmReceiptDto supplierOrderConfirmReceiptDto) {
        try {
            return supplierService.leagueStoreOrderConfirmReceipt(supplierOrderConfirmReceiptDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }
}
