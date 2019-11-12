package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcySupplierCommodityEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SupplierCommodityLatestPriceVo extends LgmnVo<SwcySupplierCommodityEntity, SupplierCommodityLatestPriceVo> {
    private Integer id;
    private String name;
    private String cover;
    private BigDecimal retailPrice;
    private String specs;
    private int status;
}
