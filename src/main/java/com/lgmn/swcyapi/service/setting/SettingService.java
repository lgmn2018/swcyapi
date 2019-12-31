package com.lgmn.swcyapi.service.setting;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcyStarSettingDto;
import com.lgmn.swcy.basic.entity.SwcyStarSettingEntity;
import com.lgmn.swcy.basic.service.SwcyStarSettingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettingService {

    @Reference(version = "${demo.service.version}")
    private SwcyStarSettingService swcyStarSettingService;

    public List<SwcyStarSettingEntity> getSettingStars() throws Exception {
        SwcyStarSettingDto swcyStarSettingDto = new SwcyStarSettingDto();
        return swcyStarSettingService.getListByDto(swcyStarSettingDto);
    }
}
