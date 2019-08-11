package com.lgmn.swcyapi.dto.swcyAd;

import java.util.Date;
import lombok.Data;

@Data
public class SwcyAdSaveDto {
    //标题    
    private String title;
        
    private Object content;
    //封面    
    private String cover;
    //创建时间    
    private Date createTime;
    //状态 0：不显示，1：显示    
    private Integer status;
    //类型 0：首页轮播 1：经济传递区 2：商家轮播 3：个人中心广告    
    private Integer type;

}