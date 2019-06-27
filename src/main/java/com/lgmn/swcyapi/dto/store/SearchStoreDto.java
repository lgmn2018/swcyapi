package com.lgmn.swcyapi.dto.store;

import lombok.Data;

@Data
public class SearchStoreDto {
    private String name;
    private int pageNumber;
    private int pageSize;
    private String lat;
    private String lng;
}
