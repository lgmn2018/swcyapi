package com.lgmn.swcyapi.dto.store;

import lombok.Data;

import java.util.Map;

@Data
public class LeagueStoreAddCommodityDto {
    private Integer storeId;
    private Integer supplierId;
    private Map<Integer, Integer> map;
}
