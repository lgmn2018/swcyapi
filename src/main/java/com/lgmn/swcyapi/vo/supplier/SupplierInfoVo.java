package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcySupplierEntity;
import lombok.Data;

@Data
public class SupplierInfoVo extends LgmnVo<SwcySupplierEntity, SupplierInfoVo> {
    private Integer id;
    private String name;
    private String address;
    private String lat;
    private String lng;
    private String photo;
    private String provinceName;
    private String cityName;
    private String areaName;
    private String phone;
    private Integer status;
    private String logo;
    private String description;
    private Integer starCode;
    private String brief;
}
