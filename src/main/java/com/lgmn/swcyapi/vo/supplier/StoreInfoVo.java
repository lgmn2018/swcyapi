package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyStoreEntity;
import lombok.Data;

@Data
public class StoreInfoVo extends LgmnVo<SwcyStoreEntity, StoreInfoVo> {
    private String storeName;
    private String address;
    private String photo;
    private String provinceName;
    private String cityName;
    private String areaName;
    private String phone;
}
