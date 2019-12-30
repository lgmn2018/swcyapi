package com.lgmn.swcyapi.dto.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MpRegisterDto {
    private String account;
    private String avatar;
    private String nikeName;
    private String password;
    private String puid;
    @ApiModelProperty(value = "0 注册，1忘记密码")
    private Integer type;
    private Integer gender;
}
