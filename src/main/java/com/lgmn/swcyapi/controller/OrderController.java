package com.lgmn.swcyapi.controller;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.order.OrderDetailDto;
import com.lgmn.swcyapi.dto.order.OrderPageDto;
import com.lgmn.swcyapi.service.OrderService;
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

}
