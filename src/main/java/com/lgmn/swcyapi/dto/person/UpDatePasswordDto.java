package com.lgmn.swcyapi.dto.person;

import lombok.Data;

@Data
public class UpDatePasswordDto {
    private String password;
    private String newPassword;
    private String confirmPassword;
}
