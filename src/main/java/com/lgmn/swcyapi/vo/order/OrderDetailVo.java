package com.lgmn.swcyapi.vo.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetailVo {
    private List<OrderDetailListVo> orderDetailListVo;
    private OrderPageVo orderPageVo;
}
