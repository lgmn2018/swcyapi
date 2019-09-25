package com.lgmn.swcyapi.dto.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddStoreForUnlicensedDto {
    private String storeName;
    private String legalPerson;
    private String provinceName;
    private String cityName;
    private String areaName;
    private String address;
    private String photo;
    private Integer industryId;
    private String industryName;
    private BigDecimal area;
    private String lat;
    private String lng;
}