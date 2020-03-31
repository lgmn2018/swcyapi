package com.lgmn.swcyapi.dto.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddStoreForUnlicensedDto {
    private Integer id;
    private String storeName;
    private String legalPerson;
    private String provinceName;
    private String cityName;
    private String areaName;
    private String address;
    private String photo;
    private Integer industryId;
    private String industryName;
    private double area;
    private String lat;
    private String lng;
    private String phone;
    private Integer starCode;
    private String brief;
}
