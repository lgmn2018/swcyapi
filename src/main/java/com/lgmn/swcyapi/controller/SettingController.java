package com.lgmn.swcyapi.controller;

import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.service.SSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "系统设置")
@RestController
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    private SSettingService settingService;

    @ApiOperation(value = "获取商品等级设置")
    @PostMapping("/getSettingStars")
    public Result getSettingStars() {
        try {
            return settingService.getSettingStars();
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }
}
