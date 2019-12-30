package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcySupplierCategoryEntity;
import lombok.Data;

@Data
public class GetSupplierCommodityPageVo extends LgmnVo<SwcySupplierCategoryEntity, GetSupplierCommodityPageVo> {
    private Integer id;
    private String name;
    private LgmnPage<SupplierCommodityInfoVo> lgmnPage;
}
