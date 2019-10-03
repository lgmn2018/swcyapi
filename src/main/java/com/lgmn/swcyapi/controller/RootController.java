package com.lgmn.swcyapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {
    @GetMapping("MP_verify_zR9sDquWUGAN7H74.txt")
    public String webAuto(){
        return "zR9sDquWUGAN7H74";
    }
}