package com.lgmn.swcyapi.dto.store;

import lombok.Data;

@Data
public class AddStoreDto extends AddStoreForUnlicensedDto {
    private String licenseCode;
    private String licensePhoto;
}
