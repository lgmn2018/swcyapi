package com.lgmn.swcyapi.vo.store;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyIndustryEntity;
import lombok.Data;

@Data
public class StoreIndustryVo extends LgmnVo<SwcyIndustryEntity, StoreIndustryVo> {
    private int id;
    private Integer pid;
    private String name;
}
