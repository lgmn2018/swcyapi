package com.lgmn.swcyapi.vo.store;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.entity.SwcyIndustryEntity;
import com.lgmn.swcyapi.dto.store.NewsGetPageStoreDto;
import lombok.Data;

import java.util.Map;

@Data
public class NewsGetPageStoreVo {
    private NewsGetPageStoreDto newsGetPageStoreDto;
    private SwcyIndustryEntity swcyIndustryEntity;
    private LgmnPage<Map> storeMap;
}
