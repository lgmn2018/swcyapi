package com.lgmn.swcyapi.dto.supplier;

import lombok.Data;

@Data
public class LoadMoreSupplierCommodityPageDto {
    private Integer categoryId;
    private Integer pageNumber;
    private Integer pageSize;
}
