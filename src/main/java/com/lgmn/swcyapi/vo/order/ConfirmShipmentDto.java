package com.lgmn.swcyapi.vo.order;

import lombok.Data;

/**
 * 确认发货
 */
@Data
public class ConfirmShipmentDto {
    private String orderId;
    // 物流单号（必须填写，若不是快递发货，可写其他，如：货拉拉，或自己带货，或，无）
    private String logisticsNum;
}
