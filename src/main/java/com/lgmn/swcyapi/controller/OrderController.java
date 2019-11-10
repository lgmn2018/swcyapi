package com.lgmn.swcyapi.controller;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.order.GetOrderPageByStoreIdDto;
import com.lgmn.swcyapi.dto.order.OrderDetailDto;
import com.lgmn.swcyapi.dto.order.OrderPageDto;
import com.lgmn.swcyapi.service.OrderService;
import com.lgmn.swcyapi.vo.order.ConfirmShipmentDto;
import com.lgmn.userservices.basic.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Api(description = "订单")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "获取订单列表")
    @PostMapping("/getOrderPage")
    public Result getOrderPage (@RequestHeader String Authorization, Principal principal, @RequestBody OrderPageDto orderPageDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return orderService.getOrderPage(orderPageDto, lgmnUserInfo.getId());
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取订单详情")
    @PostMapping("/getOrderDetailById")
    public Result getOrderDetailById (@RequestHeader String Authorization, Principal principal, @RequestBody OrderDetailDto orderDetailDto) {
        try {
            return orderService.getOrderDetailById(orderDetailDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "确认收货")
    @PostMapping("/confirmReceipt")
    public Result confirmReceipt (@RequestHeader String Authorization, Principal principal, @RequestBody OrderDetailDto orderDetailDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return orderService.confirmReceipt(orderDetailDto, lgmnUserInfo);
    }

    @ApiOperation(value = "获取门店订单Page")
    @PostMapping("/getOrderPageByShopId")
    public Result getOrderPageByShopId(@RequestHeader String Authorization, Principal principal, @RequestBody GetOrderPageByStoreIdDto getOrderPageByStoreIdDto) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return orderService.getOrderPageByStoreId(lgmnUserInfo, getOrderPageByStoreIdDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取共享店订单详情")
    @PostMapping("/getShopOrderDetailByOrderId")
    public Result getShopOrderDetailByOrderId(@RequestHeader String Authorization, Principal principal, @RequestBody OrderDetailDto orderDetailDto) {
        try {
            return orderService.getShopOrderDetailByOrderId(orderDetailDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "确认订单")
    @PostMapping("/confirmationOfOrder")
    public Result confirmationOfOrder (@RequestHeader String Authorization, Principal principal, @RequestBody OrderDetailDto orderDetailDto) {
        return orderService.confirmationOfOrder(orderDetailDto);
    }

    @ApiOperation(value = "确认发货")
    @PostMapping("/confirmShipment")
    public Result confirmShipment(@RequestHeader String Authorization, Principal principal, @RequestBody ConfirmShipmentDto confirmShipmentDto) {
        return orderService.confirmShipment(confirmShipmentDto);
    }


}
