package com.lgmn.swcyapi.vo.home;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import lombok.Data;

@Data
public class HomeAdVo extends LgmnVo<SwcyAdEntity, HomeAdVo> {
    private Integer id;
    private String title;
    private String cover;
    private Integer type;
}
