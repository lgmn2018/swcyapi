package com.lgmn.swcyapi.controller;

import java.util.ArrayList;
import java.util.List;

import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.utils.ObjectTransfer;
import com.lgmn.swcy.basic.dto.SwcyAdDto;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcyapi.excel.ExcelData;
import com.lgmn.swcyapi.excel.ExportExcelUtils;
import com.lgmn.swcyapi.service.SSwcyAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lgmn.common.result.Result;

import javax.servlet.http.HttpServletResponse;

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

    @PostMapping("/update")
    public Result update (@RequestBody SwcyAdDto swcyAdDto) {
        try {
            SwcyAdEntity swcyAdEntity = new SwcyAdEntity();
            ObjectTransfer.transValue(swcyAdDto, swcyAdEntity);
            sSwcyAdService.update(swcyAdEntity);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result add (@RequestBody SwcyAdDto swcyAdDto) {
        try {
            SwcyAdEntity swcyAdEntity = new SwcyAdEntity();
            ObjectTransfer.transValue(swcyAdDto, swcyAdEntity);
            sSwcyAdService.add(swcyAdEntity);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public Result delete (@RequestBody SwcyAdDto swcyAdDto) {
        sSwcyAdService.deleteById(swcyAdDto.getId());
        return Result.success("删除成功");
    }

    @PostMapping("/detail")
    public Result detail (@RequestBody SwcyAdDto swcyAdDto) {
        SwcyAdEntity swcyAdEntity = sSwcyAdService.getById(swcyAdDto.getId());
        return Result.success(swcyAdEntity);
    }


    @PostMapping("/excel")
    public void excel(@RequestBody SwcyAdDto swcyAdDto, HttpServletResponse response) throws Exception {
        List<List<Object>> rows = new ArrayList();
        LgmnPage<SwcyAdEntity> page = sSwcyAdService.page(swcyAdDto);
        for (SwcyAdEntity swcyAdEntity : page.getList()) {
            List<Object> row = new ArrayList();
            row.add(swcyAdEntity.getId());
            row.add(swcyAdEntity.getTitle());
            row.add(swcyAdEntity.getType());
            row.add(swcyAdEntity.getCreateTime());
            row.add(swcyAdEntity.getContent());
            row.add(swcyAdEntity.getCover());
            row.add(swcyAdEntity.getStatus());
            rows.add(row);
        }
        ExcelData data = new ExcelData();
        data.setName("APP用户数据导出");
        List<String> titles = new ArrayList();
        titles.add("昵称");
        titles.add("手机号");
        titles.add("推荐人");
        titles.add("推荐数量");
        titles.add("开户银行");
        titles.add("银行卡号");
        titles.add("开户城市");
        titles.add("姓名");
        titles.add("身份证号码");
        titles.add("门店");
        titles.add("销售");
        titles.add("跟进等级");
        data.setTitles(titles);
        data.setRows(rows);
        ExportExcelUtils.exportExcel(response, "数据导出.xlsx", data);
    }

}