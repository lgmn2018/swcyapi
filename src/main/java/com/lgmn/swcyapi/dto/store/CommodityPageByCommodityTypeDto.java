package com.lgmn.swcyapi.dto.store;

import lombok.Data;

@Data
public class CommodityPageByCommodityTypeDto {
    private Integer storeId;
    private Integer commodityTypeId;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isAdmin;
}
