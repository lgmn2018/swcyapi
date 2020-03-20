package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcySupplierOrderDetailEntity;
import com.lgmn.swcy.basic.entity.SwcySupplierOrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class LeagueStoreOrderPageVo extends LgmnVo<SwcySupplierOrderEntity, LeagueStoreOrderPageVo> {
    private String id;
    private Integer status;
    private BigDecimal money;
    private Timestamp orderTime;
    private Timestamp payTime;
    private String payChannel;
    private String payNum;
    private Integer supplierId;
    private String storeOwnerId;
    private Integer storeId;
    private String logisticsNum;
    private Integer addressId;
    private String refundsReason;

    private Integer commTypeSum;

    private List<SwcySupplierOrderDetailEntity> list;
}
