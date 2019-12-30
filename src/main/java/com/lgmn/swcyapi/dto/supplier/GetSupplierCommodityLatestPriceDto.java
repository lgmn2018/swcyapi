package com.lgmn.swcyapi.dto.supplier;

import lombok.Data;

import java.util.List;

@Data
public class GetSupplierCommodityLatestPriceDto {
    private List<Integer> ids;
}
