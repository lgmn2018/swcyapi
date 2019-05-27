package com.lgmn.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "两个码农后台管理API", value = "ceshu")
@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("/test")
    public String test(){
        return "hello";
    }
}