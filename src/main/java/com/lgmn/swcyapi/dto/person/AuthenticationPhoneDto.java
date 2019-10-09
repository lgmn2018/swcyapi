package com.lgmn.swcyapi.dto.person;

import lombok.Data;

@Data
public class AuthenticationPhoneDto {
    private String phone;
    private String smsCode;
}
