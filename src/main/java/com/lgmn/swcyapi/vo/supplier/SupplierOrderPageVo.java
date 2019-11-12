package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcySupplierOrderEntity;
import com.lgmn.swcyapi.vo.order.ReceivingAddressVo;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class SupplierOrderPageVo extends LgmnVo<SwcySupplierOrderEntity, SupplierOrderPageVo> {
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
    private SupplierInfoVo supplierInfoVo;
    private ReceivingAddressVo receivingAddressVo;
    private StoreInfoVo storeInfoVo;
    private List<SupplierOrderDetailInfoVo> supplierOrderDetailInfoVos;

}
