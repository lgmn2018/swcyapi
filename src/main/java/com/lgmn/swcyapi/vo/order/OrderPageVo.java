package com.lgmn.swcyapi.vo.order;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyOrderEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class OrderPageVo extends LgmnVo<SwcyOrderEntity, OrderPageVo> {

    private String id;
    private Integer status;
    private BigDecimal money;
    private Timestamp orderTime;
    private Integer storeId;
    private String storeName;
    private Integer storeType;
    private String imageUrl;
    private Timestamp payTime;
    private String payChannel;
    private String payNum;
    private String uid;
    private String logisticsNum;
    private Integer addressId;
    private String address;

}
