package com.lgmn.swcyapi.controller;

import java.util.Date;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcyAdDto;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcyapi.service.SSwcyAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lgmn.common.result.Result;

@RestController
@RequestMapping("/SwcyAdController")
public class SwcyAdController {

    @Autowired
    SSwcyAdService sSwcyAdService;

    @PostMapping("/page")
    public Result page (@RequestBody SwcyAdDto swcyAdDto) {
        try {
            LgmnPage<SwcyAdEntity> page = sSwcyAdService.page(swcyAdDto);
            return Result.success(page);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

}