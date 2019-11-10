package com.lgmn.swcyapi.vo.order;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyReceivingAddressEntity;
import lombok.Data;

/**
 * 收货地址信息
 */
@Data
public class ReceivingAddressVo extends LgmnVo<SwcyReceivingAddressEntity, ReceivingAddressVo> {
    private String receiverName;
    private String receiverPhone;
    private String address;
    private String zipCode;
    private String provinceName;
    private String cityName;
    private String areaName;
}
