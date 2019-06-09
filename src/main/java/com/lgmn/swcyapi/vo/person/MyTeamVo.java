package com.lgmn.swcyapi.vo.person;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyAppUserEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyTeamVo extends LgmnVo<SwcyAppUserEntity, MyTeamVo> {
    private String uid;
    private String avatar;
    private String nikeName;
    private String phone;
    private BigDecimal achievement;
}
