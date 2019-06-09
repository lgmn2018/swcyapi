package com.lgmn.swcyapi.dto.person;

import lombok.Data;

@Data
public class MessageDto {
    private Integer type;
    private Integer pageNumber;
    private Integer pageSize;
}
