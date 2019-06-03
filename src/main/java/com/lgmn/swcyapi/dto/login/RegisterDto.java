package com.lgmn.swcyapi.dto.login;

import lombok.Data;

@Data
public class RegisterDto {
    private String phone;
    private Integer smsCode;
    private String password;
    private String confirmPassword;
}
