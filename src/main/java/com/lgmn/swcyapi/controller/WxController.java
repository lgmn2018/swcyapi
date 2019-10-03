package com.lgmn.swcyapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcyapi.dto.login.WxLoginDto;
import com.lgmn.swcyapi.dto.order.UnifiedOrderDto;
import com.lgmn.swcyapi.service.OrderService;
import com.lgmn.swcyapi.service.SwcyAdApiService;
import com.lgmn.userservices.basic.util.UserUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.nutz.dao.Cnd;
import org.nutz.lang.Lang;
import org.nutz.lang.Xmls;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.weixin.bean.WxPayUnifiedOrder;
import org.nutz.weixin.impl.WxApi2Impl;
import org.nutz.weixin.util.WxPaySign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wxApi")
public class WxController {

    @Autowired
    OrderService orderService;

    // 微信登录
    @PostMapping("/wxLogin")
    public Result wxLogin(@RequestBody WxLoginDto wxLoginDto) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse;
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx6135b0ad2c35654c&secret=0ad25062707c8e928e590dbb57f27ea3&code=" + wxLoginDto.getCode() +"&grant_type=authorization_code");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);
        try {
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            //HttpCode
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return Result.error(ResultEnum.SERVER_ERROR);
            }
            //此处处理状态码200
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String isLogin = EntityUtils.toString(httpEntity, "UTF-8");
            JSONObject object = JSON.parseObject(isLogin);
            return Result.success(object);
        } catch (IOException e) {
            return Result.serverError(e.getMessage());
        }
    }

    // 统一下单

    @PostMapping("/wxPay")
    public Result wxPay(HttpServletRequest req, @RequestBody UnifiedOrderDto unifiedOrderDto, @RequestHeader String Authorization, Principal principal) {
        LgmnUserInfo lgmnUserInfo = UserUtil.getCurrUser(principal);
        return orderService.wxPay(req, unifiedOrderDto, lgmnUserInfo);
    }

    @RequestMapping("/wxPayCallBack")
    public void wxPayCallBack(HttpServletRequest req, HttpServletResponse response) {
        try {
            orderService.wxPayCallBack(req, response);
        } catch (Exception e) {
            System.out.println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>微信支付回调失败>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
        }
    }
}
