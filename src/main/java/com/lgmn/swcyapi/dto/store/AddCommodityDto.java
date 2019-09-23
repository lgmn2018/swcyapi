package com.lgmn.swcyapi.dto.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddCommodityDto {
    private Integer typeId;
    private String name;
    private String cover;
    private String specs;
    private BigDecimal price;
}
