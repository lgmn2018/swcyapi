package com.lgmn.swcyapi.vo.supplier;

import com.lgmn.swcy.basic.entity.SwcySupplierOrderDetailEntity;
import com.lgmn.swcy.basic.entity.SwcySupplierOrderEntity;
import lombok.Data;

import java.util.List;

@Data
public class GetLeagueStoreOrderDetailsVo {
    private SwcySupplierOrderEntity swcySupplierOrderEntity;
    private List<SwcySupplierOrderDetailEntity> swcySupplierOrderDetailEntities;
}
