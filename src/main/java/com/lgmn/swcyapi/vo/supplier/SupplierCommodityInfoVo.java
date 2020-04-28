package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcySupplierCommodityEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SupplierCommodityInfoVo extends LgmnVo<SwcySupplierCommodityEntity, SupplierCommodityInfoVo> {
    private Integer id;
    private Integer supplierId;
    private Integer categoryId;
    private String name;
    private String model;
    private String cover;
    private String detail;
    private BigDecimal retailPrice;
    private String specs;
    private String notes;
}
