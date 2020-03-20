package com.lgmn.swcyapi.dto.supplier;

import lombok.Data;

@Data
public class GetLeagueStoreOrderPageDto {
    private Integer storeId;
    private Integer pageNumber;
    private Integer pageSize;
}
