package com.lgmn.swcyapi.service.flow;

import com.lgmn.swcy.basic.dto.SwcyFlowDto;
import com.lgmn.swcy.basic.entity.SwcyFlowEntity;
import com.lgmn.swcy.basic.service.SwcyFlowService;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Reference;

import java.util.List;

@Component
public class SwcyFlowApiService {

    @Reference(version = "${demo.service.version}")
    private SwcyFlowService swcyFlowService;

    public SwcyFlowEntity save(SwcyFlowEntity swcyFlowEntity) {
        return swcyFlowService.saveEntity(swcyFlowEntity);
    }

    public SwcyFlowEntity getFlowById(Integer id) {
        return swcyFlowService.findById(id);
    }

    public SwcyFlowEntity getFlowByPayNo(String payNo) throws Exception {
        SwcyFlowDto swcyFlowDto = new SwcyFlowDto();
        swcyFlowDto.setPayNo(payNo);
        List<SwcyFlowEntity> list = swcyFlowService.getListByDto(swcyFlowDto);
        return list.get(0);
    }
}
