package com.lgmn.swcyapi.vo.person;

import lombok.Data;

@Data
public class PersonInfoVo {
    private String id;
    private String avatar;
    private String nikeName;
    private boolean isAuthentication;
    private String email;
    private String phone;
}
