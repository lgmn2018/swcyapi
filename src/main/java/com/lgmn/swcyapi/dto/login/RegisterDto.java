package com.lgmn.swcyapi.dto.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegisterDto {
    private String phone;
    private String smsCode;
    private String password;
    private String confirmPassword;

    @ApiModelProperty(value = "0 注册，1忘记密码")
    private Integer type;
}
