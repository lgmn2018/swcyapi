package com.lgmn.swcyapi.dto.supplier;

import lombok.Data;

@Data
public class LoadMoreSupplierPageDto {
    private Integer IndustryId;
    private Integer pageNumber;
    private Integer pageSize;
}
