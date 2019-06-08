package com.lgmn.swcyapi.vo.home;

import lombok.Data;

import java.util.List;

@Data
public class HomePageVo {
    private String id;
    private String avatar;
    private String nikeName;
    private Integer groupPower;
    private Integer personPower;
    private Integer score;
    List<HomeAdVo> homeAdType0Vos;
    List<HomeAdVo> homeAdType1Vos;
}
