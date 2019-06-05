package com.lgmn.swcyapi.service.appuser;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.entity.SwcyAppUserEntity;
import com.lgmn.swcy.basic.service.SwcyAppUserService;
import org.springframework.stereotype.Component;

@Component
public class AppUserService {

    @Reference(version = "${demo.service.version}")
    private SwcyAppUserService swcyAppUserService;

    public SwcyAppUserEntity save (SwcyAppUserEntity swcyAppUserEntity) {
        return swcyAppUserService.saveEntity(swcyAppUserEntity);
    }

}
