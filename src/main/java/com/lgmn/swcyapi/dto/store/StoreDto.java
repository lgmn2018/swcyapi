package com.lgmn.swcyapi.dto.store;

import com.lgmn.common.domain.LgmnDto;
import lombok.Data;

@Data
public class StoreDto extends LgmnDto {
    private Integer industryId;
    private int pageNumber;
    private int pageSize;
    private String lat;
    private String lng;
}
