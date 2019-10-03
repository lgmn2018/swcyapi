package com.lgmn.swcyapi.dto.order;

import lombok.Data;

import java.util.Map;

@Data
public class UnifiedOrderDto {
    private Map<Integer, Integer> idAndCount;
    private Integer storeId;
    private Integer addressId;
}
