package com.lgmn.swcyapi.dto.order;

import lombok.Data;

@Data
public class GetOrderPageByStoreIdDto {
    private Integer storeId;
    private Integer pageNumber;
    private Integer pageSize;
}
