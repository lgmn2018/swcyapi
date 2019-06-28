package com.lgmn.swcyapi.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.service.LgmnAbstractApiService;
import com.lgmn.swcy.basic.dto.SwcyAdDto;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcy.basic.service.SwcyAdService;
import org.springframework.stereotype.Component;

@Component
public class SSwcyAdService extends LgmnAbstractApiService<SwcyAdEntity, SwcyAdDto, Integer, SwcyAdService> {

    @Reference(version = "${demo.service.version}")
    private SwcyAdService swcyAdService;

    @Override
    public void initService() {
        setService(swcyAdService);
    }
}
