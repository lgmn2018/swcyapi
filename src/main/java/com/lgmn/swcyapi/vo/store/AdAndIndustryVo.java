package com.lgmn.swcyapi.vo.store;

import com.lgmn.swcyapi.vo.home.HomeAdVo;
import lombok.Data;

import java.util.List;

@Data
public class AdAndIndustryVo {
    private List<StoreIndustryVo> storeIndustryVos;
    private List<HomeAdVo> homeAdVos;
}
