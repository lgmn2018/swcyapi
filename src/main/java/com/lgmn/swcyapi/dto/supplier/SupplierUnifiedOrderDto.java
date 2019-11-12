package com.lgmn.swcyapi.dto.supplier;

import com.lgmn.swcyapi.dto.order.UnifiedOrderDto;
import lombok.Data;

@Data
public class SupplierUnifiedOrderDto extends UnifiedOrderDto {
    private Integer supplierId;
}
