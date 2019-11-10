package com.lgmn.swcyapi.vo.order;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyOrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单基本信息
 */
@Data
public class OrderEssentialInfoVo extends LgmnVo<SwcyOrderEntity, OrderEssentialInfoVo> {
    private String id;
    private Integer status;
    private BigDecimal money;
    private Timestamp orderTime;
    private Timestamp payTime;
    private String payChannel;
    private String uid;
    private Integer storeId;
    private Integer addressId;
}
