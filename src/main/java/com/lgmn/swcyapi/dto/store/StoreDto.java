package com.lgmn.swcyapi.dto.store;

import com.lgmn.common.domain.LgmnDto;
import lombok.Data;

import java.util.List;

@Data
public class StoreDto {
    private List<Integer> industryId;
    private int pageNumber;
    private int pageSize;
    private String lat;
    private String lng;
}
