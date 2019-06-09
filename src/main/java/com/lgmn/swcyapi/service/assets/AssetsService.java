package com.lgmn.swcyapi.service.assets;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcyAssetsDto;
import com.lgmn.swcy.basic.entity.SwcyAssetsEntity;
import com.lgmn.swcy.basic.service.SwcyAssetsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssetsService {
    @Reference(version = "${demo.service.version}")
    private SwcyAssetsService swcyAssetsService;

    public SwcyAssetsEntity getAsstesByUid (String uid) throws Exception {
        SwcyAssetsDto swcyAssetsDto = new SwcyAssetsDto();
        swcyAssetsDto.setUid(uid);
        List<SwcyAssetsEntity> list = swcyAssetsService.getListByDto(swcyAssetsDto);
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }
}
