package com.lgmn.swcyapi.vo.store;

import com.lgmn.swcy.basic.entity.SwcyCommodityTypeEntity;
import com.lgmn.swcy.basic.entity.SwcyStoreEntity;
import lombok.Data;

import java.util.List;

@Data
public class ShopTypeAndEssentialMessageVo {
    SwcyStoreEntity swcyStoreEntity;
    List<SwcyCommodityTypeEntity> commodityTypeList;
    boolean isFollow;
}
