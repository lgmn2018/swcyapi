package com.lgmn.swcyapi.controller;

import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.login.RegisterDto;
import com.lgmn.swcyapi.dto.login.SmsCodeDto;
import com.lgmn.swcyapi.service.sms.SmsCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "登录注册")
@RestController
@RequestMapping("/account")
public class LoginController {

    static String regxPhone = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    static String regxPass = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,32}$";

    @Autowired
    private SmsCodeService smsCodeService;

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public String login(@RequestBody RegisterDto registerDto){

        return "login";
    }

    @PostMapping("/getSmsCode")
    public Result getSmsCode (@RequestBody SmsCodeDto smsCodeDto) {
        try {
            return smsCodeService.sendSmsCode(smsCodeDto.getPhone());
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

}
