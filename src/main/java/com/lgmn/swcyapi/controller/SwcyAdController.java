package com.lgmn.swcyapi.controller;

import java.util.Date;

import com.lgmn.swcy.basic.dto.SwcyAdDto;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcyapi.dto.swcyAd.SwcyAdSaveDto;
import com.lgmn.swcyapi.dto.swcyAd.SwcyAdUpDateDto;
import com.lgmn.swcyapi.excel.ExcelData;
import com.lgmn.swcyapi.excel.ExportExcelUtils;
import com.lgmn.swcyapi.service.SwcyAdApiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.lgmn.common.result.Result;
import java.util.ArrayList;
import java.util.List;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.utils.ObjectTransfer;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.websocket.server.PathParam;


@RestController
@RequestMapping("/swcyAdApi")
public class SwcyAdController {

    @Autowired
    SwcyAdApiService service;

    @PostMapping("/page")
    public Result page (@RequestBody SwcyAdDto dto) {
        try {
            LgmnPage<SwcyAdEntity> page = service.page(dto);
            return Result.success(page);
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/update")
    public Result update (@RequestBody SwcyAdUpDateDto upDateDto) {
        try {
            SwcyAdEntity entity = new SwcyAdEntity();
            ObjectTransfer.transValue(upDateDto, entity);
            service.update(entity);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result add (@RequestBody SwcyAdSaveDto saveDto) {
        try {
            SwcyAdEntity entity = new SwcyAdEntity();
            ObjectTransfer.transValue(saveDto, entity);
            service.add(entity);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.serverError(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public Result delete (@PathParam("id") Integer id) {
        service.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping("/detail")
    public Result detail (@PathParam("id") Integer id) {
        SwcyAdEntity entity = service.getById(id);
        return Result.success(entity);
    }


    @PostMapping("/excel")
    public void excel(@RequestBody SwcyAdDto swcyAdDto, HttpServletResponse response) throws Exception {
        List<List<Object>> rows = new ArrayList();
        LgmnPage<SwcyAdEntity> page = service.page(swcyAdDto);
        for (SwcyAdEntity entity : page.getList()) {
            List<Object> row = new ArrayList();
                        row.add(entity.getId());
                        row.add(entity.getTitle());
                        row.add(entity.getContent());
                        row.add(entity.getCover());
                        row.add(entity.getCreateTime());
                        row.add(entity.getStatus());
                        row.add(entity.getType());
                    }
        ExcelData data = new ExcelData();
        data.setName("数据");
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