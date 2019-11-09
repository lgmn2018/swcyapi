package com.lgmn.swcyapi.vo.order;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GetOrderPageByStoreIdVo {
    private String id;
    private Integer status;
    private Timestamp orderTime;
    private String uid;
    private Integer storeId;

    private String avatar;
    private String nikeName;

    private String address;
    private String provinceName;
    private String cityName;
    private String areaName;
}
