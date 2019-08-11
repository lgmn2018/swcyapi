package com.lgmn.swcyapi.service;

import java.util.Date;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.service.LgmnAbstractApiService;
import com.lgmn.swcy.basic.dto.SwcyAdDto;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcy.basic.service.SwcyAdService;
import org.springframework.stereotype.Component;


@Component
public class SwcyAdApiService extends LgmnAbstractApiService<SwcyAdEntity, SwcyAdDto, Integer, SwcyAdService> {

    @Reference(version = "${demo.service.version}")
    private SwcyAdService service;

    @Override
    public void initService() {
        setService(service);
    }
}