package com.lgmn.swcyapi.vo.order;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyOrderDetailEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailListVo extends LgmnVo<SwcyOrderDetailEntity, OrderDetailListVo> {
    private int id;
    private String orderId;
    private int commodityId;
    private String commodityName;
    private String commodityType;
    private String cover;
    private BigDecimal price;
    private int num;

}
