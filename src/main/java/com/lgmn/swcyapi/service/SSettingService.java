package com.lgmn.swcyapi.service;

import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.service.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SSettingService {

    @Autowired
    SettingService settingService;

    public Result getSettingStars() throws Exception {
        return Result.success(settingService.getSettingStars());
    }

}
