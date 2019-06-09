package com.lgmn.swcyapi.service.complaints;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.basicservices.basic.entity.LgmnComplaintsEntity;
import com.lgmn.basicservices.basic.service.LgmnComplaintsService;
import org.springframework.stereotype.Component;

@Component
public class ComplaintsService {
    @Reference(version = "${demo.service.version}")
    private LgmnComplaintsService lgmnComplaintsService;

    public LgmnComplaintsEntity save (LgmnComplaintsEntity lgmnComplaintsEntity) {
        return lgmnComplaintsService.saveEntity(lgmnComplaintsEntity);
    }
}
