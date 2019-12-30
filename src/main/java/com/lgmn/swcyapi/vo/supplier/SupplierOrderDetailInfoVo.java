package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcySupplierOrderDetailEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SupplierOrderDetailInfoVo extends LgmnVo<SwcySupplierOrderDetailEntity, SupplierOrderDetailInfoVo> {
    private String commodityName;
    private String commodityType;
    private String cover;
    private BigDecimal price;
    private int num;
}
