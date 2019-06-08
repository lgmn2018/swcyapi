package com.lgmn.swcyapi.vo.person;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.swcyapi.vo.home.HomeAdVo;
import lombok.Data;

import java.util.List;

@Data
public class PersonAndAdVo {
    private LgmnUserInfo lgmnUserInfo;
    private List<HomeAdVo> homeAdVo;
}
