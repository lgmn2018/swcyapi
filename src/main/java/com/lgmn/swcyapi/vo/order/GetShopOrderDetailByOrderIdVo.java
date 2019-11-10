package com.lgmn.swcyapi.vo.order;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyOrderEntity;
import lombok.Data;
import java.util.List;

@Data
public class GetShopOrderDetailByOrderIdVo extends LgmnVo<SwcyOrderEntity, GetShopOrderDetailByOrderIdVo> {
    // 订单明细列表
    private List<OrderDetailListVo> orderDetailListVos;

    // 订单基本信息
    private OrderEssentialInfoVo orderEssentialInfoVo;

    // 订单收货地址
    private ReceivingAddressVo receivingAddressVo;

    // 下单用户信息
    private String avatar;
    private String nikeName;

}
