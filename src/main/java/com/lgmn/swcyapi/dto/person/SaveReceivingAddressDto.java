package com.lgmn.swcyapi.dto.person;

import lombok.Data;

@Data
public class SaveReceivingAddressDto {
    private String receiverName;
    private String receiverPhone;
    private String address;
    private String zipCode;
    private String provinceName;
    private String cityName;
    private String areaName;
}
