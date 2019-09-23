package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.result.Result;
import com.lgmn.swcy.basic.entity.SwcyOrderDetailEntity;
import com.lgmn.swcy.basic.entity.SwcyOrderEntity;
import com.lgmn.swcy.basic.entity.SwcyStoreEntity;
import com.lgmn.swcy.basic.service.SwcyStoreService;
import com.lgmn.swcyapi.dto.order.OrderDetailDto;
import com.lgmn.swcyapi.dto.order.OrderPageDto;
import com.lgmn.swcyapi.service.order.SOrderDetailService;
import com.lgmn.swcyapi.service.order.SOrderService;
import com.lgmn.swcyapi.service.store.SStoreService;
import com.lgmn.swcyapi.vo.order.OrderDetailListVo;
import com.lgmn.swcyapi.vo.order.OrderDetailVo;
import com.lgmn.swcyapi.vo.order.OrderPageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderService {

    @Autowired
    SOrderService sOrderService;

    @Autowired
    SStoreService sStoreService;

    @Autowired
    SOrderDetailService sOrderDetailService;

    public Result getOrderPage (OrderPageDto orderPageDto, String uid) throws Exception {
        LgmnPage<SwcyOrderEntity> lgmnPage = sOrderService.getOrderPage(orderPageDto, uid);
        LgmnPage<OrderPageVo> orderPageVoLgmnPage = new OrderPageVo().getVoPage(lgmnPage, OrderPageVo.class);
        for (OrderPageVo orderPageVo : orderPageVoLgmnPage.getList()) {
            SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(orderPageVo.getStoreId());
            List<SwcyOrderDetailEntity> orderDetailEntities = sOrderDetailService.getOrderDetailsByOrderId(orderPageVo.getId());
            orderPageVo.setStoreName(swcyStoreEntity.getStoreName());
            orderPageVo.setImageUrl(orderDetailEntities.get(0).getCover());
        }
        return Result.success(orderPageVoLgmnPage);
    }

    public Result getOrderDetailById (OrderDetailDto orderDetailDto) throws Exception {
        SwcyOrderEntity swcyOrderEntity = sOrderService.getOrderById(orderDetailDto.getOrderId());
        OrderPageVo orderPageVo = new OrderPageVo().getVo(swcyOrderEntity, OrderPageVo.class);
        List<SwcyOrderDetailEntity> swcyOrderDetailEntities = sOrderDetailService.getOrderDetailsByOrderId(swcyOrderEntity.getId());
        List<OrderDetailListVo> orderDetailListVos = new OrderDetailListVo().getVoList(swcyOrderDetailEntities, OrderDetailListVo.class);
        SwcyStoreEntity swcyStoreEntity = sStoreService.getStoreById(swcyOrderEntity.getStoreId());
        orderPageVo.setImageUrl(swcyStoreEntity.getPhoto());
        orderPageVo.setStoreName(swcyStoreEntity.getStoreName());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderPageVo(orderPageVo);
        orderDetailVo.setOrderDetailListVo(orderDetailListVos);
        return Result.success(orderDetailVo);
    }

}
