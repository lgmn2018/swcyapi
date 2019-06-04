package com.lgmn.swcyapi.controller;

import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.login.ExitLoginDto;
import com.lgmn.swcyapi.dto.login.LoginDto;
import com.lgmn.swcyapi.dto.login.RegisterDto;
import com.lgmn.swcyapi.dto.login.SmsCodeDto;
import com.lgmn.swcyapi.service.sms.SmsCodeService;
import com.lgmn.swcyapi.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "登录注册")
@RestController
@RequestMapping("/account")
public class LoginController {

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "注册/重置密码")
    @PostMapping("/register")
    public Result login(@RequestBody RegisterDto registerDto){
        try {
            return userService.register(registerDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "获取验证码")
    @PostMapping("/getSmsCode")
    public Result getSmsCode (@RequestBody SmsCodeDto smsCodeDto) {
        try {
            return smsCodeService.sendSmsCode(smsCodeDto.getPhone());
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login (@RequestBody LoginDto loginDto) {
        try {
            return userService.login(loginDto);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/exitLogin")
    public Result exitLogin(@RequestBody ExitLoginDto exitLoginDto) {
        return userService.exitLogin(exitLoginDto);
    }

}
