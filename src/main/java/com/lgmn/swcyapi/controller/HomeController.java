package com.lgmn.swcyapi.controller;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.service.appuser.AppUserService;
import com.lgmn.userservices.basic.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Api(description = "首页")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    AppUserService appUserService;

    @ApiOperation(value = "获取首页数据")
    @PostMapping("/getHomePage")
    public Result getHomePage (@RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        try {
            return Result.success(appUserService.getHomePage(lgmnUserInfo));
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

}
