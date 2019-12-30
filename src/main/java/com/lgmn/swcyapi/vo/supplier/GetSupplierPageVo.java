package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyIndustryEntity;
import lombok.Data;

@Data
public class GetSupplierPageVo extends LgmnVo<SwcyIndustryEntity, GetSupplierPageVo> {
    private Integer id;
    private String name;
    private LgmnPage<SupplierInfoVo> infoVoLgmnPage;
}
