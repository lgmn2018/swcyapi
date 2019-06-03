package com.lgmn.swcyapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcyapi.dto.login.RegisterDto;
import com.lgmn.swcyapi.dto.login.SmsCodeDto;
import com.lgmn.swcyapi.sms.SmsKit;
import com.lgmn.yzx.starter.model.SendSmsEntity;
import com.lgmn.yzx.starter.service.Yzx_SMS_StarterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Pattern;

@Api(description = "登录注册")
@RestController
@RequestMapping("/account")
public class LoginController {

    @Autowired
    private Yzx_SMS_StarterService yzx_sms_starterService;

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public String login(@RequestBody RegisterDto registerDto){

        return "login";
    }

    @PostMapping("/getSmsCode")
    public Result getSmsCode (@RequestBody SmsCodeDto smsCodeDto) {
        String regxStr = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        String phone = smsCodeDto.getPhone();
        if(!Pattern.matches(regxStr, phone)) {
            return Result.error(ResultEnum.DATA_UNEQUAL_ERROR);
        }
        SendSmsEntity sendSmsEntity = new SendSmsEntity();
        sendSmsEntity.setMobile(phone);
        sendSmsEntity.setParam(SmsKit.randomSMSCode(6));
        JSONObject jsonObject = yzx_sms_starterService.sendSms(sendSmsEntity);
        String code = jsonObject.get("code").toString();
        if ("000000".equals(code)) {
            return Result.success("发送成功");
        }
        return Result.error(ResultEnum.SERVER_ERROR);
    }

}
