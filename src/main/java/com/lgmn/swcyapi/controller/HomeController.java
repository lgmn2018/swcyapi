package com.lgmn.swcyapi.controller;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.service.HomeService;
import com.lgmn.userservices.basic.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(description = "首页")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    HomeService homeService;

    @Value("${swcyVersion.version}")
    private String version;

    @Value("${swcyVersion.isMandatoryUpdate}")
    private String isMandatoryUpdate;

    @Value("${swcyVersion.updateMsg}")
    private String updateMsg;

    @ApiOperation(value = "获取首页数据")
    @PostMapping("/getHomePage")
    public Result getHomePage (@RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return homeService.getHomePage(lgmnUserInfo);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取版本信息")
    @PostMapping("/getSwcyVersion")
    public Result getSwcyVersion () {
        Map<String, String> map = new HashMap<>();
        map.put("version", version);
        map.put("isMandatoryUpdate", isMandatoryUpdate);
        map.put("updateMsg", updateMsg);
        return Result.success(map);
    }

}
